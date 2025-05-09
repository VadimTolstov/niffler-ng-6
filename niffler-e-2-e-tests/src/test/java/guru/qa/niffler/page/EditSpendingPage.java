package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.component.Calendar;
import io.qameta.allure.Step;
import lombok.Getter;

import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Selenide.$;

@ParametersAreNonnullByDefault
public class EditSpendingPage extends BasePage<EditSpendingPage> {
    @Getter
    private final Calendar<EditSpendingPage> calendar = new Calendar<>($("input[name='date']"), this);
    private final SelenideElement descriptionInput = $("#description");
    private final SelenideElement saveBtn = $("#save");
    private final SelenideElement amountInput = $("#amount");
    private final SelenideElement categoryInput = $("#category");

    @Step("Ввести описание траты: {description}")
    public EditSpendingPage setNewSpendingDescription(String description) {
        descriptionInput.setValue(description);
        return this;
    }

    @Step("Сохранить изменения")
    public EditSpendingPage save() {
        saveBtn.click();
        return this;
    }

    @Step("Ввести название категории: {category}")
    public EditSpendingPage setSpendingCategory(String category) {
        categoryInput.setValue(category);
        return this;
    }

    @Step("Ввести сумму траты: {amount}")
    public EditSpendingPage setSpendingAmount(String amount) {
        amountInput.setValue(amount);
        return this;
    }
}
