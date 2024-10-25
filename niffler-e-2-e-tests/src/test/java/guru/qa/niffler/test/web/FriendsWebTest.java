package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.meta.User;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;

import static guru.qa.niffler.jupiter.extension.UsersQueueExtension.StaticUser;
import static guru.qa.niffler.jupiter.extension.UsersQueueExtension.Type.*;
import static guru.qa.niffler.jupiter.extension.UsersQueueExtension.UserType;

@WebTest
public class FriendsWebTest {

    private static final Config CFG = Config.getInstance();
    @User(friends = 1)
    @Test
    void friendShouldBePresentInFriendsTable(UserJson user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.testData().password())
                .clickPersonIcon()
                .openFriendPage()
                .checkFriendName(user.testData().friends());

    }

    @User()
    @Test
    void friendTableShouldBeEmptyForNewUser(UserJson user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.testData().password())
                .clickPersonIcon()
                .openFriendPage()
                .checkNotFriends()
                .openTabAllePeople()
                .checkNotIncoming();
    }

    @User(income = 1)
    @Test
    void incomeInvitationBePresentInFriendsTable(UserJson user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.testData().password())
                .clickPersonIcon()
                .openFriendPage()
                .checkNotFriends()
                .openTabAllePeople()
                .checkIncomingFriend(user.testData().income());
    }

    @User(outcome = 1)
    @Test
    void outcomeInvitationBePresentInAllPeoplesTable(UserJson user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.testData().password())
                .clickPersonIcon()
                .openFriendPage()
                .checkFriendRequests(user.testData().outcome());
    }
}
