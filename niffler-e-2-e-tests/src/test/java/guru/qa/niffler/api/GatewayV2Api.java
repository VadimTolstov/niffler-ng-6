package guru.qa.niffler.api;

import guru.qa.niffler.model.rest.UserJson;
import guru.qa.niffler.model.rest.pageable.RestResponsePage;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

import javax.annotation.Nullable;


public interface GatewayV2Api {

    /**
     * Получает список друзей с возможностью поиска.
     *
     * @param bearerToken Токен авторизации.
     * @param page        Номер страницы.
     * @param size        Количество объектов на истринце.
     * @param searchQuery Поисковый запрос.
     * @return Объект List&lt;UserJson&gt;, содержащий список друзей.
     */
    @GET("/api/v2/friends/all")
    Call<RestResponsePage<UserJson>> allFriends(@Header("Authorization") String bearerToken,
                                                @Query("searchQuery") @Nullable String searchQuery,
                                                @Query("page") @Nullable Integer page,
                                                @Query("size") @Nullable Integer size,
                                                @Query("sort") @Nullable String sort);
}