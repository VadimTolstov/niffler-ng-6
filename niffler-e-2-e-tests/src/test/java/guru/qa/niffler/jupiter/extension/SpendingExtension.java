package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.Spending;
import guru.qa.niffler.jupiter.annotation.meta.User;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.data.entity.userdata.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.SpendClient;
import guru.qa.niffler.service.SpendDbClient;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static guru.qa.niffler.utils.RandomDataUtils.randomCategoryName;

public class SpendingExtension implements BeforeEachCallback, ParameterResolver {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(SpendingExtension.class);

    private final SpendClient spendClient = new SpendDbClient();

    @Override
    public void beforeEach(@Nonnull ExtensionContext context) throws Exception {
        AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), User.class)
                .ifPresent(userAnno -> {
                            if (userAnno.spendings().length > 0) {
                                if (ArrayUtils.isNotEmpty(userAnno.spendings())) {
                                    List<SpendJson> result = new ArrayList<>();

                                    UserJson user = context.getStore(UserExtension.NAMESPACE)
                                            .get(context.getUniqueId(), UserJson.class);

                                    for (Spending spendAnno : userAnno.spendings()) {
                                        SpendJson spend = new SpendJson(
                                                null,
                                                new Date(),
                                                new CategoryJson(
                                                        null,
                                                        spendAnno.category(),
                                                        user != null ? user.username() : userAnno.username(),
                                                        false
                                                ),
                                                CurrencyValues.RUB,
                                                spendAnno.amount(),
                                                spendAnno.description(),
                                                user != null ? user.username() : userAnno.username()
                                        );

                                        SpendJson createdSpend = spendClient.createSpend(spend);
                                        result.add(createdSpend);
                                    }

                                    if (user != null) {
                                        user.testData().spendings().addAll(result);
                                    } else {
                                        context.getStore(NAMESPACE).put(context.getUniqueId(), result);
                                    }
                                }
                            }
                        }
                );
    }


    @Override
    public boolean supportsParameter(@Nonnull ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(SpendJson[].class);
    }

    @Override
    @SuppressWarnings("unchecked")
    public SpendJson[] resolveParameter(ParameterContext parameterContext, @Nonnull ExtensionContext extensionContext) throws ParameterResolutionException {
        return (SpendJson[]) extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId(), List.class).toArray();
    }
}
