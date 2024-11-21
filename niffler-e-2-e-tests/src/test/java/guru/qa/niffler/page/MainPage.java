package guru.qa.niffler.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.component.Header;
import guru.qa.niffler.page.component.SearchField;
import guru.qa.niffler.page.component.SpendingTable;
import guru.qa.niffler.page.component.StatComponent;
import io.qameta.allure.Step;
import lombok.Getter;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@ParametersAreNonnullByDefault
public class MainPage extends BasePage<MainPage> {
    public static final String URL = CFG.frontUrl() + "main";

    protected final Header header = new Header();
    protected final SpendingTable spendingTable = new SpendingTable();
    protected final StatComponent statComponent = new StatComponent();
//    private final SearchField searchField = new SearchField($("input[placeholder='Search']"));
//    private final SelenideElement spendings = $("#spendings");
//    private final SelenideElement statistics = $("#stat");

    @Nonnull
    public Header getHeader() {
        return header;
    }
    @Nonnull
    public StatComponent getStatComponent() {
        statComponent.getSelf().scrollIntoView(true);
        return statComponent;
    }

    @Nonnull
    public SpendingTable getSpendingTable() {
        spendingTable.getSelf().scrollIntoView(true);
        return spendingTable;
    }

//    @Step("Редактировать трату: {spendingDescription}")
//    public EditSpendingPage editSpending(String spendingDescription) {
//        searchField.search(spendingDescription);
//        spendingTable.editSpending(spendingDescription);
//        return new EditSpendingPage();
//    }
//
//    @Step("Проверить, что компоненты главной страницы видны")
//    public void verifyMainComponentsIsVisible() {
//        statistics.shouldBe(visible);
//        spendingTable.titleIsVisible();
//    }
//
//    @Step("Проверить, что таблица трат содержит: {spendingDescription}")
//    public void checkThatTableContainsSpending(String... spendingDescription) {
//        for (String spend : spendingDescription) {
//            searchField.search(spend);
//            spendingTable.checkTableContains(spend);
//            searchField.clearIfNotEmpty();
//        }
//    }
//
//    @Step("Открыть страницу всех людей")
//    public PeoplePage openAllPeople() {
//        header.toAllPeoplePage();
//        return new PeoplePage();
//    }
//
//    @Step("Открыть страницу профиля")
//    public ProfilePage openProfilePage() {
//        header.toProfilePage();
//        return new ProfilePage();
//    }
//
//    @Step("Открыть страницу друзей")
//    public PeoplePage openFriendPage() {
//        header.toFriendsPage();
//        return new PeoplePage();
//    }

    @Override
    @Nonnull
    @Step("Убедиться, что главная страница загрузилась")
    public MainPage checkThatPageLoaded() {
        header.getSelf().should(visible).shouldHave(text("Niffler"));
        statComponent.getSelf().should(visible).shouldHave(text("Statistics"));
        spendingTable.getSelf().should(visible).shouldHave(text("History of Spendings"));
        return this;
    }
}
