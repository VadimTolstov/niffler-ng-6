package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import com.github.javafaker.Faker;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.Spending;
import guru.qa.niffler.jupiter.annotation.meta.User;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.MainPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@WebTest
public class LoginWebTest {
    private static final String ERROR_MESSAGE = "Неверные учетные данные пользователя";

    @Test
    @DisplayName("Авторизация пользователя с невалидными данными")
    void unsuccessfulAuthorization() {
        Selenide.open(LoginPage.URL, LoginPage.class)
                .setUsername("q")
                .setPassword("k")
                .submit(new LoginPage())
                .checkError(ERROR_MESSAGE);
    }

    @Test
    @DisplayName("Показать пароль")
    void openPassword() {
        String password = new Faker().code().ean8();

        Selenide.open(LoginPage.URL, LoginPage.class)
                .setPassword(password)
                .showPassword();
    }


    @User(
            categories = {
                    @Category(name = "cat_1", archived = false),
                    @Category(name = "cat_2", archived = true)
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
        Selenide.open(LoginPage.URL, LoginPage.class)
                .fillLoginPage(user.username(), user.testData().password())
                .submit(new MainPage())
                .checkThatPageLoaded();
    }
}
