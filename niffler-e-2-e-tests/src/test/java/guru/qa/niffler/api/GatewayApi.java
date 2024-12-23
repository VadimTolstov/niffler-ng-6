package guru.qa.niffler.api;

import guru.qa.niffler.data.entity.userdata.CurrencyValues;
import guru.qa.niffler.model.rest.*;
import retrofit2.Call;
import retrofit2.http.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * Интерфейс GatewayApi определяет набор методов для взаимодействия с API шлюза приложения.
 * Используется библиотека Retrofit для выполнения HTTP-запросов к серверу.
 * Каждый метод соответствует определенному эндпоинту API и позволяет выполнять операции CRUD
 * над различными ресурсами, такими как траты, категории, валюты, друзья и статистика.
 */
public interface GatewayApi {

    /**
     * Получает конкретную трату по её идентификатору.
     *
     * @param id    Идентификатор траты.
     * @param bearerToken Токен авторизации.
     * @return Объект SpendJson, содержащий данные траты.
     */
    @GET("/api/spends/{id}")
    Call<SpendJson> getSpend(@Path("id") String id, @Header("Authorization") String bearerToken);

    /**
     * Получает список всех трат с возможностью фильтрации по периоду и валюте.
     *
     * @param bearerToken          Токен авторизации.
     * @param filterPeriod   Период фильтрации.
     * @param filterCurrency Валюта фильтрации.
     * @return Объект List&lt;SpendJson&gt;, содержащий список трат.
     */
    @GET("/api/spends/all")
    Call<List<SpendJson>> getSpends(@Header("Authorization") String bearerToken,
                                    @Query("filterPeriod") DataFilterValues filterPeriod,
                                    @Query("filterCurrency") CurrencyValues filterCurrency);

    /**
     * Добавляет новую трату.
     *
     * @param bearerToken Токен авторизации.
     * @param spend Объект траты для добавления.
     * @return Объект SpendJson, содержащий добавленную трату.
     */
    @POST("/api/spends/add")
    Call<SpendJson> addSpend(@Header("Authorization") String bearerToken, @Body SpendJson spend);

    /**
     * Редактирует существующую трату.
     *
     * @param bearerToken Токен авторизации.
     * @param spend Объект траты для редактирования.
     * @return Объект SpendJson, содержащий отредактированную трату.
     */
    @PATCH("/api/spends/edit")
    Call<SpendJson> editSpend(@Header("Authorization") String bearerToken, @Body SpendJson spend);

    /**
     * Удаляет одну или несколько трат по их идентификаторам.
     *
     * @param bearerToken Токен авторизации.
     * @param ids   Список идентификаторов трат для удаления.
     * @return Объект SpendJson, содержащий данные об удалении.
     */
    @DELETE("/api/spends/remove")
    Call<SpendJson> deleteSpends(@Header("Authorization") String bearerToken, @Query("ids") List<String> ids);

    /**
     * Получает список всех категорий с возможностью исключения архивных.
     *
     * @param bearerToken           Токен авторизации.
     * @param excludeArchived Флаг исключения архивных категорий.
     * @return Объект List&lt;CategoryJson&gt;, содержащий список категорий.
     */
    @GET("/api/categories/all")
    Call<List<CategoryJson>> getCategories(@Header("Authorization") String bearerToken, @Query("excludeArchived") boolean excludeArchived);

    /**
     * Добавляет новую категорию.
     *
     * @param bearerToken    Токен авторизации.
     * @param category Объект категории для добавления.
     * @return Объект CategoryJson, содержащий добавленную категорию.
     */
    @POST("/api/categories/add")
    Call<CategoryJson> addCategory(@Header("Authorization") String bearerToken, @Body CategoryJson category);

    /**
     * Обновляет существующую категорию.
     *
     * @param bearerToken    Токен авторизации.
     * @param category Объект категории для обновления.
     * @return Объект CategoryJson, содержащий обновленную категорию.
     */
    @PATCH("/api/categories/update")
    Call<CategoryJson> updateCategory(@Header("Authorization") String bearerToken, @Body CategoryJson category);

    /**
     * Получает список всех доступных валют.
     *
     * @param bearerToken Токен авторизации.
     * @return Объект List&lt;CurrencyJson&gt;, содержащий список валют.
     */
    @GET("/api/currencies/all")
    Call<List<CurrencyJson>> getAllCurrencies(@Header("Authorization") String bearerToken);

    /**
     * Получает список друзей с возможностью поиска.
     *
     * @param bearerToken       Токен авторизации.
     * @param searchQuery Поисковый запрос.
     * @return Объект List&lt;UserJson&gt;, содержащий список друзей.
     */
    @GET("/api/friends/all")
    Call<List<UserJson>> allFriends(@Header("Authorization") @Nonnull String bearerToken, @Query("searchQuery") @Nullable String searchQuery);

    /**
     * Удаляет друга по его имени пользователя.
     *
     * @param bearerToken          Токен авторизации.
     * @param targetUsername Имя пользователя друга для удаления.
     * @return Объект void, содержащий результат удаления.
     */
    @GET("/api/friends/remove")
    Call<Void> removeFriend(@Header("Authorization") String bearerToken, @Query("username") String targetUsername);

    /**
     * Отправляет приглашение дружбы.
     *
     * @param bearerToken  Токен авторизации.
     * @param friend Объект друга для приглашения.
     * @return Объект UserJson, содержащий данные о приглашении.
     */
    @POST("/api/invitations/send")
    Call<UserJson> sendInvitation(@Header("Authorization") String bearerToken,
                                  @Body FriendJson friend);

    /**
     * Принимает приглашение дружбы.
     *
     * @param bearerToken      Токен авторизации.
     * @param invitation Объект приглашения.
     * @return Объект UserJson, содержащий данные о принятии приглашения.
     */
    @POST("/api/invitations/accept")
    Call<UserJson> acceptInvitation(@Header("Authorization") String bearerToken,
                                    @Body FriendJson invitation);

    /**
     * Отклоняет приглашение дружбы.
     *
     * @param bearerToken      Токен авторизации.
     * @param invitation Объект приглашения.
     * @return Объект UserJson, содержащий данные об отклонении приглашения.
     */
    @POST("/api/invitations/decline")
    Call<UserJson> declineInvitation(@Header("Authorization") String bearerToken,
                                     @Body FriendJson invitation);

    /**
     * Получает текущую сессию пользователя.
     *
     * @param bearerToken Токен авторизации.
     * @return Объект SessionJson, содержащий данные сессии.
     */
    @GET("/api/session/current")
    Call<SessionJson> session(@Header("Authorization") String bearerToken);

    /**
     * Получает общую статистику с возможностью фильтрации.
     *
     * @param bearerToken          Токен авторизации.
     * @param statCurrency   Валюта для статистики.
     * @param filterCurrency Валюта фильтрации.
     * @param filterPeriod   Период фильтрации.
     * @return Объект List&lt;StatisticJson&gt;, содержащий список статистических данных.
     */
    @GET("/api/stat/total")
    Call<List<StatisticJson>> getTotalStatistic(@Header("Authorization") String bearerToken,
                                                @Query("statCurrency") CurrencyValues statCurrency,
                                                @Query("filterCurrency") CurrencyValues filterCurrency,
                                                @Query("filterPeriod") DataFilterValues filterPeriod);

    /**
     * Получает информацию о текущем пользователе.
     *
     * @param bearerToken Токен авторизации.
     * @return Объект UserJson, содержащий данные пользователя.
     */
    @GET("/api/users/current")
    Call<UserJson> currentUser(@Header("Authorization") String bearerToken);

    /**
     * Получает список всех пользователей с возможностью поиска.
     *
     * @param bearerToken       Токен авторизации.
     * @param searchQuery Поисковый запрос.
     * @return Объект List&lt;UserJson&gt;, содержащий список пользователей.
     */
    @GET("/api/users/all")
    Call<List<UserJson>> allUsers(@Header("Authorization") String bearerToken,
                                  @Query("searchQuery") String searchQuery);

    /**
     * Обновляет информацию о пользователе.
     *
     * @param bearerToken Токен авторизации.
     * @param user  Объект пользователя для обновления.
     * @return Объект UserJson, содержащий обновленные данные пользователя.
     */
    @POST("/api/users/update")
    Call<UserJson> updateUserInfo(@Header("Authorization") String bearerToken,
                                  @Body UserJson user);
}