package guru.qa.niffler.service;

import guru.qa.niffler.api.SpendApiClient;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class SpendRestApiClient implements SpendClient {

    SpendApiClient spendApiClient = new SpendApiClient();

    @Override
    public SpendJson createSpend(SpendJson spend) {
        return spendApiClient.createSpend(spend);
    }

    @Override
    public SpendJson updateSpend(SpendJson spend) {
        return spendApiClient.updateSpend(spend);
    }

    @Override
    public Optional<SpendJson> findSpendById(UUID id) {
        return spendApiClient.findSpendById(id);
    }

    @Override
    public List<SpendJson> findSpendByUsernameAndDescription(String username, String description) {
        return spendApiClient.findSpendByUsernameAndDescription(username, description);
    }

    @Override
    public void deleteSpend(SpendJson spend) {
        spendApiClient.deleteSpend(spend);
    }

    @Override
    public CategoryJson createCategory(CategoryJson category) {
        return spendApiClient.createCategory(category);
    }

    @Override
    public CategoryJson updateCategory(CategoryJson category) {
        return spendApiClient.updateCategory(category);
    }

    @Override
    public Optional<CategoryJson> findCategoryById(UUID id) {
        return spendApiClient.findCategoryById(id);
    }

    @Override
    public Optional<CategoryJson> findCategoryByUsernameAndCategoryName(String username, String name) {
        return spendApiClient.findCategoryByUsernameAndCategoryName(username, name);
    }

    @Override
    public void deleteCategory(CategoryJson category) {
        spendApiClient.deleteCategory(category);
    }
}
