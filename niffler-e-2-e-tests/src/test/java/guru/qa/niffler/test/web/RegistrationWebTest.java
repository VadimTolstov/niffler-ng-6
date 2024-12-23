package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.RegisterPage;
import guru.qa.niffler.utils.RandomDataUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static guru.qa.niffler.utils.RandomDataUtils.randomUsername;

@WebTest
public class RegistrationWebTest {
    private final static String HEADER = "Sign up";
    private final static String HEADER_LOGIN = "Log in";
    private static final Config CFG = Config.getInstance();

    @Test
    @User
    @DisplayName("Регистрация нового пользователя")
    void successRegistration() {
        String name = randomUsername();
        String password = RandomDataUtils.randomPassword();
        String successRegister = "Congratulations! You've registered!";

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .clickRegisterPage(new RegisterPage())
                .setUsername(name)
                .setPassword(password)
                .setPasswordSubmit(password)
                .successSubmit()
                .checkThatPageLoaded();
    }

    @Test
    @User
    @DisplayName("Регистрация пользователя с логином и паролем из одного символа")
    void validationLoginPassword() {
        String errorName = "Allowed username length should be from 3 to 50 characters";
        String errorPassword = "Allowed password length should be from 3 to 12 characters";
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .clickRegisterPage(new RegisterPage())
                .setUsername("A")
                .setPassword("A")
                .setPasswordSubmit("A")
                .clickSubmit()
                .checkError(errorName, errorPassword, errorPassword);
    }


    @Test
    @User
    @DisplayName("Проверить что страница регистрации загружается")
    void checkingHeader() {

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .clickRegisterPage(new RegisterPage())
                .checkThatPageLoaded();
    }


    @Test
    @User
    @DisplayName("Регистрация пользователя с зарегистрированным логином")
    void mainPageShouldBeDisplayedAfterSuccessLogin() {
        String username = "books";
        String password = "12345";

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .clickRegisterPage(new RegisterPage())
                .setUsername(username)
                .setPassword(password)
                .setPasswordSubmit(password)
                .clickSubmit()
                .checkError("Username `" + username + "` already exists");
    }
}
