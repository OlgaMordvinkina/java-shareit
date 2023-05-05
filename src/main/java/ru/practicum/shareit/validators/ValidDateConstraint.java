package ru.practicum.shareit.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ValidDateValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidDateConstraint {
    String message() default "Дата старта не может быть позже или равна дате окончания";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
