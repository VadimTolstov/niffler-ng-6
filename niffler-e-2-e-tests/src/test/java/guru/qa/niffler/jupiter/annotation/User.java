package guru.qa.niffler.jupiter.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface User {
    Spending[] spendings() default {};
    Category[] categories() default {};
    String username() default "";
    int income() default 0;
    int outcome() default 0;
    int friends() default 0;
}
