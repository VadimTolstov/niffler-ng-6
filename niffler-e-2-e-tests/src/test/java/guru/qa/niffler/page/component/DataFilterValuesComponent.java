package guru.qa.niffler.page.component;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum DataFilterValuesComponent {
    TODAY("Today"), WEEK("last week"), MONTH("Last month");
    public final String text;
}
