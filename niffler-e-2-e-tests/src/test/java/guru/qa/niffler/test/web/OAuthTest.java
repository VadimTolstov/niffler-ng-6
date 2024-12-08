package guru.qa.niffler.test.web;

import guru.qa.niffler.api.AuthApiClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class OAuthTest {
    private final AuthApiClient authApiClient = new AuthApiClient();


    @Test
    void oauthTest() {
        final String token = authApiClient.singIn("Adica", "12345");
        System.out.println("Получили токен = " + token);
        Assertions.assertNotNull(token);
    }
}
