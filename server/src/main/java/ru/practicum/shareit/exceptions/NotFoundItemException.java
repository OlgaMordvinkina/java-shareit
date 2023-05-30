package ru.practicum.shareit.exceptions;

import java.util.NoSuchElementException;

public class NotFoundItemException extends NoSuchElementException {
    public NotFoundItemException(Long id) {
        super("Item с ID " + id + " нет.");
    }
}
