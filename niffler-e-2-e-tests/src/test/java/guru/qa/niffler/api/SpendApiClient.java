package guru.qa.niffler.api;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.data.entity.userdata.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.service.SpendClient;
import org.apache.hc.core5.http.HttpStatus;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SpendApiClient {

    private final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(Config.getInstance().spendUrl())
            .addConverterFactory(JacksonConverterFactory.create())
            .build();

    private final SpendApi spendApi = retrofit.create(SpendApi.class);

    public SpendJson createSpend(SpendJson spend) {
        final Response<SpendJson> response;
        try {
            response = spendApi
                    .addSpend(spend)
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(HttpStatus.SC_CREATED, response.code());
        return response.body();
    }


    public Optional<SpendJson> findSpendById(UUID id) {
        throw new UnsupportedOperationException("FindSpendById a spend is not supported by API");
    }

    public List<SpendJson> findSpendByUsernameAndDescription(String username, String description) {
        throw new UnsupportedOperationException("FindSpendByUsernameAndDescription a spends is not supported by API");
    }

    public void deleteSpend(SpendJson spend) {
        final Response<Void> response;
        try {
            response = spendApi
                    .deleteSpends(spend.username(), List.of(spend.id().toString()))
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(HttpStatus.SC_OK, response.code());
    }

    public SpendJson updateSpend(SpendJson spend) {
        final Response<SpendJson> response;
        try {
            response = spendApi
                    .editSpend(spend)
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(HttpStatus.SC_OK, response.code());
        return response.body();
    }

    public SpendJson getSpend(String id, String username) {
        final Response<SpendJson> response;
        try {
            response = spendApi
                    .getSpend(id, username)
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(HttpStatus.SC_OK, response.code());
        return response.body();
    }

    public List<SpendJson> getSpends(String username, CurrencyValues filterCurrency,
                                     Date from, Date to) {
        final Response<List<SpendJson>> response;
        try {
            response = spendApi
                    .getSpends(username, filterCurrency, from, to)
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(HttpStatus.SC_OK, response.code());
        return response.body();
    }

    public void deleteSpends(String username, List<String> ids) {
        final Response<Void> response;
        try {
            response = spendApi
                    .deleteSpends(username, ids)
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(HttpStatus.SC_OK, response.code());
    }

    public CategoryJson createCategory(CategoryJson category) {
        final Response<CategoryJson> response;
        try {
            response = spendApi
                    .addCategory(category)
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(HttpStatus.SC_OK, response.code());
        return response.body();
    }

    public CategoryJson updateCategory(CategoryJson category) {
        final Response<CategoryJson> response;
        try {
            response = spendApi
                    .updateCategory(category)
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(HttpStatus.SC_OK, response.code());
        return response.body();
    }

    public Optional<CategoryJson> findCategoryById(UUID id) {
        throw new UnsupportedOperationException("FindCategoryById a category is not supported by API");
    }

    public Optional<CategoryJson> findCategoryByUsernameAndCategoryName(String username, String name) {
        throw new UnsupportedOperationException("FindCategoryByUsernameAndCategoryName a category is not supported by API");
    }

    public void deleteCategory(CategoryJson category) {
        throw new UnsupportedOperationException("Deleting a category is not supported by API");
    }

    public List<CategoryJson> getCategories(String username, boolean excludeArchived) {
        final Response<List<CategoryJson>> response;
        try {
            response = spendApi
                    .getCategories(username, excludeArchived)
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(HttpStatus.SC_OK, response.code());
        return response.body();
    }
}
