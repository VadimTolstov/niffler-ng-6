package guru.qa.niffler.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.component.SearchField;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;

import static com.codeborne.selenide.ClickOptions.usingJavaScript;
import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.CollectionCondition.textsInAnyOrder;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

public class FriendsPage extends BasePage<FriendsPage> {

    public static String URL = CFG.frontUrl() + "people/friends";
    private final SelenideElement peopleTab = $("a[href='/people/friends']");
    private final SelenideElement allTab = $("a[href='/people/all']");

    private final SearchField searchInput = new SearchField();
    private final SelenideElement popup = $("div[role='dialog']");

    private final SelenideElement requestsTable = $("#requests");
    private final SelenideElement friendsTable = $("#friends");
    private final SelenideElement pagePrevBtn = $("#page-prev");
    private final SelenideElement pageNextBtn = $("#page-next");

    @Step("Убедитесь, что страница с друзьями загрузилась")
    @Override
    @Nonnull
    public FriendsPage checkThatPageLoaded() {
        peopleTab.shouldBe(Condition.visible);
        allTab.shouldBe(Condition.visible);
        return this;
    }

    @Step("Убедитесь, что количество друзей равно {expectedCount}")
    @Nonnull
    public FriendsPage checkExistingFriendsCount(int expectedCount) {
        friendsTable.$$("tr").shouldHave(size(expectedCount));
        return this;
    }

    @Step("Убедитесь, что количество приглашений равно {expectedCount}")
    @Nonnull
    public FriendsPage checkExistingInvitationsCount(int expectedCount) {
        requestsTable.$$("tr").shouldHave(size(expectedCount));
        return this;
    }

    @Step("Убедитесь, что список друзей содержит {0}")
    @Nonnull
    public FriendsPage checkExistingFriends(String... expectedUsernames) {
        friendsTable.$$("tr").shouldHave(textsInAnyOrder(expectedUsernames));
        return this;
    }

    @Step("Убедитесь, что список приглашения содержит {0}")
    @Nonnull
    public FriendsPage checkExistingInvitations(String... expectedUsernames) {
        requestsTable.$$("tr").shouldHave(textsInAnyOrder(expectedUsernames));
        return this;
    }

    @Step("Удалить пользователя из списка друзей: {username}")
    @Nonnull
    public FriendsPage removeFriend(String username) {
        SelenideElement friendRow = friendsTable.$$("tr").find(text(username));
        friendRow.$("button[type='button']").click();
        popup.$(byText("Delete")).click(usingJavaScript());
        return this;
    }

    @Step("Принять приглашение от пользователя: {username}")
    @Nonnull
    public FriendsPage acceptFriendInvitationFromUser(String username) {
        SelenideElement friendRow = requestsTable.$$("tr").find(text(username));
        friendRow.$(byText("Accept")).click();
        return this;
    }

    @Step("Отклонить приглашение от пользователя: {username}")
    @Nonnull
    public FriendsPage declineFriendInvitationFromUser(String username) {
        SelenideElement friendRow = requestsTable.$$("tr").find(text(username));
        friendRow.$(byText("Decline")).click();
        popup.$(byText("Decline")).click(usingJavaScript());
        return this;
    }
}