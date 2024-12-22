package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.meta.User;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.SpendClient;
import guru.qa.niffler.service.impl.SpendDbClient;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.platform.commons.support.AnnotationSupport;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

import static guru.qa.niffler.utils.RandomDataUtils.randomCategoryName;

public class CategoryExtension implements
        BeforeEachCallback,
        AfterTestExecutionCallback,
        ParameterResolver {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(CategoryExtension.class);

    private final SpendClient spendClient = new SpendDbClient();

    @Override
    public void beforeEach(@Nonnull ExtensionContext context) throws Exception {
        AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), User.class)
                .ifPresent(userAnno -> {
                    if (ArrayUtils.isNotEmpty(userAnno.categories())) {
                        List<CategoryJson> result = new ArrayList<>();

                        UserJson user = context.getStore(UserExtension.NAMESPACE)
                                .get(context.getUniqueId(), UserJson.class);

                        for (Category categoryAnno : userAnno.categories()) {
                            final String categoryName = "".equals(categoryAnno.name())
                                    ? randomCategoryName()
                                    : categoryAnno.name();

                            CategoryJson category = new CategoryJson(
                                    null,
                                    categoryName,
                                   user != null ? user.username() : userAnno.username(),
                                    categoryAnno.archived()
                            );
                            CategoryJson createdCategory = spendClient.createCategory(category);
                            result.add(createdCategory);
                        }

                        if (user != null) {
                            user.testData().categories().addAll(result);
                        } else {
                            context.getStore(NAMESPACE).put(context.getUniqueId(), result);
                        }
                    }
                });
    }

    @Override
    public void afterTestExecution(@Nonnull ExtensionContext context) throws Exception {
        UserJson user = context.getStore(UserExtension.NAMESPACE)
                .get(context.getUniqueId(), UserJson.class);

        List<CategoryJson> categories = user != null
                ? user.testData().categories()
                : context.getStore(NAMESPACE).get(context.getUniqueId(), List.class);
        for (CategoryJson category : categories) {
            spendClient.deleteCategory(category);
        }
    }

    @Override
    public boolean supportsParameter(@Nonnull ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(CategoryJson[].class);
    }

    @Override
    @SuppressWarnings("unchecked")
    public CategoryJson[] resolveParameter(ParameterContext parameterContext, @Nonnull ExtensionContext extensionContext) throws ParameterResolutionException {
        return (CategoryJson[]) extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId(), List.class).toArray();
    }
}