package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

@ParametersAreNonnullByDefault
public class RegisterPage extends BasePage<RegisterPage> {

    public static final String URL = CFG.authUrl() + "register";

    private final SelenideElement usernameInput = $("input[name='username']");
    private final SelenideElement passwordInput = $("input[name='password']");
    private final SelenideElement passwordSubmitInput = $("input[name='passwordSubmit']");
    private final SelenideElement submitButton = $("button[type='submit']");
    private final SelenideElement proceedLoginButton = $(".form_sign-in");
    private final SelenideElement errorContainer = $(".form__error");

    @Step("Заполните страницу регистрации учетными данными: username: {0}, password: {1}, submit password: {2}")
    @Nonnull
    public RegisterPage fillRegisterPage(String login, String password, String passwordSubmit) {
        setUsername(login);
        setPassword(password);
        setPasswordSubmit(passwordSubmit);
        return this;
    }

    @Step("Ввести username: {0}")
    @Nonnull
    public RegisterPage setUsername(String username) {
        usernameInput.setValue(username);
        return this;
    }

    @Step("Ввести password: {0}")
    @Nonnull
    public RegisterPage setPassword(String password) {
        passwordInput.setValue(password);
        return this;
    }

    @Step("Подтвердить password: {0}")
    @Nonnull
    public RegisterPage setPasswordSubmit(String password) {
        passwordSubmitInput.setValue(password);
        return this;
    }

    @Step("Отправить заявку на регистрацию и перейти на страницу авторизации")
    @Nonnull
    public LoginPage successSubmit() {
        submitButton.click();
        proceedLoginButton.click();
        return new LoginPage();
    }

    @Step("Нажать кнопку Sing Up")
    @Nonnull
    public LoginPage clickSubmit() {
        submitButton.click();
        return new LoginPage();
    }

    @Step("Нажать на кнопку регистрации")
    @Nonnull
    public RegisterPage errorSubmit() {
        submitButton.click();
        return this;
    }

    @Step("Проверить, что страница регистрации загрузилась")
    @Override
    @Nonnull
    public RegisterPage checkThatPageLoaded() {
        usernameInput.should(visible);
        passwordInput.should(visible);
        passwordSubmitInput.should(visible);
        return this;
    }

    @Step("Проверить, что ошибка содержит текст: {errorMessage}")
    @Nonnull
    public RegisterPage checkAlertMessage(String errorMessage) {
        errorContainer.shouldHave(text(errorMessage));
        return this;
    }
}
