package guru.qa.niffler.page.component;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.condition.SpendConditions;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.page.BasePage;
import guru.qa.niffler.page.EditSpendingPage;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.ClickOptions.usingJavaScript;
import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byTagAndText;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;

@ParametersAreNonnullByDefault
public class SpendingTable extends BaseComponent<SpendingTable> {
    private final SearchField searchField = new SearchField();
    private final SelenideElement periodMenu = self.$("#period");
    private final SelenideElement currencyMenu = self.$("#currency");
    private final ElementsCollection menuItems = $$(".MuiList-padding li");
    private final SelenideElement deleteBtn = self.$("#delete");
    private final SelenideElement popup = $("div[role='dialog']");

    private final SelenideElement tableHeader = self.$(".MuiTableHead-root");
    private final ElementsCollection headerCells = tableHeader.$$(".MuiTableCell-root");

    private final ElementsCollection tableRows = self.$("tbody").$$("tr");

    public SpendingTable() {
        super($("#spendings"));
    }

    @Step("Выбрать период для отображения трат: {0}")
    @Nonnull
    public SpendingTable selectPeriod(DataFilterValues period) {
        periodMenu.click();
        menuItems.find(text(period.text)).click();
        return this;
    }

    @Step("Редактировать трату: {description}")
    @Nonnull
    public EditSpendingPage editSpending(String description) {
        searchSpendingByDescription(description);
        SelenideElement row = tableRows.find(text(description));
        row.$$("td").get(5).click();
        return new EditSpendingPage();
    }

    @Step("Удалить трату: {description}")
    @Nonnull
    public SpendingTable deleteSpending(String description) {
        searchSpendingByDescription(description);
        SelenideElement row = tableRows.find(text(description));
        row.$$("td").get(0).click();
        deleteBtn.click();
        popup.$(byText("Delete")).click(usingJavaScript());
        return this;
    }

    @Step("Поиск траты: {description}")
    @Nonnull
    public SpendingTable searchSpendingByDescription(String description) {
        searchField.search(description);
        return this;
    }

    @Step("Проверить, что таблица содержит траты: {expectedSpends}")
    @Nonnull
    public SpendingTable checkTableContains(String... expectedSpends) {
        for (String spend : expectedSpends) {
            searchSpendingByDescription(spend);
            tableRows.find(text(spend)).shouldBe(visible);
        }
        return this;
    }

    @Step("Проверить количество трат: {expectedSize}")
    @Nonnull
    public SpendingTable checkTableSize(int expectedSize) {
        tableRows.shouldHave(size(expectedSize));
        return this;
    }

    @Step("Проверить, что заголовок таблицы виден")
    @Nonnull
    public void titleIsVisible() {
        self.$("h2").shouldHave(text("History of Spendings"));
    }

    @Step("Убедитесь, что таблица расходов содержит {expectedSpends}")
    @Nonnull
    public SpendingTable checkSpends(SpendJson... expectedSpends) {
        tableRows.should(SpendConditions.spends(expectedSpends));
        return this;
    }
}
