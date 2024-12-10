package guru.qa.niffler.api;


import com.fasterxml.jackson.databind.JsonNode;
import guru.qa.niffler.api.core.interceptor.AuthorizedCodeInterceptor;
import guru.qa.niffler.api.core.store.AuthCodeStore;
import guru.qa.niffler.service.RestClient;
import guru.qa.niffler.service.ThreadSafeCookiesStore;
import guru.qa.niffler.utils.OauthUtils;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.core5.http.HttpStatus;
import org.junit.jupiter.api.Assertions;
import retrofit2.Response;
import retrofit2.converter.jackson.JacksonConverterFactory;

import javax.annotation.ParametersAreNonnullByDefault;
import java.io.IOException;
import java.util.Objects;

import static guru.qa.niffler.api.enums.Token.CSRF;
import static okhttp3.logging.HttpLoggingInterceptor.Level.BODY;

@Slf4j
@ParametersAreNonnullByDefault
public class AuthApiClient extends RestClient {
    private static final String CLIENT_ID = "client";
    private static final String RESPONSE_TYPE = "code";
    private static final String GRANT_TYPE = "authorization_code";
    private static final String SCOPE = "openid";
    private static final String CODE_CHALLENGE_METHOD = "S256";
    private static final String REDIRECT_URI = CFG.frontUrl() + "authorized";

    private final AuthApi authApi;

    public AuthApiClient() {
        super(
                CFG.authUrl(),
                true,
                JacksonConverterFactory.create(),
                BODY,
                new AuthorizedCodeInterceptor()
        );
        this.authApi = retrofit.create(AuthApi.class);
    }

    /**
     * Метод для запроса формы регистрации для получения CSRF токена.
     */
    @Step("Запрос формы регистрации для получения CSRF токена")
    public void requestRegisterForm() {
        final Response<Void> response;
        try {
            response = authApi.requestRegisterForm().execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertEquals(HttpStatus.SC_OK, response.code());
    }

    /**
     * Метод для входа в систему и получения токена.
     *
     * @param username Имя пользователя.
     * @param password Пароль пользователя.
     * @return Токен доступа.
     */
    @Step("Получения token пользователя username = {username}, password = {password}")
    public String singIn(String username, String password) {
        final String codeVerifier = OauthUtils.generateCodeVerifier();
        ThreadSafeCookiesStore.INSTANCE.removeAll();
        log.info("Войдите в систему под: username = [{}], password = [{}]", username, password);

        authorize(OauthUtils.generateCodeChallange(codeVerifier));
        login(username, password);
        return token(codeVerifier);
    }

    /**
     * Метод для выполнения запроса на авторизацию.
     *
     * @param codeChallenge Код верификатора для OAuth2.
     */
    @Step("Запрос на авторизацию")
    private void authorize(final String codeChallenge) {
        final Response<Void> response;
        try {
            response = authApi.authorize(
                    RESPONSE_TYPE,
                    CLIENT_ID,
                    SCOPE,
                    REDIRECT_URI,
                    codeChallenge,
                    CODE_CHALLENGE_METHOD
            ).execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        Assertions.assertEquals(HttpStatus.SC_OK, response.code());
    }

    /**
     * Метод для выполнения запроса на вход в систему.
     *
     * @param username Имя пользователя.
     * @param password Пароль пользователя.
     */
    @Step("Запрос на вход в систему под username = {username}, password = {password}")
    private void login(String username, String password) {
        final Response<Void> response;
        try {
            response = authApi.login(
                            username,
                            password,
                            ThreadSafeCookiesStore.INSTANCE.cookieValue(CSRF.getCookieName()))
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        Assertions.assertEquals(HttpStatus.SC_OK, response.code());
    }

    /**
     * Метод для регистрации нового пользователя.
     *
     * @param username       Имя пользователя.
     * @param password       Пароль пользователя.
     * @param passwordSubmit Подтверждение пароля.
     * @param _csrf          CSRF-токен.
     */
    @Step("Регистрация нового пользователя c именем: {username}")
    public void registerUser(String username, String password, String passwordSubmit, String _csrf) {
        final Response<Void> response;
        try {
            response = authApi.register(username, password, passwordSubmit, _csrf).execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        Assertions.assertEquals(HttpStatus.SC_CREATED, response.code());
    }

    /**
     * Метод для получения токена доступа.
     *
     * @param codeVerifier Код верификатора для OAuth2.
     * @return Токен доступа.
     */
    @Step("Получение токена")
    private String token(String codeVerifier) {
        final Response<JsonNode> response;
        try {
            var code = Objects.requireNonNull(AuthCodeStore.INSTANCE.getCode());
            response = authApi.token(
                    code,
                    REDIRECT_URI,
                    codeVerifier,
                    GRANT_TYPE,
                    CLIENT_ID
            ).execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        Assertions.assertEquals(HttpStatus.SC_OK, response.code());
        Assertions.assertNotNull(response.body());
        return response.body().get("id_token").asText();
    }
}
