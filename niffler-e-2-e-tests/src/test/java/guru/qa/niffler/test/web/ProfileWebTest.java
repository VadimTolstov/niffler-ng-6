package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.Spending;
import guru.qa.niffler.jupiter.annotation.meta.User;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.annotation.ParametersAreNonnullByDefault;

import static guru.qa.niffler.utils.RandomDataUtils.randomName;

@WebTest
@ParametersAreNonnullByDefault
public class ProfileWebTest {

    private static final Config CFG = Config.getInstance();

    @User(
            spendings = @Spending(
            category = "Обучение41",
            description = "Обучение Advanced 9.0",
            amount = 79990
    ),

            categories = @Category(
                    name = "Не обучение12",
                    archived = true
            )

    )
    @Test
    void archivedCategoryShouldPresentInCategoriesList(UserJson user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), "12345")
                .openProfilePage()
                .categoryShouldNotBeVisible(user.testData().categories().get(0).name())
                .showArchivedCategories()
                .categoryShouldBeVisible(user.testData().categories().get(0).name());
    }

    @User(
            categories = @Category(
                    archived = false
            ),
            spendings = @Spending(
                    category = "Обучение42",
                    description = "Обучение Advanced 5.0",
                    amount = 79990
            )
    )
    @Test
    void activeCategoryShouldPresentInCategoriesList(UserJson user) {
        String name = randomName();
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), "12345")
                .getHeader()
                .toProfilePage()
                .setName(name)
                .clickSaveButton()
                .shouldBeVisibleSaveChangesSuccessMessage();
    }

    @User
    @Test
    void shouldUpdateProfileName(UserJson user) {
        String name = randomName();
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.testData().password())
                .getHeader()
                .toProfilePage()
                .setName(name)
                .clickSaveButton()
                .shouldBeVisibleSaveChangesSuccessMessage();
    }
    @User
    @Test
    void changeNameAndCheckAlertTest(UserJson user) {
        String name = randomName();
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.testData().password())
                .getHeader()
                .toProfilePage()
                .setName(name)
                .clickSaveButton()
                .checkAlert("Profile successfully updated")
                .checkName(name);
    }
}
