package ru.practicum.shareit.exceptions;

import java.util.NoSuchElementException;

public class NotFoundUserException extends NoSuchElementException {
    public NotFoundUserException(Long id) {
        super("User с ID " + id + " нет.");
    }
}
