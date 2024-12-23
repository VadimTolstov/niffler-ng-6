package guru.qa.niffler.jupiter.extension;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import guru.qa.niffler.api.core.ThreadSafeCookiesStore;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.Token;
import guru.qa.niffler.model.rest.CategoryJson;
import guru.qa.niffler.model.rest.SpendJson;
import guru.qa.niffler.model.rest.TestData;
import guru.qa.niffler.model.rest.UserJson;
import guru.qa.niffler.page.MainPage;
import guru.qa.niffler.service.impl.AuthApiClient;
import guru.qa.niffler.service.impl.SpendApiClient;
import guru.qa.niffler.service.impl.UserDataApiClient;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;
import org.openqa.selenium.Cookie;

import java.util.List;

import static guru.qa.niffler.model.rest.FriendState.*;

public class ApiLoginExtension implements BeforeTestExecutionCallback, ParameterResolver {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(ApiLoginExtension.class);

    private final AuthApiClient authApiClient = new AuthApiClient();
    private final UserDataApiClient usersClient = new UserDataApiClient();
    private final SpendApiClient spendApiClient = new SpendApiClient();
    private final static Config CFG = Config.getInstance();
    private final boolean setupBrowser;

    private ApiLoginExtension(boolean setupBrowser) {
        this.setupBrowser = setupBrowser;
    }

    private ApiLoginExtension() {
        this.setupBrowser = true;
    }

    public static ApiLoginExtension rest() {
        return new ApiLoginExtension(false);
    }

    @Override
    public void beforeTestExecution(@NotNull ExtensionContext context) throws Exception {
        AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), ApiLogin.class)
                .ifPresent(apiLogin -> {

                    final UserJson userToLogin;
                    final UserJson userFromUserExtension = UserExtension.getUserJson();
                    if ("".equals(apiLogin.username()) || "".equals(apiLogin.password())) {
                        if (userFromUserExtension == null) {
                            throw new IllegalArgumentException("Если указана @ApiLogin то должна быть указана @User");
                        }
                        userToLogin = userFromUserExtension;
                    } else {
                        UserJson fakeUser = new UserJson(
                                apiLogin.username(),
                                new TestData(
                                        apiLogin.password()
                                )
                        );
                        if (userFromUserExtension != null) {
                            throw new IllegalArgumentException("Если указали username and password то мы не должны генерировать юзера");
                        }
                        UserExtension.setUser(fakeUser);
                        userToLogin = fakeUser;
                    }

                    // Получаем друзей и фильтруем по состоянию
                    List<UserJson> friends = usersClient
                            .friends(userToLogin.username(), null)
                            .stream()
                            .filter(user -> user.friendState() == FRIEND)
                            .toList();

                    // Получаем входящие приглашения
                    List<UserJson> incomeInvitations = friends.stream()
                            .filter(user -> user.friendState() == INVITE_RECEIVED)
                            .toList();

                    // Получаем исходящие приглашения
                    List<UserJson> outcomeInvitations = usersClient
                            .getAllUsers(userToLogin.username(), null)
                            .stream()
                            .filter(user -> user.friendState() == INVITE_SENT)
                            .toList();

                    // Получаем категории и траты
                    List<CategoryJson> categories = spendApiClient.getAllCategories(userToLogin.username(), false);
                    categories.addAll(spendApiClient.getAllCategories(userToLogin.username(), true));
                    List<SpendJson> spends = spendApiClient.getAllSpends(userToLogin.username(), null, null, null);

                    // Добавляем данные в TestData пользователя
                    userToLogin.addTestData(
                            new TestData(
                                    userToLogin.testData().password(),
                                    categories,
                                    spends,
                                    incomeInvitations,
                                    outcomeInvitations,
                                    friends
                            )
                    );

                    final String token = authApiClient.singIn(
                            userToLogin.username(),
                            userToLogin.testData().password()
                    );
                    setToken(token);
                    if (setupBrowser) {
                        Selenide.open(CFG.frontUrl());
                        Selenide.localStorage().setItem("id_token", getToken());
                        WebDriverRunner.getWebDriver().manage().addCookie(getJsessionIdCookie());

                        Selenide.open(MainPage.URL, MainPage.class).checkThatPageLoaded();
                    }
                });
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(String.class)
                && AnnotationSupport.isAnnotated(parameterContext.getParameter(), Token.class);
    }

    @Override
    public String resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return "Bearer " + getToken();
    }

    public static void setToken(String token) {
        TestMethodContextExtension.context().getStore(NAMESPACE).put("token", token);
    }

    public static String getToken() {
        return TestMethodContextExtension.context().getStore(NAMESPACE).get("token", String.class);
    }

    public static void setCode(String code) {
        TestMethodContextExtension.context().getStore(NAMESPACE).put("code", code);
    }

    public static String getCode() {
        return TestMethodContextExtension.context().getStore(NAMESPACE).get("code", String.class);
    }

    public static Cookie getJsessionIdCookie() {
        return new Cookie(
                "JSESSIONID",
                ThreadSafeCookiesStore.INSTANCE.cookieValue("JSESSIONID")
        );
    }
}
