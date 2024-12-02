package guru.qa.niffler.page.component;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.condition.Bubble;
import guru.qa.niffler.condition.Color;
import guru.qa.niffler.condition.StatConditions;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

import static com.codeborne.selenide.Selenide.$;
import static guru.qa.niffler.condition.StatConditions.statBubblesContains;

public class StatComponent extends BaseComponent<StatComponent> {
    public StatComponent() {
        super($("#stat"));
    }

    private final ElementsCollection bubble = self.$("#legend-container").$$("li");
    private final SelenideElement chart = $("canvas[role='img']");


    @Step("Check that stat bubbles contain {expectedBubbles}")
    @Nonnull
    public StatComponent checkBubbles(Bubble... expectedBubbles) {
        bubble.should(statBubblesContains(expectedBubbles));
        return this;
    }

    @Nonnull
    public BufferedImage chartScreenshot() throws IOException {
        return ImageIO.read(Objects.requireNonNull(chart.screenshot()));
    }

    public StatComponent checkBubbles(Color... expectedColors) {
        bubble.should(StatConditions.color(expectedColors));
        return this;
    }
}
