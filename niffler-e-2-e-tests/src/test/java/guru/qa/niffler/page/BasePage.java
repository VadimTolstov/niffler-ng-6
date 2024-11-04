package guru.qa.niffler.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;

public abstract class BasePage<T extends BasePage<?>> {

    protected final SelenideElement alert = $("");

    @SuppressWarnings("unchecked")
    protected T checkAlert(String message) {
        alert.shouldHave(Condition.text(message));
        return (T) this;
    }

    public abstract T checkPageLoaded();
}
