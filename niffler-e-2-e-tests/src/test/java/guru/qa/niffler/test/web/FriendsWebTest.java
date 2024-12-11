package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
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

    @User(friends = 1)
    @Test
    void friendShouldBePresentInFriendsTable(UserJson user) {
        final String friendUsername = user.testData().friendsUsernames()[0];

        Selenide.open(LoginPage.URL, LoginPage.class)
                .fillLoginPage(user.username(), user.testData().password())
                .submit(new MainPage())
                .getHeader()
                .toFriendsPage()
                .checkExistingFriends(friendUsername);

    }

    @User()
    @Test
    void friendTableShouldBeEmptyForNewUser(UserJson user) {
        Selenide.open(LoginPage.URL, LoginPage.class)
                .fillLoginPage(user.username(), user.testData().password())
                .submit(new MainPage())
                .getHeader()
                .toFriendsPage()
                .checkExistingFriendsCount(0);
    }

    @User(income = 1)
    @Test
    void incomeInvitationBePresentInFriendsTable(UserJson user) {
        final String income = user.testData().incomeInvitationsUsernames()[0];

        Selenide.open(LoginPage.URL, LoginPage.class)
                .fillLoginPage(user.username(), user.testData().password())
                .submit(new MainPage())
                .getHeader()
                .toAllPeoplesPage()
                .checkExistingUser(income);
    }

    @User(
            outcome = 1
    )
    @Test
    void outcomeInvitationBePresentInAllPeoplesTable(UserJson user) {
        final String outcome = user.testData().outcomeInvitationsUsernames()[0];

        Selenide.open(LoginPage.URL, LoginPage.class)
                .fillLoginPage(user.username(), user.testData().password())
                .submit(new MainPage())
                .getHeader()
                .toAllPeoplesPage()
                .checkInvitationSentToUser(outcome);
    }

    @User(
            income = 1
    )
    @Test
    void shouldAcceptFriendRequest(UserJson user) {
        final String income = user.testData().incomeInvitationsUsernames()[0];

        Selenide.open(LoginPage.URL, LoginPage.class)
                .fillLoginPage(user.username(), user.testData().password())
                .submit(new MainPage())
                .getHeader()
                .toFriendsPage()
                .acceptFriendInvitationFromUser(income)
                .checkExistingFriendsCount(1);
    }

    @User(
            income = 1
    )
    @Test
    void shouldDeclineFriendRequest(UserJson user) {
        final String income = user.testData().incomeInvitationsUsernames()[0];

        Selenide.open(LoginPage.URL, LoginPage.class)
                .fillLoginPage(user.username(), user.testData().password())
                .submit(new MainPage())
                .getHeader()
                .toFriendsPage()
                .declineFriendInvitationFromUser(income)
                .checkExistingFriendsCount(0);
    }
}