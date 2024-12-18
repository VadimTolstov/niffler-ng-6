package guru.qa.niffler.test.web.order;

import guru.qa.niffler.service.impl.UserDataApiClient;
import guru.qa.niffler.jupiter.annotation.meta.User;
import guru.qa.niffler.model.UserJson;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.util.List;

@Order(1)
public class FirstTest {

    @User
    @Test
    void firstUserTest(UserJson userJson) {
        UserDataApiClient userDataApi = new UserDataApiClient();
        List<UserJson> response = userDataApi.getAllUsers(userJson.username(), null);
        Assertions.assertTrue(response.isEmpty(), "Список пользователей должен быть пустым");
    }
}
