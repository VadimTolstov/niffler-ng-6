package guru.qa.niffler.service;

import guru.qa.niffler.api.UserApiClient;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.utils.RandomDataUtils;

import java.util.ArrayList;
import java.util.List;

import static guru.qa.niffler.utils.RandomDataUtils.*;

public class UserRestApiClient implements UsersClient {
    private UserApiClient userApiClient = new UserApiClient();

    @Override
    public UserJson createUser(String username, String password) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<UserJson> createIncomeInvitations(UserJson targetUser, int count) {
        List<UserJson> users = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            final String username = randomUsername();
            users.add(
                    userApiClient.sendInvitation(
                            createUser(username, "12345").username(),
                            targetUser.username()
                    )
            );
        }
        return users;
    }

    @Override
    public List<UserJson> createOutcomeInvitations(UserJson targetUser, int count) {
        List<UserJson> users = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            final String username = randomUsername();
            users.add(
                    userApiClient.sendInvitation(
                            targetUser.username(),
                            createUser(username, "12345").username()
                    )
            );
        }
        return users;
    }

    @Override
    public List<UserJson> createFriends(UserJson targetUser, int count) {
        List<UserJson> users = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            final String username = randomUsername();
            UserJson user = createUser(username, "12345");
            userApiClient.sendInvitation(
                    user.username(),
                    targetUser.username()
            );
            userApiClient.acceptInvitation(user.username(), targetUser.username());
            users.add(user);
        }
        return users;
    }
}
