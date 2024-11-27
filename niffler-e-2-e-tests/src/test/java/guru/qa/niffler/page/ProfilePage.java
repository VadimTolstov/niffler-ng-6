package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.utils.ScreenDiffResult;
import io.qameta.allure.Step;
import org.springframework.core.io.ClassPathResource;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Objects;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byTagAndText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static org.junit.jupiter.api.Assertions.assertFalse;

@ParametersAreNonnullByDefault
public class ProfilePage extends BasePage<ProfilePage> {
    public static final String URL = CFG.frontUrl() + "profile";

    private final SelenideElement avatar = $("#image__input").parent().$("img");
    private final SelenideElement userName = $("#username");
    private final SelenideElement nameInput = $("#name");
    private final SelenideElement photoInput = $("input[type='file']");
    private final SelenideElement submitButton = $("button[type='submit']");
    private final SelenideElement categoryInput = $("input[name='category']");
    private final SelenideElement archivedSwitcher = $(".MuiSwitch-input");

    private final ElementsCollection bubbles = $$(".MuiChip-filled.MuiChip-colorPrimary");
    private final ElementsCollection bubblesArchived = $$(".MuiChip-filled.MuiChip-colorDefault");

//    private final SelenideElement showArchivedToggle = $(".MuiSwitch-switchBase");
//    private final SelenideElement nameInput = $("#name");
//    private final SelenideElement saveButton = $("button[type='submit']");
//    private final SelenideElement successSaveChangesMessage = $(".MuiAlert-message");

    @Step("Проверить отображение страницы профиля")
    @Override
    @Nonnull
    public ProfilePage checkThatPageLoaded() {
        userName.should(visible);
        return this;
    }

    @Step("Ввести имя: {name}")
    @Nonnull
    public ProfilePage setName(String name) {
        nameInput.setValue(name);
        return this;
    }

    @Step("Загрузить фото из classpath")
    @Nonnull
    public ProfilePage uploadPhotoFromClasspath(String path) {
        photoInput.uploadFromClasspath(path);
        return this;
    }

    @Step("Создать категорию: {0}")
    @Nonnull
    public ProfilePage addCategory(String category) {
        categoryInput.setValue(category).pressEnter();
        return this;
    }

    @Step("Проверить отображение категории: {category}")
    @Nonnull
    public ProfilePage checkCategoryExists(String category) {
        bubbles.find(text(category)).shouldBe(visible);
        return this;
    }

    @Step("Проверить отсутствие категории: {category}")
    @Nonnull
    public ProfilePage categoryShouldNotBeVisible(String category) {
        $(byTagAndText("span", category)).shouldNotBe(visible);
        return this;
    }

    @Step("Проверить архивную категорию: {0}")
    @Nonnull
    public ProfilePage checkArchivedCategoryExists(String category) {
        archivedSwitcher.click();
        bubblesArchived.find(text(category)).shouldBe(visible);
        return this;
    }

    @Step("Показать архивные категории")
    public ProfilePage showArchivedCategories() {
        archivedSwitcher.click();
        return this;
    }

    @Step("Проверить userName: {0}")
    @Nonnull
    public ProfilePage checkUsername(String username) {
        userName.should(value(username));
        return this;
    }

    @Step("Проверить name: {0}")
    @Nonnull
    public ProfilePage checkName(String name) {
        nameInput.shouldHave(value(name));
        return this;
    }

    @Step("Проверить фото")
    @Nonnull
    public ProfilePage checkPhoto(String path) throws IOException {
        final byte[] photoContent;
        try (InputStream is = new ClassPathResource(path).getInputStream()) {
            photoContent = Base64.getEncoder().encode(is.readAllBytes());
        }
        avatar.should(attribute("src", new String(photoContent, StandardCharsets.UTF_8)));
        return this;
    }

    @Step("Проверьте, существует ли фотография")
    @Nonnull
    public ProfilePage checkPhotoExist() {
        avatar.should(attributeMatching("src", "data:image.*"));
        return this;
    }

    @Step("Убедитесь, что ввод категории отключен")
    @Nonnull
    public ProfilePage checkThatCategoryInputDisabled() {
        categoryInput.should(disabled);
        return this;
    }

    @Step("Нажать кнопку сохранить изменения профиля")
    @Nonnull
    public ProfilePage submitProfile() {
        submitButton.click();
        return this;
    }

    @Step("Проверьте, соответствует ли изображение аватара ожидаемому изображению")
    public ProfilePage checkAvatarImg(BufferedImage expectedAvatar) throws IOException {
        BufferedImage actual = ImageIO.read(Objects.requireNonNull($(".MuiAvatar-img").screenshot()));
        assertFalse(new ScreenDiffResult(
                actual,
                expectedAvatar
        ));
        return this;
    }
}
