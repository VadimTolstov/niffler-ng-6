package guru.qa.niffler.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import guru.qa.niffler.api.GhApi;
import guru.qa.niffler.api.core.RestClient;
import guru.qa.niffler.service.GhClient;
import io.qameta.allure.Step;
import org.apache.hc.core5.http.HttpStatus;
import retrofit2.Response;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.io.IOException;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ParametersAreNonnullByDefault
public class GhApiClient extends RestClient implements GhClient {

    private static final String GH_TOKEN_ENV = "GITHUB_TOKEN";

    private final GhApi ghApi;

    public GhApiClient() {
        super(CFG.ghUrl());
        this.ghApi = retrofit.create(GhApi.class);
    }

    @Step("Send GET(\"repos/VadimTolstov/niffler-ng-6/issues/{issue_number}\") to github api")
    @Nonnull
    public String issueState(String issueNumber) {
        final Response<JsonNode> response;
        try {
            response = ghApi.issue(
                    "Bearer " + System.getenv(GH_TOKEN_ENV),
                    issueNumber).execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(HttpStatus.SC_OK, response.code());
        return Objects.requireNonNull(response.body()).get("state").asText();
    }
}
