package guru.qa.niffler.test.web;


import guru.qa.niffler.api.AuthApiClient;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.Token;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class OAuthTest {
    private final AuthApiClient authApiClient = new AuthApiClient();

    private final static Config CFG = Config.getInstance();

    @Test
    @ApiLogin(username = "Adica",password = "12345")
    void oauthTest(@Token String token) {
     //   final String token = authApiClient.singIn("Adica", "12345");
        System.out.println("Получили токен = " + token);
        Assertions.assertNotNull(token);
    }
}
