package guru.qa.niffler.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selectors;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import lombok.NonNull;
import org.openqa.selenium.By;

import javax.annotation.Nonnull;
import java.util.List;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

public class FriendsPage {

    private final SelenideElement friendsTable = $("#friends");
    private final By friendRequests = Selectors.byTagAndText("h2", "Friend requests");
    private final SelenideElement notFriends = $("img[alt='Lonely niffler']");
    private final SelenideElement tabAllPeople = $("a[href='/people/all']");
    private final SelenideElement self = $("input[placeholder='Search']");
    private final SelenideElement myFriends = $x("//h2[text()='My friends']");
    private final SelenideElement waiting = $x("//span[text()='Waiting...']");
    private final ElementsCollection allPeople = $$("#all tr");
    private final ElementsCollection incomeSpan = $$x("//tbody[@id='all']//span");
    private final ElementsCollection listFriends = $("#friends").$$("tr");
    private final ElementsCollection listFriendRequests = $("#simple-tabpanel-friends").$$("table");
    private final ElementsCollection std = $$x("//tbody[@id='all']/tr/td");

    public FriendsPage openTabAllePeople() {
        tabAllPeople.click();
        return this;
    }

    public FriendsPage checkNotFriends() {
        notFriends.shouldHave(visible);
        return this;
    }

    public void checkNotIncoming() {
        incomeSpan.filterBy(Condition.text("Waiting...")).should(size(0));
    }

    @Step("Поиск по запросу: {query}")
    public FriendsPage search(String query) {
        self.clear();
        self.setValue(query).pressEnter();
        return this;
    }

    @Step("Проверить, что таблица друзей содержит пользователей: {friends}")
    public FriendsPage checkFriendName(@Nonnull List<String> friends) {
        for (String friend : friends) {
            search(friend);
            listFriends.findBy(Condition.text(friend)).shouldHave(visible);
        }
        return this;
    }

    @Step("Проверить, что таблица всех людей содержит пользователей которым был отправлен запрос на дружбу: {friends}")
    public void checkIncomingFriend(@Nonnull List<String> users) {
        for (String user : users) {
            search(user);
            allPeople.find(text(user)).shouldHave(text("Waiting..."));
        }

    }
    @Step("Проверить, что таблица друзей содержит пользователей на добавления дружбы: {friends}")
    public FriendsPage checkFriendRequests(@Nonnull List<String> friends) {
        for (String friend : friends) {
            search(friend);
            listFriendRequests.findBy(Condition.text(friend)).shouldHave(visible);
        }
        return this;
    }
}
