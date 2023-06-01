package ru.practicum.shareit.exceptions;

import java.util.NoSuchElementException;

public class NotFoundBookingException extends NoSuchElementException {
    public NotFoundBookingException(Long id) {
        super("Booking с ID " + id + " нет.");
    }
}
