package guru.qa.niffler.test.web;

import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.data.entity.userdata.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.SpendDbClient;
import guru.qa.niffler.service.UsersDbClient;
import org.junit.jupiter.api.Test;

import java.util.Date;

public class JdbcTest {

    @Test
    void txTest() {
        SpendDbClient spendDbClient = new SpendDbClient();

        SpendJson spend = spendDbClient.createSpend(
                new SpendJson(
                        null,
                        new Date(),
                        new CategoryJson(
                                null,
                                "cat-name-tx-114124",
                                "duck",
                                false
                        ),
                        CurrencyValues.RUB,
                        1000.0,
                        "cat-name-tx-12",
                        "cat-name-tx-12"
                )
        );

        System.out.println(spend);
    }

    @Test
    void springJdbcTest() {
        UsersDbClient usersDbClient = new UsersDbClient();
        UserJson user = usersDbClient.createUserSpringJdbcTransaction(
                new UserJson(
                        null,
                        "valentin-901",
                        null,
                        null,
                        null,
                        CurrencyValues.RUB,
                        null,
                        null,
                        null
                )
        );
        System.out.println(user);
    }

    @Test
    void createUserSpringJdbcTransactionTest() {
        UsersDbClient usersDbClient = new UsersDbClient();
        UserJson user = usersDbClient.createUserSpringJdbcTransaction(
                new UserJson(
                        null,
                        null,
                        "valentin-33",
                        null,
                        null,
                        null,
                        null,
                        null,
                        null
                )
        );
        System.out.println(user);
    }

    @Test
    void createUserSpringJdbcWithoutTransactionTest() {
        UsersDbClient usersDbClient = new UsersDbClient();
        UserJson user = usersDbClient.createUserSpringJdbc(
                new UserJson(
                        null,
                        "valentin-33",
                        null,
                        null,
                        null,
                        null,
                        "valentin-33",
                        "valentin-33",
                        null
                )
        );
        System.out.println(user);
    }

    @Test
    void createUserJdbcTransactionTest() {
        UsersDbClient usersDbClient = new UsersDbClient();
        UserJson user = usersDbClient.createUserJdbcTransaction(
                new UserJson(
                        null,
                        "valentin-33",
                        null,
                        null,
                        "valentin-33",
                        null,
                        "valentin-33",
                        "valentin-33",
                        null
                )
        );
        System.out.println(user);
    }

    @Test
    void createUserJdbcWithoutTransactionTest() {
        UsersDbClient usersDbClient = new UsersDbClient();
        UserJson user = usersDbClient.createUserJdbc(
                new UserJson(
                        null,
                        "valentin-33",
                        "valentin-33",
                        null,
                        "valentin-33",
                        null,
                        "valentin-33",
                        "valentin-33",
                        null
                )
        );
        System.out.println(user);
    }
}
