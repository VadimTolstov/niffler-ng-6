package guru.qa.niffler.page;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.component.Header;
import io.qameta.allure.Step;
import lombok.Getter;
import guru.qa.niffler.config.Config;


import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

@ParametersAreNonnullByDefault
public abstract class BasePage<T extends BasePage<?>> {
    protected static final Config CFG = Config.getInstance();
    private final SelenideElement alert = $(".MuiSnackbar-root");
    private final ElementsCollection formErrors = $$("p.Mui-error, .input__helper-text");

    public abstract T checkThatPageLoaded();


    @SuppressWarnings("unchecked")
    @Step("Проверка успешного всплывающего сообщения: {message}")
    @Nonnull
    public T checkAlertMessage(String message) {
        alert.shouldHave(text(message)).shouldHave(visible);
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    @Step("Проверка всплывающего сообщения с ошибкой: {message}")
    @Nonnull
    public T checkFormErrorMessage(String... expectedText) {
        formErrors.should(CollectionCondition.textsInAnyOrder(expectedText));
        return (T) this;
    }
}
