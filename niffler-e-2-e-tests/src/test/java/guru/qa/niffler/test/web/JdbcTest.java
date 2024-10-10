package guru.qa.niffler.test.web;

import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.data.entity.userdata.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.SpendDbClient;
import guru.qa.niffler.service.UsersDbClient;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.Optional;

public class JdbcTest {

    @Test
    void springJdbcTest() {
        UsersDbClient usersDbClient = new UsersDbClient();
        UserJson myself = usersDbClient.createUser(
                new UserJson(
                        null,
                        "myself3",
                        null,
                        null,
                        null,
                        CurrencyValues.RUB,
                        null,
                        null,
                        null
                )
        );

        UserJson friend = usersDbClient.createUser(
                new UserJson(
                        null,
                        "friend3",
                        null,
                        null,
                        null,
                        CurrencyValues.RUB,
                        null,
                        null,
                        null
                )
        );

        UserJson income = usersDbClient.createUser(
                new UserJson(
                        null,
                        "income3",
                        null,
                        null,
                        null,
                        CurrencyValues.RUB,
                        null,
                        null,
                        null
                )
        );

        UserJson outcome = usersDbClient.createUser(
                new UserJson(
                        null,
                        "outcome3",
                        null,
                        null,
                        null,
                        CurrencyValues.RUB,
                        null,
                        null,
                        null
                )
        );

        usersDbClient.addInvitation(income, myself);
        usersDbClient.addInvitation(myself, outcome);
        usersDbClient.addFriend(myself, friend);
    }
}