package guru.qa.niffler.service.impl;

import com.google.common.base.Stopwatch;
import guru.qa.niffler.api.UserDataApi;
import guru.qa.niffler.api.core.RestClient;
import guru.qa.niffler.api.core.ThreadSafeCookiesStore;
import guru.qa.niffler.api.enums.TokenName;
import guru.qa.niffler.model.rest.TestData;
import guru.qa.niffler.model.rest.UserJson;
import guru.qa.niffler.service.UsersClient;
import io.qameta.allure.Step;
import org.apache.hc.core5.http.HttpStatus;
import retrofit2.Response;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static guru.qa.niffler.utils.RandomDataUtils.randomUsername;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ParametersAreNonnullByDefault
public class UserDataApiClient extends RestClient implements UsersClient {
    private static final String defaultPassword = "12345";

    private final UserDataApi userDataApi;
    private final AuthApiClient authApiClient = new AuthApiClient();

    public UserDataApiClient() {
        super(CFG.userdataUrl());
        this.userDataApi = retrofit.create(UserDataApi.class);
    }

    @Override
    @Nonnull
    @Step("Создание пользователя: username = {username}, password = {password}")
    public UserJson createUser(String username, String password) {
        // Запрос формы регистрации для получения CSRF токена
        authApiClient.requestRegisterForm();
        authApiClient.registerUser(
                username,
                password,
                password,
                ThreadSafeCookiesStore.INSTANCE.cookieValue(TokenName.CSRF.getCookieName())
        );

        // Ожидание появления пользователя после регистрации
        long maxWaitTime = 5000L; // 5 секунд ожидания
        Stopwatch sw = Stopwatch.createStarted();

        while (sw.elapsed(TimeUnit.MILLISECONDS) < maxWaitTime) {
            try {
                UserJson userJson = userDataApi.getCurrentUser(username)
                        .execute()
                        .body();
                if (userJson != null || userJson.id() != null) {
                    // Пользователь найден, добавляем ему TestData и возвращаем
                    return userJson.addTestData(
                            new TestData(password)
                    );
                } else {
                    Thread.sleep(100); // Ожидаем перед следующей проверкой
                }
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException("Ошибка при выполнении запроса на получение пользователя или ожидании", e);
            }
        }
        // Если пользователь не найден за отведенное время
        throw new AssertionError("Пользователь не был найден в системе после истечения времени ожидания");
    }

    @Nonnull
    @Step("Получение текущего пользователя по имени: {username}")
    public UserJson getCurrentUser(String username) {
        final Response<UserJson> response;
        try {
            response = userDataApi
                    .getCurrentUser(username)
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(HttpStatus.SC_OK, response.code());
        return Objects.requireNonNull(response.body(), "Ответ API вернул null");
    }

    @Step("Получение списка пользователей по имени: {username}")
    public @Nonnull List<UserJson> getAllUsers(String username, String searchQuery) {
        final Response<List<UserJson>> response;
        try {
            response = userDataApi
                    .getAllUsers(username, searchQuery)
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(HttpStatus.SC_OK, response.code());
        return response.body() != null
                ? response.body()
                : Collections.emptyList();
    }

    @Step("Обновить информацию о пользователе по имени: {userJson.username()}")
    public @Nullable UserJson updateUserInfo(UserJson userJson) {
        final Response<UserJson> response;
        try {
            response = userDataApi
                    .updateUserInfo(userJson)
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(HttpStatus.SC_OK, response.code());
        return response.body();
    }

    @Step("Отправка приглашения от пользователя {username} пользователю {targetUsername}")
    public @Nullable UserJson sendInvitation(String username, String targetUsername) {
        final Response<UserJson> response;
        try {
            response = userDataApi
                    .sendInvitation(username, targetUsername)
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(HttpStatus.SC_OK, response.code());
        return response.body();
    }

    @Step("Принятие приглашения от пользователя {username} пользователю {targetUsername}")
    public @Nullable UserJson acceptInvitation(String username, String targetUsername) {
        final Response<UserJson> response;
        try {
            response = userDataApi
                    .acceptInvitation(username, targetUsername)
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(HttpStatus.SC_OK, response.code());
        return response.body();
    }

    @Step("Отклонить приглашение о дружбе от пользователя {targetUsername} к пользователю {username.}")
    public @Nullable UserJson declineInvitation(String username, String targetUsername) {
        final Response<UserJson> response;
        try {
            response = userDataApi
                    .declineInvitation(username, targetUsername)
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(HttpStatus.SC_OK, response.code());
        return response.body();
    }

    @Override
    @Step("Добавление {count} входящих приглашений пользователю: {targetUser.username}")
    public @Nullable List<UserJson> addIncomeInvitations(UserJson targetUser, int count) {
        List<UserJson> incomeUsers = new ArrayList<>();
        if (count > 0) {
            // Шаг 1: Проверка, существует ли целевой пользователь (targetUser)
            UserJson user = getCurrentUser(targetUser.username());

            if (user == null || user.id() == null) {
                throw new AssertionError("Пользователь с именем " + targetUser.username() + " не найден");
            }

            for (int i = 0; i < count; i++) {
                // Шаг 2: Создание рандомного пользователя
                UserJson newUser = createUser(randomUsername(), defaultPassword);

                // Шаг 3: Отправка приглашения в друзья
                sendInvitation(newUser.username(), targetUser.username());

                // Добавляем созданного пользователя в список
                incomeUsers.add(newUser);
                // Добавляем созданного пользователя в модель targetUser
                targetUser.testData().income().add(newUser);
            }
        }
        return incomeUsers;
    }

    @Override
    @Step("Добавление {count} исходящих приглашений пользователю: {targetUser.username}")
    public @Nonnull List<UserJson> addOutcomeInvitations(UserJson targetUser, int count) {
        List<UserJson> incomeUsers = new ArrayList<>();
        if (count > 0) {
            // Шаг 1: Проверка, существует ли целевой пользователь (targetUser)
            UserJson user = getCurrentUser(targetUser.username());

            if (user == null || user.id() == null) {
                throw new AssertionError("Пользователь с именем " + targetUser.username() + " не найден");
            }

            for (int i = 0; i < count; i++) {
                // Шаг 2: Создание рандомного пользователя
                UserJson newUser = createUser(randomUsername(), "12345");

                // Шаг 3: Отправка приглашения в друзья
                sendInvitation(targetUser.username(), newUser.username());

                // Добавляем созданного пользователя в список
                incomeUsers.add(newUser);
                // Добавляем созданного пользователя в модель targetUser
                targetUser.testData().outcome().add(newUser);
            }
        }
        return incomeUsers;
    }

    @Override
    @Step("Добавление {count} друзей пользователю: {targetUser.username}")
    public @Nonnull List<UserJson> addFriends(@Nonnull UserJson targetUser, int count) {
        List<UserJson> friends = new ArrayList<>();

        if (count > 0) {
            // Шаг 1: Проверка, существует ли целевой пользователь (targetUser)
            UserJson user = getCurrentUser(targetUser.username());
            if (user == null || user.id() == null) {
                throw new AssertionError("Пользователь с именем " + targetUser.username() + " не найден");
            }

            for (int i = 0; i < count; i++) {
                //создаем нового юзера
                UserJson newUser = createUser(randomUsername(), defaultPassword);

                //отправляем приглушения в друзья
                sendInvitation(newUser.username(), targetUser.username());
                //принимаем приглушения
                acceptInvitation(targetUser.username(), newUser.username());

                // Добавляем созданного друга в список
                friends.add(newUser);
                // Добавляем созданного друга в модель targetUser
                targetUser.testData().friends().add(newUser);
            }
        }
        return friends;
    }

    @Step("Удаление списка друзей по имени: {username}")
    public @Nonnull List<UserJson> removeFriend(String username, String searchQuery) {
        final Response<List<UserJson>> response;
        try {
            response = userDataApi
                    .removeFriend(username, searchQuery)
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(HttpStatus.SC_OK, response.code());
        return response.body() != null
                ? response.body()
                : Collections.emptyList();
    }

    @Step("Получаем всех друзей по API")
    @Nonnull
    public List<UserJson> friends(@Nonnull String username, @Nullable String searchQuery) {
        final Response<List<UserJson>> response;
        try {
            response = userDataApi.friends(username, searchQuery).execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(HttpStatus.SC_OK, response.code());
        return response.body() != null
                ? response.body()
                : Collections.emptyList();
    }
}