package guru.qa.niffler.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;
import static org.assertj.core.api.Assertions.assertThat;

public class ProfilePage {
    private final SelenideElement username = $("input[name='username']"),
            archivedCheckbox = $("input[type='checkbox']");


    public void checkUsername(String currentUsername) {
        assertThat(username.getValue())
                .as("неверный username")
                .isEqualTo(currentUsername);
    }

    public ProfilePage clickShowArchivedCheckbox() {
        archivedCheckbox.click();
        return this;
    }

    public void checkCategoryArchived(boolean archived, String categoryName) {
        SelenideElement category = $x(String.format("//*[text()='%s']", categoryName));
        if (archived) {
            category.$x("../..//button[@aria-label='Unarchive category']").shouldHave(Condition.visible);
        } else {
            category.$x("../../..//button[@aria-label='Edit category']").shouldHave(Condition.visible);
            category.$x("../..//button[@aria-label='Archive category']").shouldHave(Condition.visible);
        }
    }
}
