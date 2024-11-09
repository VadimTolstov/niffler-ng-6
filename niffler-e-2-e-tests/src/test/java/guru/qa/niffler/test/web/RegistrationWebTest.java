package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.utils.RandomDataUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@WebTest
public class RegistrationWebTest {
    private final static String HEADER = "Sign up";
    private final static String HEADER_LOGIN = "Log in";
    private static final Config CFG = Config.getInstance();

    @Test
    @DisplayName("Регистрация нового пользователя")
    void successRegistration() {
        String name = RandomDataUtils.randomUsername();
        String password = RandomDataUtils.randomPassword();
        String successRegister = "Congratulations! You've registered!";

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .clickRegisterPage()
                .setUsername(name)
                .setPassword(password)
                .setPasswordSubmit(password)
                .SignInAuthorizationPage()
                .shouldSuccessRegister(successRegister);
    }

    @Test
    @DisplayName("Регистрация пользователя с логином и паролем из одного символа")
    void validationLoginPassword() {
        String errorName = "Allowed username length should be from 3 to 50 characters";
        String errorPassword = "Allowed password length should be from 3 to 12 characters";
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .clickRegisterPage()
                .setUsername("A")
                .setPassword("A")
                .setPasswordSubmit("A")
                .SignInAuthorizationPage()
                .shouldErrorMessage(errorName)
                .shouldErrorMessage(errorPassword);
    }

    @Test
    @DisplayName("Показать значения в полях Password, Submit password")
    void openPassword() {
        String password = RandomDataUtils.randomPassword();

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .clickRegisterPage()
                .setPassword(password)
                .setPasswordSubmit(password)
                .showPassword();
    }

    @Test
    @DisplayName("Вернуться на страницу авторизации")
    void goBackToTheAuthorizationPage() {

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .clickRegisterPage()
                .openLogIn()
                .checkTextHeader(HEADER_LOGIN);
    }

    @Test
    @DisplayName("Проверить отображения текста заголовка")
    void checkingHeader() {

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .clickRegisterPage()
                .checkTextHeader(HEADER);
    }


    @Test
    @DisplayName("Регистрация пользователя с зарегистрированным логином")
    void mainPageShouldBeDisplayedAfterSuccessLogin() {
        String username = "books";
        String password = "12345";

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .clickRegisterPage()
                .setUsername(username)
                .setPassword(password)
                .setPasswordSubmit(password)
                .SignInAuthorizationPage()
                .shouldErrorMessage("Username `" + username + "` already exists");
    }
}