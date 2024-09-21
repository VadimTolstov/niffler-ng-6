package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import com.github.javafaker.Faker;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@WebTest
public class LoginWebTest {
    private static final String ERROR_MESSAGE = "Неверные учетные данные пользователя";
    private static final String HEADER = "Log in";
    private static final Config CFG = Config.getInstance();

    @Test
    @DisplayName("Авторизация пользователя с невалидными данными")
    void unsuccessfulAuthorization() {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .setUsername("f")
                .setPassword("g")
                .clickLogIn()
                .errorMessage(ERROR_MESSAGE);
    }

    @Test
    @DisplayName("Показать пароль")
    void openPassword() {
        String password = new Faker().code().ean8();

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .setPassword(password)
                .showPassword();
    }

    @Test
    @DisplayName("Проверить отображения текста заголовка")
    void checkingHeader() {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .checkTextHeader(HEADER);
    }

    @Test
    @DisplayName("Авторизация пользователя")
    void userAuthorization() {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login("books", "12345")
                .checkingHeader();
    }
}
