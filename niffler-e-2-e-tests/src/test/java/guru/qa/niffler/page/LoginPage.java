package guru.qa.niffler.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;

import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Selenide.$;

@ParametersAreNonnullByDefault
public class LoginPage extends BasePage<LoginPage> {

    public static final String URL = CFG.authUrl() + "login";
    private final SelenideElement usernameInput = $("input[name='username']");
    private final SelenideElement passwordInput = $("input[name='password']");
    private final SelenideElement submitButton = $("button[type='submit']");
    private final SelenideElement registerButton = $(".form__register");
    private final SelenideElement errorMessage = $(".form__error-container");
    private final SelenideElement showPassword = $("button[class='form__password-button']");
    private final SelenideElement checkShowPassword = $(".form__password-button_active");
    private final SelenideElement header = $(".header");
    private final SelenideElement brand = $(".logo-section__text");

    public MainPage login(String username, String password) {
        setUsername(username).setPassword(password).clickLogIn();
        return new MainPage();
    }

    public LoginPage setUsername(String username) {
        usernameInput.setValue(username);
        return this;
    }

    public LoginPage setPassword(String password) {
        passwordInput.setValue(password);
        return this;
    }

    public LoginPage clickLogIn() {
        submitButton.click();
        return this;
    }

    public RegisterPage clickRegisterPage() {
        registerButton.click();
        return new RegisterPage();
    }

    public LoginPage errorMessage(String value) {
        errorMessage.shouldHave(Condition.visible)
                .shouldHave(Condition.text(value));
        return this;
    }

    public LoginPage showPassword() {
        showPassword.click();
        checkShowPassword.shouldHave(Condition.visible);
        return this;
    }

    public LoginPage checkTextHeader(String value) {
        header.shouldHave(Condition.visible, Condition.text(value));
        return this;
    }

    public LoginPage checkTextBrand(String value) {
        brand.shouldHave(Condition.visible, Condition.text(value));
        return this;
    }

}
