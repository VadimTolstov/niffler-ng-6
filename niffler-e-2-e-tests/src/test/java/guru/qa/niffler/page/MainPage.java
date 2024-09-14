package guru.qa.niffler.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class MainPage {
    private final static String HEADER = "History of Spendings";

    private final ElementsCollection tableRows = $("#spendings tbody ").$$("tr");
    private final ElementsCollection listCheckbox = $$("[type='checkbox']");

    private final SelenideElement
            buttonAddNewSpendingPage = $("a[href='spending'"),
            header = $("#spendings h2"),
            search = $("input[placeholder='Search']"),
            selectPeriod = $("#period"),
            selectCurrency = $("#currency"),
            buttonDelete = $("#delete"),
            buttonNext = $("#page-next"),
            buttonPrevious = $("#page-prev"),
            personIcon = $("svg[data-testid='PersonIcon']"),
            openProfilePage = $("a[href='/profile']");

    public EditSpendingPage editSpending(String spendingDescription) {
        tableRows.find(text(spendingDescription)).$$("td").get(5).click();
        return new EditSpendingPage();
    }

    public void checkThatTableContainsSpending(String spendingDescription) {
        tableRows.find(text(spendingDescription)).should(visible);
    }

    public MainPage checkingHeader() {
        header.shouldHave(visible, text(HEADER));
        return this;
    }

    public MainPage clickPersonIcon() {
        personIcon.click();
        return this;
    }

    public ProfilePage openProfilePage() {
        openProfilePage.click();
        return new ProfilePage();
    }
}
