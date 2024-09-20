package guru.qa.niffler.page;

import com.codeborne.selenide.*;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FriendsPage {
    private final By friendRequests = Selectors.byTagAndText("h2", "Friend requests");
    // $x("//h2[text()='Friend requests']"),
    private final SelenideElement notFriends = $("img[alt='Lonely niffler']");
    private final SelenideElement tabAllPeople = $("a[href='/people/all']");
    private final SelenideElement myFriends = $x("//h2[text()='My friends']");
    private final SelenideElement waiting = $x("//span[text()='Waiting...']");
    //    private final ElementsCollection allPeople = $("#all").$$("tr");
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
        incomeSpan.findBy(Condition.not(Condition.text("Waiting..."))).shouldHave(visible);
    }

    public FriendsPage checkFriendsName(String friends) {
        listFriends.get(0).shouldBe(visible).shouldHave(text(friends));
        return this;
    }

    public void checkIncomingFriends(String username) {
        allPeople.find(text(username)).shouldHave(text("Waiting..."));
    }
    public FriendsPage checkFriendRequests(String friends) {
        listFriendRequests.get(0).shouldBe(visible).shouldHave(text(friends));
        return this;
    }
}
