package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import com.github.javafaker.Faker;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.Spending;
import guru.qa.niffler.jupiter.annotation.meta.User;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.utils.RandomDataUtils;
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
                .setUsername("q")
                .setPassword("k")
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

    @User(
            categories = {
                    @Category(name = "cat_1",archived = false),
                    @Category(name = "cat_2",archived = true)
            },
            spendings = {
                    @Spending(category = "cat_3",
                    description = "test_spend",
                    amount = 100)
            }
    )
    @Test
    @DisplayName("Авторизация пользователя")
    void userAuthorization(UserJson user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.testData().password())
                .checkingHeader();
    }
}
