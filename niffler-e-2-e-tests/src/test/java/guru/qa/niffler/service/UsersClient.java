package guru.qa.niffler.service;

import guru.qa.niffler.model.UserJson;

import java.util.List;

public interface UsersClient {
    UserJson createUser(String username, String password);

    List<UserJson> addIncomeInvitations(UserJson targetUser, int count);

    List<UserJson> addOutcomeInvitations(UserJson targetUser, int count);

    List<UserJson> addFriends(UserJson targetUser, int count);
}
