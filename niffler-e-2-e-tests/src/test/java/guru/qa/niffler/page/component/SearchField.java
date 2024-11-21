package guru.qa.niffler.page.component;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.BasePage;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Condition.empty;
import static com.codeborne.selenide.Condition.not;
import static com.codeborne.selenide.Selenide.$;

import static com.codeborne.selenide.Selenide.$;

@ParametersAreNonnullByDefault
public class SearchField extends BaseComponent<SearchField> {
    public SearchField(@Nonnull SelenideElement self) {
        super(self);
    }

    public SearchField() {
        super($("input[aria-label='search']"));
    }

    private final SelenideElement clearSearchInputBtn = $("#input-clear");


    @Step("Поиск по запросу: {query}")
    @Nonnull
    public SearchField search(String query) {
        clearIfNotEmpty();
        self.setValue(query).pressEnter();
        return this;
    }

    @Step("Очистить поле поиска, если не пустое")
    @Nonnull
    public SearchField clearIfNotEmpty() {
        if (self.is(not(empty))) {
            clearSearchInputBtn.click();
            self.should(empty);
        }
        return this;
    }
}