package guru.qa.niffler.test.rest;

import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.Token;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.meta.RestTest;
import guru.qa.niffler.jupiter.extension.ApiLoginExtension;
import guru.qa.niffler.model.rest.FriendJson;
import guru.qa.niffler.model.rest.FriendState;
import guru.qa.niffler.model.rest.UserJson;
import guru.qa.niffler.service.impl.GatewayApiClient;
import guru.qa.niffler.service.impl.GatewayApiV2Client;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.data.domain.PageImpl;

import java.util.Comparator;
import java.util.List;

@RestTest
public class FriendsV2Test {

    @RegisterExtension
    private static final ApiLoginExtension apiLoginExtension = ApiLoginExtension.rest();
    private final GatewayApiV2Client gatewayApiV2Client = new GatewayApiV2Client();
    private final GatewayApiClient gatewayApiClient = new GatewayApiClient();

    @User(friends = 2, income = 1)
    @ApiLogin
    @Test
    void allFriendsAndIncomeInvitationsShouldBeReturnedForUser(UserJson user, @Token String token) {

        final List<UserJson> expectedFriends = user.testData().friends()
                .stream()
                .sorted(Comparator.comparing(UserJson::username))
                .toList();

        final List<UserJson> expectedInvitations = user.testData().income()
                .stream()
                .sorted((u1, u2) -> u1.username().compareTo(u2.username()))
                .toList();

        final PageImpl<UserJson> result = gatewayApiV2Client.allFriends(
                token,
                null,
                0,
                null,
                "username,ASC"
        );

        Assertions.assertNotNull(result);
        Assertions.assertEquals(3, result.getContent().size());

        final List<UserJson> friendsFromResponse = result.stream()
                .filter(u -> u.friendState() == FriendState.FRIEND)
                .toList();

        final List<UserJson> invitationsFromResponse = result.stream()
                .filter(u -> u.friendState() == FriendState.INVITE_RECEIVED)
                .toList();

        Assertions.assertEquals(2, friendsFromResponse.size());
        Assertions.assertEquals(1, invitationsFromResponse.size());

        Assertions.assertEquals(
                expectedInvitations.getFirst().username(),
                invitationsFromResponse.getFirst().username()
        );

        Assertions.assertEquals(
                expectedInvitations.getFirst().username(),
                invitationsFromResponse.getFirst().username()
        );

        final UserJson firstUserFromRequest = friendsFromResponse.getFirst();
        final UserJson secondUserFromRequest = friendsFromResponse.getLast();

        Assertions.assertEquals(
                expectedFriends.getFirst().username(),
                firstUserFromRequest.username()
        );

        Assertions.assertEquals(
                expectedFriends.getLast().username(),
                secondUserFromRequest.username()
        );
    }


    @User(friends = 1)
    @ApiLogin
    @Test
    void friendShouldBeDeleted(@Token String token) {
        final PageImpl<UserJson> friendsBefore = gatewayApiV2Client.allFriends(
                token,
                null,
                null,
                null,
                null
        );

        Assertions.assertEquals(1, friendsBefore.getContent().size());
        final List<UserJson> friendToDelete = friendsBefore.stream().toList();

        gatewayApiClient.removeFriend(token, friendToDelete.get(0).username());

        final PageImpl<UserJson> friendsAfter = gatewayApiV2Client.allFriends(
                token,
                null,
                null,
                null,
                null
        );
        Assertions.assertTrue(friendsAfter.isEmpty());
    }

    @User(income = 1)
    @ApiLogin
    @Test
    void invitationShouldBeAccepted(@Token String token) {
        final PageImpl<UserJson> invitationsBefore = gatewayApiV2Client.allFriends(
                token,
                null,
                null,
                null,
                null
        );

        Assertions.assertEquals(1, invitationsBefore.getContent().size());
        final List<UserJson> invitation = invitationsBefore.stream().toList();

        gatewayApiClient.acceptInvitation(token, new FriendJson(invitation.get(0).username()));

        final PageImpl<UserJson> friendsAfter = gatewayApiV2Client.allFriends(
                token,
                null,
                null,
                null,
                null
        );
        Assertions.assertEquals(1, friendsAfter.getContent().size());
        final List<UserJson> friends = friendsAfter.stream().toList();
        Assertions.assertEquals(FriendState.FRIEND, friends.getFirst().friendState());
    }

    @User(income = 1)
    @ApiLogin
    @Test
    void invitationShouldBeDeclined(@Token String token) {
        final PageImpl<UserJson> invitationsBefore = gatewayApiV2Client.allFriends(
                token,
                null,
                null,
                null,
                null
        );
        Assertions.assertEquals(1, invitationsBefore.getContent().size());
        final List<UserJson> invitation = invitationsBefore.stream().toList();

        gatewayApiClient.declineInvitation(token, new FriendJson(invitation.get(0).username()));

        final PageImpl<UserJson> invitationsAfter = gatewayApiV2Client.allFriends(
                token,
                null,
                null,
                null,
                null
        );
        Assertions.assertTrue(invitationsAfter.isEmpty());
    }

    @User(outcome = 1)
    @ApiLogin
    @Test
    void outgoingInvitationShouldBeCreated(UserJson user, @Token String token) {
        final String outcomeInvitationUsername = user.testData().outcomeInvitationsUsernames()[0];
        final FriendJson newFriend = new FriendJson(outcomeInvitationUsername);

        gatewayApiClient.sendInvitation(token, newFriend);

        final List<UserJson> outgoingInvitations = gatewayApiV2Client.allUsers(
                        token,
                        null,
                        null,
                        null,
                        null
                ).stream()
                .filter(u -> u.friendState() == FriendState.INVITE_SENT)
                .toList();

        Assertions.assertEquals(1, outgoingInvitations.size());
        Assertions.assertEquals(newFriend.username(), outgoingInvitations.getFirst().username());
    }
}

