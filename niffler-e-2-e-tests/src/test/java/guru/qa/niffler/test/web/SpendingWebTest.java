package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.DisabledByIssue;
import guru.qa.niffler.jupiter.annotation.ScreenShotTest;
import guru.qa.niffler.jupiter.annotation.Spending;
import guru.qa.niffler.jupiter.annotation.meta.User;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.EditSpendingPage;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.MainPage;
import guru.qa.niffler.utils.ScreenDiffResult;
import org.junit.jupiter.api.Test;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.imageio.ImageIO;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.time.LocalDate;

import static com.codeborne.selenide.Selenide.$;
import static guru.qa.niffler.utils.RandomDataUtils.randomCategoryName;
import static guru.qa.niffler.utils.RandomDataUtils.randomSentence;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@WebTest
@ParametersAreNonnullByDefault
public class SpendingWebTest {

    @User(
            username = "books",
            spendings = @Spending(
                    category = "Тест",
                    description = "Обучение Advanced 2.0",
                    amount = 79990
            )
    )
    @DisabledByIssue("2")
    @Test
    void categoryDescriptionShouldBeChangedFromTable(SpendJson spend) {
        final String newDescription = "Обучение Niffler Next Generation1";

        Selenide.open(LoginPage.URL, LoginPage.class)
                .login(spend.username(), "12345")
                .editSpending(spend.description())
                .setNewSpendingDescription(newDescription)
                .save();

        new MainPage().checkThatTableContainsSpending(newDescription);
    }

    @User
    @Test
    void addSpendTest(UserJson user) {
        String category = randomCategoryName();
        String description = randomSentence(2);

        Selenide.open(LoginPage.URL, LoginPage.class)
                .login(user.username(), user.testData().password())
                .getHeader()
                .addSpendingPage()
                .setSpendingCategory(category)
                .setNewSpendingDescription(description)
                .setSpendingAmount("10")
                .getCalendar()
                .selectDateInCalendar(LocalDate.now());
        new EditSpendingPage().save();
        new MainPage().checkThatTableContainsSpending(description);
    }

    @User
    @Test
    void addSpendAndCheckAlertTest(UserJson user) {
        String category = randomCategoryName();
        String description = randomSentence(2);

        Selenide.open(LoginPage.URL, LoginPage.class)
                .login(user.username(), user.testData().password())
                .getHeader()
                .addSpendingPage()
                .setSpendingCategory(category)
                .setNewSpendingDescription(description)
                .setSpendingAmount("10")
                .getCalendar()
                .selectDateInCalendar(LocalDate.now())
                .save()
                .checkAlert("New spending is successfully created");
        new MainPage().checkThatTableContainsSpending(description);
    }

    @User(
            spendings = @Spending(
                    category = "Обучение",
                    description = "Обучение Advanced 2.0",
                    amount = 79990
            )
    )
    @ScreenShotTest("img/expected-stat.png")
    void checkStatComponentTest(UserJson user, BufferedImage expected) throws IOException, InterruptedException {
        Selenide.open(LoginPage.URL, LoginPage.class)
                .login(user.username(), user.testData().password());

BufferedImage actual = ImageIO.read($("canvas[role='img']").screenshot());

        assertTrue(new ScreenDiffResult(
                expected,
                actual
        ), "Screen comparison failure");
    }
}
