package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.CollectionCondition.textsInAnyOrder;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@ParametersAreNonnullByDefault
public class LoginPage extends BasePage<LoginPage> {

    public static final String URL = CFG.authUrl() + "login";
    private final SelenideElement usernameInput = $("input[name='username']");
    private final SelenideElement passwordInput = $("input[name='password']");
    private final SelenideElement submitButton = $("button[type='submit']");
    private final SelenideElement registerButton = $(".form__register");
    private final ElementsCollection errorContainer = $$(".form__error");
    private final SelenideElement showPassword = $("button[class='form__password-button']");
    private final SelenideElement checkShowPassword = $(".form__password-button_active");

    @Step("Убедитесь, что страница авторизации загрузилась")
    @Override
    public LoginPage checkThatPageLoaded() {
        usernameInput.should(visible);
        passwordInput.should(visible);
        return this;
    }

    @Step("Заполнить форму авторизации данными: username: {0}, password: {1}")
    @Nonnull
    public LoginPage fillLoginPage(String username, String password) {
        setUsername(username);
        setPassword(password);
        return this;
    }

    @Step("Вводим имя: {0}")
    @Nonnull
    public LoginPage setUsername(String username) {
        usernameInput.setValue(username);
        return this;
    }

    @Step("Вводим пароль: {0}")
    @Nonnull
    public LoginPage setPassword(String password) {
        passwordInput.setValue(password);
        return this;
    }

    @Step("Нажать кнопку LogIn")
    @Nonnull
    public <T extends BasePage<?>> T submit(T expectedPage) {
        submitButton.click();
        return expectedPage;
    }

    @Step("Нажать кнопку создать новый аккаунт")
    @Nonnull
    public <T extends BasePage<?>> T clickRegisterPage(T expectedPage) {
        registerButton.click();
        return expectedPage;
    }

    @Step("Проверьте ошибку на странице: {error}")
    @Nonnull
    public LoginPage checkError(String... errors) {
        errorContainer.shouldHave(textsInAnyOrder(errors));
        return this;
    }

    @Step("Проверить отображение пароля")
    @Nonnull
    public LoginPage showPassword() {
        showPassword.click();
        checkShowPassword.shouldHave(visible);
        return this;
    }
}
