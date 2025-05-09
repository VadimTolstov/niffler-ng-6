package guru.qa.niffler.page.component;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.BasePage;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Selenide.$;

public class Calendar <T extends BasePage<?>> extends BaseComponent<T>{
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    public Calendar(SelenideElement self, T page) {
        super(self, page);
    }

    @Step("Выбор дату в календаре: {date}")
    public T selectDateInCalendar(@Nonnull LocalDate date){
        self.setValue(date.format(formatter));
        return getPage();
    }
}
