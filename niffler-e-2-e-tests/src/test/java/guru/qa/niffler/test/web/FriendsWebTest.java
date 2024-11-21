package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.meta.User;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.MainPage;
import org.junit.jupiter.api.Test;

import javax.annotation.ParametersAreNonnullByDefault;

@WebTest
@ParametersAreNonnullByDefault
public class FriendsWebTest {

    private static final Config CFG = Config.getInstance();

    @User(friends = 1)
    @Test
    void friendShouldBePresentInFriendsTable(UserJson user) {
        final String friendUsername = user.testData().friendsUsernames()[0];

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .fillLoginPage(user.username(), user.testData().password())
                .submit(new MainPage())
                .getHeader()
                .toFriendsPage()
                .checkExistingFriends(friendUsername);

    }

    @User()
    @Test
    void friendTableShouldBeEmptyForNewUser(UserJson user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .fillLoginPage(user.username(), user.testData().password())
                .openFriendPage()
                .verifyFriendsTableShouldBeEmpty();
    }

    @User(income = 1)
    @Test
    void incomeInvitationBePresentInFriendsTable(UserJson user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .fillLoginPage(user.username(), user.testData().password())
                .openFriendPage()
                .verifyFriendTableContainsIncome(user.testData().income());
    }

    @User(
            income = 1
    )
    @Test
    void outcomeInvitationBePresentInAllPeoplesTable(UserJson user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .fillLoginPage(user.username(), user.testData().password())
                .openAllPeople()
                .verifyAllPeopleTableContainsOutcome(user.testData().outcome());
    }

    @User(
            income = 1
    )
    @Test
    void shouldAcceptFriendRequest(UserJson user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .fillLoginPage(user.username(), user.testData().password())
                .openFriendPage()
                .acceptFriend(user.username())
                .verifyFriendAdded(user.testData().income().getFirst());
    }

    @User(
            income = 1
    )
    @Test
    void shouldDeclineFriendRequest(UserJson user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .fillLoginPage(user.username(), user.testData().password())
                .openFriendPage()
                .declineFriend(user.username())
                .verifyFriendsTableShouldBeEmpty();
    }
}