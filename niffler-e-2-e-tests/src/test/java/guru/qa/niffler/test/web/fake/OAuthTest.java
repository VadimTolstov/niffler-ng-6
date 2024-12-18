package guru.qa.niffler.test.web.fake;


import guru.qa.niffler.service.impl.AuthApiClient;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.Token;
import guru.qa.niffler.model.UserJson;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class OAuthTest {
    private final AuthApiClient authApiClient = new AuthApiClient();

    private final static Config CFG = Config.getInstance();

    @Test
    @ApiLogin(username = "Adica", password = "12345")
    void oauthTest(@Token String token, UserJson user) {
        System.out.println(user);
        Assertions.assertNotNull(token);
    }
}
