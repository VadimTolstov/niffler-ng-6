package guru.qa.niffler.service.impl;

import guru.qa.niffler.api.GatewayV2Api;
import guru.qa.niffler.api.core.RestClient;
import guru.qa.niffler.model.rest.UserJson;
import guru.qa.niffler.model.rest.pageable.RestResponsePage;
import io.qameta.allure.Step;
import org.apache.hc.core5.http.HttpStatus;
import org.junit.jupiter.api.Assertions;
import retrofit2.Response;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;

public class GatewayApiV2Client extends RestClient {

    private final GatewayV2Api gatewayV2Api;

    public GatewayApiV2Client() {
        super(CFG.gatewayUrl());
        this.gatewayV2Api = retrofit.create(GatewayV2Api.class);
    }

    @Step("send /api/v2/friends/all GET request to niffler-gateway")
    public RestResponsePage<UserJson> allFriends(@Nonnull String bearerToken,
                                                 @Nullable String searchQuery,
                                                 @Nullable Integer page,
                                                 @Nullable Integer size,
                                                 @Nullable String sort) {
        final Response<RestResponsePage<UserJson>> response;

        try {
            response = gatewayV2Api.allFriends(bearerToken, searchQuery, page, size, sort).execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        Assertions.assertEquals(HttpStatus.SC_OK, response.code());
        return response.body();
    }
}
