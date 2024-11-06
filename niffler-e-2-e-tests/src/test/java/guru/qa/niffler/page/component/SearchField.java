package guru.qa.niffler.page.component;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.BasePage;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;

import static com.codeborne.selenide.Selenide.$;

@ParametersAreNonnullByDefault
public class SearchField <T extends BasePage<?>> extends BaseComponent<T> {
   public SearchField(SelenideElement searchFieldElement, T page) {
        super(searchFieldElement, page);
    }

    @Step("Поиск по запросу: {query}")
    public SearchField<T> search(String query) {
        self.setValue(query).pressEnter();
        return this;
    }

    @Step("Очистить поле поиска, если не пустое")
    public SearchField<T> clearIfNotEmpty() {
        if (!Objects.requireNonNull(self.getValue()).isEmpty()) {
            self.clear();
        }
        return this;
    }

}