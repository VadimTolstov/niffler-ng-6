package guru.qa.niffler.service.impl;

import guru.qa.niffler.api.GatewayApi;
import guru.qa.niffler.api.core.RestClient;
import guru.qa.niffler.model.rest.FriendJson;
import guru.qa.niffler.model.rest.UserJson;
import io.qameta.allure.Step;
import org.apache.hc.core5.http.HttpStatus;
import org.junit.jupiter.api.Assertions;
import retrofit2.Response;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class GatewayApiClient extends RestClient {

    private final GatewayApi gatewayApi;

    public GatewayApiClient() {
        super(CFG.gatewayUrl());
        this.gatewayApi = retrofit.create(GatewayApi.class);
    }

    @Nonnull
    @Step("send /api/friends/all GET request to niffler-gateway")
    public  List<UserJson> allFriends(@Nonnull String bearerToken, @Nullable String searchQuery) {
        final Response<List<UserJson>> response;
        try {
            response = gatewayApi.allFriends(bearerToken, searchQuery).execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        Assertions.assertEquals(HttpStatus.SC_OK, response.code());
        return response.body() != null
                ? response.body()
                : Collections.emptyList();
    }

    @Step("send /api/friends/remove DELETE request to niffler-gateway")
    public void removeFriend(@Nonnull String bearerToken, @Nullable String targetUsername) {
        final Response<Void> response;
        try {
            response = gatewayApi.removeFriend(bearerToken, targetUsername).execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        Assertions.assertEquals(HttpStatus.SC_OK, response.code());
    }

    @Step("send /api/invitations/send POST request to niffler-gateway")
    public UserJson sendInvitation(@Nonnull String bearerToken, @Nonnull FriendJson friend) {
        final Response<UserJson> response;
        try {
            response = gatewayApi.sendInvitation(bearerToken, friend).execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        Assertions.assertEquals(HttpStatus.SC_OK, response.code());
        return response.body();
    }

    @Step("send /api/invitations/accept POST request to niffler-gateway")
    public UserJson acceptInvitation(@Nonnull String bearerToken, @Nonnull FriendJson friend) {
        final Response<UserJson> response;
        try {
            response = gatewayApi.acceptInvitation(bearerToken, friend).execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        Assertions.assertEquals(HttpStatus.SC_OK, response.code());
        return response.body();
    }

    @Step("send /api/invitations/decline POST request to niffler-gateway")
    public UserJson declineInvitation(@Nonnull String bearerToken, @Nonnull FriendJson friend) {
        final Response<UserJson> response;
        try {
            response = gatewayApi.declineInvitation(bearerToken, friend).execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        Assertions.assertEquals(HttpStatus.SC_OK, response.code());
        return response.body();
    }

    @Step("send /api/users/all GET request to niffler-gateway")
    public @Nonnull List<UserJson> allUsers(@Nonnull String bearerToken, @Nullable String searchQuery) {
        final Response<List<UserJson>> response;
        try {
            response = gatewayApi.allUsers(bearerToken, searchQuery).execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        Assertions.assertEquals(HttpStatus.SC_OK, response.code());
        return response.body() != null
                ? response.body()
                : Collections.emptyList();
    }

}
