package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.api.UserDataApiClient;
import guru.qa.niffler.jupiter.annotation.meta.User;
import guru.qa.niffler.model.TestData;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.UsersClient;
import guru.qa.niffler.utils.RandomDataUtils;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class UserExtension implements BeforeEachCallback, ParameterResolver {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(UserExtension.class);
    private static final String defaultPassword = "12345";
    private final UsersClient usersClient = new UserDataApiClient();

    @Override
    public void beforeEach(@Nonnull ExtensionContext context) throws Exception {
        AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), User.class)
                .ifPresent(userAnno -> {
                    if ("".equals(userAnno.username())) {
                        final String username = RandomDataUtils.randomUsername();
                        UserJson testUser = usersClient.createUser(username, defaultPassword);
                        List<UserJson> income = usersClient.addIncomeInvitations(testUser, userAnno.income());
                        List<UserJson> outcome = usersClient.addOutcomeInvitations(testUser, userAnno.outcome());
                        List<UserJson> friends = usersClient.addFriends(testUser, userAnno.friends());

                        context.getStore(NAMESPACE).put(
                                context.getUniqueId(),
                                testUser.addTestData(
                                        new TestData(
                                                defaultPassword,
                                                new ArrayList<>(),
                                                new ArrayList<>(),
                                                income.stream().map(UserJson::username).toList(),
                                                outcome.stream().map(UserJson::username).toList(),
                                                friends.stream().map(UserJson::username).toList()

                                        )
                                )
                        );
                    }
                });
    }

    @Override
    public boolean supportsParameter(@Nonnull ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(UserJson.class);
    }

    @Override
    public UserJson resolveParameter(ParameterContext parameterContext, @Nonnull ExtensionContext extensionContext) throws ParameterResolutionException {
        return extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId(), UserJson.class);
    }
}
