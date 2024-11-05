package guru.qa.niffler.service;

import guru.qa.niffler.api.UserDataApiClient;
import guru.qa.niffler.model.UserJson;

import java.util.ArrayList;
import java.util.List;

import static guru.qa.niffler.utils.RandomDataUtils.*;

public class UserRestApiClient implements UsersClient {
    private final UserDataApiClient userDataApiClient = new UserDataApiClient();

    @Override
    public UserJson createUser(String username, String password) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<UserJson> addIncomeInvitations(UserJson targetUser, int count) {
        List<UserJson> users = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            final String username = randomUsername();
            users.add(
                    userDataApiClient.sendInvitation(
                            createUser(username, "12345").username(),
                            targetUser.username()
                    )
            );
        }
        return users;
    }

    @Override
    public List<UserJson> addOutcomeInvitations(UserJson targetUser, int count) {
        List<UserJson> users = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            final String username = randomUsername();
            users.add(
                    userDataApiClient.sendInvitation(
                            targetUser.username(),
                            createUser(username, "12345").username()
                    )
            );
        }
        return users;
    }

    @Override
    public List<UserJson> addFriends(UserJson targetUser, int count) {
        List<UserJson> users = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            final String username = randomUsername();
            UserJson user = createUser(username, "12345");
            userDataApiClient.sendInvitation(
                    user.username(),
                    targetUser.username()
            );
            userDataApiClient.acceptInvitation(user.username(), targetUser.username());
            users.add(user);
        }
        return users;
    }
}
