package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
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
import guru.qa.niffler.page.RegisterPage;
import guru.qa.niffler.page.component.StatComponent;
import guru.qa.niffler.utils.RandomDataUtils;
import guru.qa.niffler.utils.ScreenDiffResult;
import org.junit.jupiter.api.Test;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;
import java.util.Objects;

import static com.codeborne.selenide.Selenide.$;
import static guru.qa.niffler.utils.RandomDataUtils.randomCategoryName;
import static guru.qa.niffler.utils.RandomDataUtils.randomSentence;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@WebTest
@ParametersAreNonnullByDefault
public class SpendingWebTest {

    @User(
            spendings = @Spending(
                    category = "Обучение",
                    description = "Обучение Advanced 2.0",
                    amount = 79990
            )
    )
    @ScreenShotTest(value = "img/expected-stat.png")
    void checkStatComponentTest(@Nonnull UserJson user, BufferedImage expected) throws IOException, InterruptedException {
       Selenide.open(LoginPage.URL, LoginPage.class)
                .fillLoginPage(user.username(), user.testData().password())
                .submit(new MainPage())
                .checkStatImg(expected)
                        .checkStatCell("Обучение 79990 ₽");
    }

    @User(
            spendings = @Spending(
                    category = "Обучение",
                    description = "Обучение Advanced 2.0",
                    amount = 79990
            )
    )
    @ScreenShotTest(value = "img/clear-stat.png",rewriteExpected = true)
    void deleteSpendingTest(@Nonnull UserJson user, BufferedImage clearStat) throws IOException {
        Selenide.open(LoginPage.URL, LoginPage.class)
                .fillLoginPage(user.username(), user.testData().password())
                .submit(new MainPage())
                .getSpendingTable()
                .deleteSpending("Обучение Advanced 2.0")
                .checkTableSize(0);

        new MainPage().checkStatImg(clearStat)
                .checkStatCell("");
    }
        @User(
            spendings = @Spending(
                    category = "Обучение",
                    description = "Обучение Advanced 2.0",
                    amount = 79990
            )
    )
    @Test
    void categoryDescriptionShouldBeChangedFromTable(UserJson user) {
        final String newDescription = "Обучение Niffler Next Generation";

        Selenide.open(LoginPage.URL, LoginPage.class)
                .fillLoginPage(user.username(), user.testData().password())
                .submit(new MainPage())
                .getSpendingTable()
                .editSpending("Обучение Advanced 2.0")
                .setNewSpendingDescription(newDescription)
                .saveSpending();

        new MainPage().getSpendingTable()
                .checkTableContains(newDescription);
    }

    @User
    @Test
    void shouldAddNewSpending(UserJson user) {
        String category = "Friends";
        int amount = 100;
        Date currentDate = new Date();
        String description = RandomDataUtils.randomSentence(3);

        Selenide.open(LoginPage.URL, LoginPage.class)
                .fillLoginPage(user.username(), user.testData().password())
                .submit(new MainPage())
                .getHeader()
                .addSpendingPage()
                .setNewSpendingCategory(category)
                .setNewSpendingAmount(amount)
                .setNewSpendingDate(currentDate)
                .setNewSpendingDescription(description)
                .saveSpending()
                .checkAlertMessage("New spending is successfully created");

        new MainPage().getSpendingTable()
                .checkTableContains(description);
    }

    @User
    @Test
    void shouldNotAddSpendingWithEmptyCategory(UserJson user) {
        Selenide.open(LoginPage.URL, LoginPage.class)
                .fillLoginPage(user.username(), user.testData().password())
                .submit(new MainPage())
                .getHeader()
                .addSpendingPage()
                .setNewSpendingAmount(100)
                .setNewSpendingDate(new Date())
                .saveSpending()
                .checkFormErrorMessage("Please choose category");
    }

    @User
    @Test
    void shouldNotAddSpendingWithEmptyAmount(UserJson user) {
        Selenide.open(LoginPage.URL, LoginPage.class)
                .fillLoginPage(user.username(), user.testData().password())
                .submit(new MainPage())
                .getHeader()
                .addSpendingPage()
                .setNewSpendingCategory("Friends")
                .setNewSpendingDate(new Date())
                .saveSpending()
                .checkFormErrorMessage("Amount has to be not less then 0.01");
    }

//    @User(
//            spendings = @Spending(
//                    category = "Обучение",
//                    description = "Обучение Advanced 2.0",
//                    amount = 79990
//            )
//    )
//    @Test
//    void deleteSpendingTest(UserJson user) {
//        Selenide.open(LoginPage.URL, LoginPage.class)
//                .fillLoginPage(user.username(), user.testData().password())
//                .submit(new MainPage())
//                .getSpendingTable()
//                .deleteSpending("Обучение Advanced 2.0")
//                .checkTableSize(0);
//    }
}
