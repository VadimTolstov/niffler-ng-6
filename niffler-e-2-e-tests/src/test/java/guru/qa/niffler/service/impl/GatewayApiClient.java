package guru.qa.niffler.service.impl;

import guru.qa.niffler.api.GatewayApi;
import guru.qa.niffler.api.core.RestClient;
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
}
