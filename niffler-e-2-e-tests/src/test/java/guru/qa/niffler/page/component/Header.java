package guru.qa.niffler.page.component;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.*;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;

import static com.codeborne.selenide.Selectors.byTagAndText;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class Header extends BaseComponent<Header>{

    public Header() {
        super($("#root header"));
    }

    private final SelenideElement mainPageLink = self.$("a[href*='/main']");
    private final SelenideElement addSpendingBtn = self.$("a[href*='/spending']");
    private final SelenideElement menuBtn = self.$("button");
    private final SelenideElement menu = $("ul[role='menu']");
    private final ElementsCollection menuItems = menu.$$("li");

    @Step("Перейти на страницу профиля")
    @Nonnull
    public ProfilePage toProfilePage() {
       menuBtn.click();
        menuItems.findBy(Condition.text("Profile")).click();
        return new ProfilePage();
    }

    @Step("Перейти на страницу друзей")
    @Nonnull
    public FriendsPage toFriendsPage() {
        menuBtn.click();
        menuItems.findBy(Condition.text("Friends")).click();
        return new FriendsPage();
    }

    @Step("Перейти на страницу всех людей")
    @Nonnull
    public PeoplePage toAllPeoplePage() {
        menuBtn.click();
        menuItems.findBy(Condition.text("All People")).click();
        return new PeoplePage();
    }

    @Step("Выйти из аккаунта")
    @Nonnull
    public LoginPage signOut() {
        menuBtn.click();
        menuItems.findBy(Condition.text("Sign out")).click();
        return new LoginPage();
    }

    @Step("Добавить новую трату")
    @Nonnull
    public EditSpendingPage addSpendingPage() {
        addSpendingBtn.click();
        return new EditSpendingPage();
    }

    @Step("Перейти на главную страницу")
    @Nonnull
    public MainPage toMainPage() {
        mainPageLink.click();
        return new MainPage();
    }
}
