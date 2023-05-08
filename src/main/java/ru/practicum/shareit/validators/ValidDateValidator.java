package ru.practicum.shareit.validators;

import ru.practicum.shareit.booking.dto.BookingDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

public class ValidDateValidator implements ConstraintValidator<ValidDateConstraint, BookingDto> {
    private final String startDateValidationMessage = "Дата старта не может быть позже или равна дате окончания";

    @Override
    public boolean isValid(BookingDto booking, ConstraintValidatorContext cxt) {
        cxt.disableDefaultConstraintViolation();

        boolean result = true;
        if (booking.getStart().isAfter(booking.getEnd()) || Objects.equals(booking.getStart(), booking.getEnd())) {
            result = false;
            cxt.buildConstraintViolationWithTemplate(startDateValidationMessage)
                    .addPropertyNode("start")
                    .addConstraintViolation();
        }

        return result;
    }
}
