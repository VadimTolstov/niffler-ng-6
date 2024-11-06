package guru.qa.niffler.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selectors;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.component.SearchField;
import io.qameta.allure.Step;
import lombok.Getter;
import org.openqa.selenium.By;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byTagAndText;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;

@ParametersAreNonnullByDefault
public class PeoplePage extends BasePage<PeoplePage> {

    private final SelenideElement friendsTable = $("#friends");
    private final SelenideElement tabPanelFriends = $("#simple-tabpanel-friends");
    private final SelenideElement friendTable = $("#requests");
    private final SelenideElement allPeopleTable = $("#all");
    private final SelenideElement acceptButton = $(byText("Accept"));
    private final SelenideElement declineButton = $(byText("Decline"));
    private final SelenideElement modalDialog = $("div[role='dialog']");

//    @Getter
    private final SearchField<PeoplePage> searchField = new SearchField<>($("input[placeholder='Search']"), this);

//    public void checkNotIncoming() {
//        incomeSpan.filterBy(Condition.text("Waiting...")).should(size(0));
//    }

    @Step("Проверить, что таблица друзей содержит пользователей: {names}")
    public void verifyFriendsTableContainsUser(List<String> names) {
        for (String name : names) {
            searchField.search(name);
            friendsTable.$$("tr").filter(text(name)).shouldHave(sizeGreaterThan(0));
        }
    }

    @Step("Проверить, что таблица друзей пуста")
    public void verifyFriendsTableShouldBeEmpty() {
        tabPanelFriends.shouldHave(text("There are no users yet"));
    }

    @Step("Проверить, что таблица друзей содержит входящие предложения: {names}")
    public void verifyFriendTableContainsIncome(List<String> names) {
        for (String name : names) {
            searchField.search(name);
            friendTable.$$("tr").filter(text(name)).shouldHave(sizeGreaterThan(0));
        }
    }

    @Step("Проверить, что таблица друзей содержит исходящие предложения: {names}")
    public void verifyAllPeopleTableContainsOutcome(List<String> names) {
        for (String name : names) {
            searchField.search(name);
            allPeopleTable.$$("tr").findBy(text(name)).should(text("Waiting..."));
        }
    }

    @Step("Подтвердить входящее предложение дружить: {name}")
    public PeoplePage acceptFriend(String name) {
        acceptButton.click();
        return this;
    }

    @Step("Проверить, что пользователь {name} добавлен в друзья")
    public void verifyFriendAdded(String name) {
        searchField.search(name);
        friendsTable.$$("tr").filter(text(name)).shouldHave(sizeGreaterThan(0));
    }

    @Step("Отклонить входящее предложение дружить: {name}")
    public PeoplePage declineFriend(String name) {
        declineButton.click();
        modalDialog.$(byTagAndText("button", "Decline")).click();
        return this;
    }
}
