package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.Spending;
import guru.qa.niffler.jupiter.annotation.meta.User;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@WebTest
public class ProfileWebTest {

    private static final Config CFG = Config.getInstance();

    @User(spendings = @Spending(
            category = "Почти",
            description = "Обучение Advanced 2.0",
            amount = 79990
    ),
            username = "books",
            categories = @Category(
                    archived = true
            )

    )
    @Test
    void archivedCategoryShouldPresentInCategoriesList(CategoryJson category) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(category.username(), "12345")
                .clickPersonIcon()
                .openProfilePage()
                .clickShowArchivedCheckbox()
                .checkCategoryArchived(category.archived(), category.name());
    }

    @User(
            username = "books",
            categories = @Category(
                    archived = false
            ),
            spendings = @Spending(
                    category = "Обучение",
                    description = "Обучение Advanced 2.0",
                    amount = 79990
            )
    )
    @Test
    void activeCategoryShouldPresentInCategoriesList(CategoryJson category) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(category.username(), "12345")
                .clickPersonIcon()
                .openProfilePage()
                .checkCategoryArchived(category.archived(), category.name());
    }
}
