package guru.qa.niffler.service;

import guru.qa.niffler.model.rest.UserJson;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
public interface UsersClient {
    @Nonnull
    UserJson createUser(String username, String password);

    List<UserJson> addIncomeInvitations(UserJson targetUser, int count);

    List<UserJson> addOutcomeInvitations(UserJson targetUser, int count);

    List<UserJson> addFriends(UserJson targetUser, int count);
}
