package guru.qa.niffler.api;

import guru.qa.niffler.service.RestClient;
import io.qameta.allure.Step;
import org.apache.hc.core5.http.HttpStatus;
import org.junit.jupiter.api.Assertions;
import retrofit2.Response;

import javax.annotation.ParametersAreNonnullByDefault;
import java.io.IOException;

@ParametersAreNonnullByDefault
public class AuthApiClient extends RestClient {
    private final AuthApi authApi;

    public AuthApiClient() {
        super(CFG.authUrl());
        this.authApi = retrofit.create(AuthApi.class);
    }

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

    @Step("Регистрация нового пользователя c именем: {username}")
    public void registerUser(String username, String password, String passwordSubmit, String _csrf) {
        final Response<Void> response;
        try {
            response = authApi.register(username, password, passwordSubmit, _csrf).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertEquals(HttpStatus.SC_CREATED, response.code());
    }
}
