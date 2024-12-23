package guru.qa.niffler.model.rest;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public record TestData(
        @JsonIgnore @Nonnull String password,
        @JsonIgnore @Nonnull List<CategoryJson> categories,
        @JsonIgnore @Nonnull List<SpendJson> spendings,
        @JsonIgnore @Nonnull List<UserJson> income,
        @JsonIgnore @Nonnull List<UserJson> outcome,
        @JsonIgnore @Nonnull List<UserJson> friends) {

    public TestData(@Nonnull String password) {
        this(password, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
    }

    public TestData(@Nonnull String password, @Nonnull List<CategoryJson> categories) {
        this(password, categories, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
    }

    public TestData(@Nonnull String password, @Nonnull List<CategoryJson> categories, @Nonnull List<SpendJson> spendings, @Nonnull List<UserJson> income, @Nonnull List<UserJson> outcome, @Nonnull List<UserJson> friends) {
        this.password = password;
        this.categories = categories;
        this.spendings = spendings;
        this.income = income;
        this.outcome = outcome;
        this.friends = friends;
    }

    public @Nonnull String[] friendsUsernames() {
        return extractUsernames(friends);
    }

    public @Nonnull String[] incomeInvitationsUsernames() {
        return extractUsernames(income);
    }

    public @Nonnull String[] outcomeInvitationsUsernames() {
        return extractUsernames(outcome);
    }

    private @Nonnull String[] extractUsernames(List<UserJson> users) {
        return users.stream().map(UserJson::username).toArray(String[]::new);
    }
}
