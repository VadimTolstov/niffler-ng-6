package guru.qa.niffler.test.web;

import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.UsersDbClient;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class JdbcTest {

//    @Test
//    void springJdbcTest() {
//        UsersDbClient usersDbClient = new UsersDbClient();
//        UserJson myself = usersDbClient.createUser(
//                new UserJson(
//                        null,
//                        "myself3",
//                        null,
//                        null,
//                        null,
//                        CurrencyValues.RUB,
//                        null,
//                        null,
//                        null
//                )
//        );
//
//        UserJson friend = usersDbClient.createUser(
//                new UserJson(
//                        null,
//                        "friend3",
//                        null,
//                        null,
//                        null,
//                        CurrencyValues.RUB,
//                        null,
//                        null,
//                        null
//                )
//        );
//
//        UserJson income = usersDbClient.createUser(
//                new UserJson(
//                        null,
//                        "income3",
//                        null,
//                        null,
//                        null,
//                        CurrencyValues.RUB,
//                        null,
//                        null,
//                        null
//                )
//        );
//
//        UserJson outcome = usersDbClient.createUser(
//                new UserJson(
//                        null,
//                        "outcome3",
//                        null,
//                        null,
//                        null,
//                        CurrencyValues.RUB,
//                        null,
//                        null,
//                        null
//                )
//        );
//
//        usersDbClient.addInvitation(income, myself);
//        usersDbClient.addInvitation(myself, outcome);
//        usersDbClient.addFriend(myself, friend);
//    }

    @ValueSource(strings = {
            "oleg-2",
            "oleg-3",
            "oleg-4"
    })
    @ParameterizedTest
    void springJdbcTest2(String uname) {
        UsersDbClient usersDbClient = new UsersDbClient();
        UserJson user = usersDbClient.createUser(uname,
                "12345");
    }
}