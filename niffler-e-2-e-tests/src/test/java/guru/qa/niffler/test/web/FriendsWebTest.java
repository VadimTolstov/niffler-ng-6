package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.meta.User;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.FriendsPage;
import guru.qa.niffler.page.PeoplePage;
import org.junit.jupiter.api.Test;

import javax.annotation.ParametersAreNonnullByDefault;

@WebTest
@ParametersAreNonnullByDefault
public class FriendsWebTest {
    @ApiLogin
    @User(friends = 1)
    @Test
    void friendShouldBePresentInFriendsTable(UserJson user) {
        final String friendUsername = user.testData().friendsUsernames()[0];
        Selenide.open(FriendsPage.URL, FriendsPage.class)
                .checkExistingFriends(friendUsername);

    }

    @User()
    @ApiLogin
    @Test
    void friendTableShouldBeEmptyForNewUser(UserJson user) {
        Selenide.open(FriendsPage.URL, FriendsPage.class)
                .checkExistingFriendsCount(0);
    }

    @User(income = 1)
    @ApiLogin
    @Test
    void incomeInvitationBePresentInFriendsTable(UserJson user) {
        final String income = user.testData().incomeInvitationsUsernames()[0];

        Selenide.open(PeoplePage.URL, PeoplePage.class)
                .checkExistingUser(income);
    }

    @User(
            income = 1
    )
    @ApiLogin
    @Test
    void outcomeInvitationBePresentInAllPeoplesTable(UserJson user) {
        final String outcome = user.testData().incomeInvitationsUsernames()[0];

        Selenide.open(PeoplePage.URL, PeoplePage.class)
                .checkInvitationSentToUser(outcome);
    }

    @User(
            outcome = 1
    )
    @ApiLogin
    @Test
    void shouldAcceptFriendRequest(UserJson user) {
        final String income = user.testData().outcomeInvitationsUsernames()[0];

        Selenide.open(FriendsPage.URL, FriendsPage.class)
                .acceptFriendInvitationFromUser(income)
                .checkExistingFriendsCount(1);
    }

    @User(
            outcome = 1
    )
    @ApiLogin
    @Test
    void shouldDeclineFriendRequest(UserJson user) {
        final String income = user.testData().outcomeInvitationsUsernames()[0];

        Selenide.open(FriendsPage.URL, FriendsPage.class)
                .declineFriendInvitationFromUser(income)
                .checkExistingFriendsCount(0);
    }
}