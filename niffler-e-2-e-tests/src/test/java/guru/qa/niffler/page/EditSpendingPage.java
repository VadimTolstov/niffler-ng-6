package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.data.entity.userdata.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.page.component.Calendar;
import guru.qa.niffler.page.component.SelectField;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import java.util.Date;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@ParametersAreNonnullByDefault
public class EditSpendingPage extends BasePage<EditSpendingPage> {
    public static final String URL = CFG.frontUrl() + "spending";

    private final Calendar calendar = new Calendar();
    private final SelectField currencySelect = new SelectField($("#currency"));

    private final SelenideElement amountInput = $("#amount");
    private final SelenideElement categoryInput = $("#category");
    private final ElementsCollection categories = $$(".MuiChip-root");
    private final SelenideElement descriptionInput = $("#description");
    private final SelenideElement cancelBtn = $("#cancel");
    private final SelenideElement saveBtn = $("#save");

    @Override
    @Nonnull
    public EditSpendingPage checkThatPageLoaded() {
        amountInput.should(visible);
        return this;
    }

    @Step("Заполните данные о расходах из объекта")
    @Nonnull
    public EditSpendingPage fillPage(SpendJson spend) {
        return setNewSpendingDate(spend.spendDate())
                .setNewSpendingAmount(spend.amount())
                .setNewSpendingCurrency(spend.currency())
                .setNewSpendingCategory(spend.category().name())
                .setNewSpendingDescription(spend.description());
    }

    @Step("Ввести описание траты: {description}")
    @Nonnull
    public EditSpendingPage setNewSpendingDescription(String description) {
        descriptionInput.setValue(description);
        return this;
    }

    @Step("Выбрать валюту расходов: {0}")
    @Nonnull
    public EditSpendingPage setNewSpendingCurrency(CurrencyValues currency) {
        currencySelect.setValue(currency.name());
        return this;
    }

    @Step("Сохранить новые траты")
    @Nonnull
    public EditSpendingPage saveSpending() {
        saveBtn.click();
        return this;
    }

    @Step("Ввести название категории: {category}")
    @Nonnull
    public EditSpendingPage setNewSpendingCategory(String category) {
        categoryInput.setValue(category);
        return this;
    }

    @Step("Ввести сумму траты: {amount}")
    @Nonnull
    public EditSpendingPage setSpendingAmount(String amount) {
        amountInput.setValue(amount);
        return this;
    }

    @Step("Ввести сумму траты: {0}")
    @Nonnull
    public EditSpendingPage setNewSpendingAmount(double amount) {
        amountInput.clear();
        amountInput.setValue(String.valueOf(amount));
        return this;
    }

    @Step("вести сумму траты:: {0}")
    @Nonnull
    public EditSpendingPage setNewSpendingAmount(int amount) {
        amountInput.clear();
        amountInput.setValue(String.valueOf(amount));
        return this;
    }

    @Step("Ввести дату трат: {0}")
    @Nonnull
    public EditSpendingPage setNewSpendingDate(Date date) {
        calendar.selectDateInCalendar(date);
        return this;
    }
}
