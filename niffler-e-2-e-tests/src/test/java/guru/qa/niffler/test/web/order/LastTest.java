package guru.qa.niffler.test.web.order;

import guru.qa.niffler.api.UserDataApiClient;
import guru.qa.niffler.jupiter.annotation.meta.User;
import guru.qa.niffler.model.UserJson;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.util.List;

@Order(Integer.MAX_VALUE)
public class LastTest {

    @User
    @Test
    void lastUserTest(UserJson userJson){
        UserDataApiClient userDataApi = new UserDataApiClient();
        List<UserJson> response = userDataApi.getAllUsers(userJson.username(), null);
        Assertions.assertFalse(response.isEmpty(), "Список пользователей должен быть не пустым");
    }
}
