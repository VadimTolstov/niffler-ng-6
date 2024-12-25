package guru.qa.niffler.api;

import guru.qa.niffler.data.entity.userdata.CurrencyValues;
import guru.qa.niffler.model.rest.DataFilterValues;
import guru.qa.niffler.model.rest.SpendJson;
import guru.qa.niffler.model.rest.UserJson;
import guru.qa.niffler.model.rest.pageable.RestResponsePage;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Интерфейс GatewayV2Api определяет методы для взаимодействия с API шлюза приложения версии 2.
 * Используется библиотека Retrofit для выполнения HTTP-запросов к серверу.
 * Предоставляет методы для получения данных с поддержкой пагинации, фильтрации и сортировки.
 */
public interface GatewayV2Api {

    /**
     * Получает список друзей с возможностью поиска, пагинации и сортировки.
     *
     * @param bearerToken Токен авторизации (обязательный).
     * @param searchQuery Поисковый запрос для фильтрации друзей (необязательный).
     * @param page        Номер страницы (необязательный).
     * @param size        Количество объектов на странице (необязательный).
     * @param sort        Параметры сортировки (необязательный).
     * @return Объект {@link RestResponsePage<UserJson>}, содержащий список друзей с информацией о пагинации.
     */
    @GET("/api/v2/friends/all")
    Call<RestResponsePage<UserJson>> allFriends(@Header("Authorization") @Nonnull String bearerToken,
                                                @Query("searchQuery") @Nullable String searchQuery,
                                                @Query("page") @Nullable Integer page,
                                                @Query("size") @Nullable Integer size,
                                                @Query("sort") @Nullable String sort);

    /**
     * Получает список всех трат с возможностью пагинации, фильтрации по периоду и валюте, а также поиска.
     *
     * @param bearerToken    Токен авторизации (обязательный).
     * @param page           Номер страницы (необязательный).
     * @param filterPeriod   Период фильтрации (необязательный).
     * @param filterCurrency Валюта фильтрации (необязательный).
     * @param searchQuery    Поисковый запрос для фильтрации трат (необязательный).
     * @return Объект {@link RestResponsePage<SpendJson>}, содержащий список трат с информацией о пагинации.
     */
    @GET("/api/v2/spends/all")
    Call<RestResponsePage<SpendJson>> allSpends(@Header("Authorization") @Nonnull String bearerToken,
                                                @Query("page") @Nullable Integer page,
                                                @Query("filterPeriod") @Nullable DataFilterValues filterPeriod,
                                                @Query("filterCurrency") @Nullable CurrencyValues filterCurrency,
                                                @Query("searchQuery") @Nullable String searchQuery);

    /**
     * Получает список всех пользователей с возможностью поиска, пагинации и сортировки.
     *
     * @param bearerToken Токен авторизации (обязательный).
     * @param searchQuery Поисковый запрос для фильтрации пользователей (необязательный).
     * @param page        Номер страницы (необязательный).
     * @param size        Количество объектов на странице (необязательный).
     * @param sort        Параметры сортировки (необязательный).
     * @return Объект {@link RestResponsePage<UserJson>}, содержащий список пользователей с информацией о пагинации.
     */
    @GET("/api/v2/users/all")
    Call<RestResponsePage<UserJson>> allUsers(@Header("Authorization") @Nonnull String bearerToken,
                                              @Query("searchQuery") @Nullable String searchQuery,
                                              @Query("page") @Nullable Integer page,
                                              @Query("size") @Nullable Integer size,
                                              @Query("sort") @Nullable String sort);
}