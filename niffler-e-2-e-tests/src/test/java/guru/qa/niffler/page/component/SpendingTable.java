package guru.qa.niffler.page.component;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.BasePage;
import guru.qa.niffler.page.EditSpendingPage;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byTagAndText;
import static com.codeborne.selenide.Selenide.*;

@ParametersAreNonnullByDefault
public class SpendingTable <T extends BasePage<?>> extends BaseComponent<T> {

    private final ElementsCollection spendingRows = self.$$("tbody tr");
    private final SelenideElement modalDialog = $("div[role='dialog']");

    public SpendingTable(SelenideElement spends, T page) {
        super(spends, page);
    }

    @Step("Выбрать период для отображения трат: {period}")
    public SpendingTable<T> selectPeriod(DataFilterValues period) {
        self.$("#period").click();
        $("li[data-value='" + period.name() + "']").click();
        return this;
    }

    @Step("Редактировать трату: {description}")
    public EditSpendingPage editSpending(String description) {
        spendingRows.find(text(description)).$("button[aria-label='Edit spending']").click();
        return new EditSpendingPage();
    }

    @Step("Удалить трату: {description}")
    public SpendingTable<T> deleteSpending(String description) {
        spendingRows.find(text(description)).$("input[type='checkbox']").click();
        self.$("#delete").click();
        modalDialog.$(byTagAndText("button", "Delete")).click();
        return this;
    }

    @Step("Поиск траты: {description}")
    public SpendingTable<T> searchSpendingByDescription(String description) {
        spendingRows.find(text(description)).shouldBe(visible);
        return this;
    }

    @Step("Проверить, что таблица содержит траты: {expectedSpends}")
    public SpendingTable<T> checkTableContains( String... expectedSpends) {
        for (String spend : expectedSpends) {
            spendingRows.find(text(spend)).shouldBe(visible);
        }
        return this;
    }

    @Step("Проверить количество трат: {expectedSize}")
    public SpendingTable<T> checkTableSize(int expectedSize) {
        spendingRows.shouldHave(size(expectedSize));
        return this;
    }

    @Step("Проверить, что заголовок таблицы виден")
    public void titleIsVisible() {
        $("#spendings h2").shouldHave(text("History of Spendings"));
    }
}
