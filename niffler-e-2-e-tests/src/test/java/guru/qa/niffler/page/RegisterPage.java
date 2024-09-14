package guru.qa.niffler.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.*;

public class RegisterPage {
    private final SelenideElement
            usernameInput = $("input[name='username']"),
            passwordInput = $("input[name='password']"),
            passwordSubmitInput = $("input[name='passwordSubmit']"),
            submitButton = $("button[type='submit']"),
            hrefLogIn = $("a[class='form__link']"),
            successRegisterMessage = $(".form__paragraph"),
            signInHref = $(".form__submit"),
            header = $(".header"),
            brand = $(".logo-section__text");

    private final ElementsCollection showPassword = $$("button[class='form__password-button']");
    private final ElementsCollection checkShowPassword = $$(".form__password-button_active");

    public RegisterPage setUsername(String username) {
        usernameInput.setValue(username);
        return this;
    }

    public RegisterPage setPassword(String password) {
        passwordInput.setValue(password);
        return this;
    }

    public RegisterPage setPasswordSubmit(String passwordSubmit) {
        passwordSubmitInput.setValue(passwordSubmit);
        return this;
    }

    public RegisterPage submitRegistration() {
        submitButton.click();
        return this;
    }

    public LoginPage openLogIn() {
        hrefLogIn.click();
        return new LoginPage();
    }

    public RegisterPage shouldErrorMessage(String value) {
        $x("//span[contains(text(),'" + value + "')]")
                .shouldHave(Condition.visible)
                .shouldHave(Condition.text(value));
        return this;
    }

    public RegisterPage shouldSuccessRegister(String value) {
        successRegisterMessage.shouldHave(Condition.visible)
                .shouldHave(Condition.text(value));
        return this;
    }

    public RegisterPage SignInAuthorizationPage() {
        signInHref.click();
        return this;
    }

    public RegisterPage checkTextHeader(String value) {
        header.shouldHave(Condition.visible, Condition.text(value));
        return this;
    }

    public RegisterPage checkTextBrand(String value) {
        brand.shouldHave(Condition.visible, Condition.text(value));
        return this;
    }

    public RegisterPage showPassword() {
        showPassword.forEach(SelenideElement::click);
        checkShowPassword.forEach(element -> element.shouldHave(Condition.visible));
        return this;
    }
}
