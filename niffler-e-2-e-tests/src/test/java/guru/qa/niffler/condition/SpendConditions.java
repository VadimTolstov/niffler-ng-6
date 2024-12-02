package guru.qa.niffler.condition;

import com.codeborne.selenide.CheckResult;
import com.codeborne.selenide.Driver;
import com.codeborne.selenide.WebElementsCondition;
import guru.qa.niffler.model.SpendJson;
import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static com.codeborne.selenide.CheckResult.accepted;
import static com.codeborne.selenide.CheckResult.rejected;

public class SpendConditions {

    public static WebElementsCondition spends(@NotNull SpendJson... expectedSpends) {
        return new WebElementsCondition() {
            private final String spends = Arrays.stream(expectedSpends).map(
                            s -> "Category " + s.category().name() +
                                    ", Amount " + s.amount() +
                                    ", Description " + s.description() +
                                    ", Date " + new SimpleDateFormat(
                                    "MMM dd, yyyy", Locale.ENGLISH).format(s.spendDate()))
                    .toList().toString();

            @NotNull
            @Override
            public CheckResult check(Driver driver, List<WebElement> elements) {
                if (ArrayUtils.isEmpty(expectedSpends)) {
                    throw new IllegalArgumentException("No expected colors given");
                }
                if (expectedSpends.length != elements.size()) {
                    final String message = String.format("List size mismatch (expected: %s, actual: %s)", expectedSpends.length, elements.size());
                    return rejected(message, elements);
                }

                boolean passed = true;
                List<String> actualSpends = new ArrayList<>();
                for (int i = 0; i < elements.size(); i++) {
                    final List<WebElement> actualSpend = elements.get(i).findElements(By.cssSelector("td"));
                    final String actualCategory = actualSpend.get(1).getText();
                    final Double actualAmount = Double.parseDouble(actualSpend.get(2).getText().split(" ")[0]);
                    final String actualDescription = actualSpend.get(3).getText();
                    final String actualDate = actualSpend.get(4).getText();

                    actualSpends.add("Category " + actualCategory +
                            ", Amount " + actualAmount +
                            ", Description " + actualDescription +
                            ", Date " + actualDate);

                    if (passed) {
                        String expectedDate = new SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH)
                                .format(expectedSpends[i].spendDate()).toString();

                        passed = expectedSpends[i].category().name().equals(actualCategory)
                                && expectedSpends[i].amount().equals(actualAmount)
                                && expectedSpends[i].description().equals(actualDescription)
                                && expectedDate.equals(actualDate);
                    }
                    if (!passed) {
                        String message = String.format(
                                "List spends mismatch (expected: %s, actual: %s)", spends, actualSpends);
                        return rejected(message, actualSpends);
                    }
                }
                return accepted();
            }

            @NotNull
            @Override
            public String toString() {
                return spends;
            }
        };
    }
}