package ru.practicum.shareit.exceptions;

import java.util.NoSuchElementException;

public class NotFoundItemRequestException extends NoSuchElementException {
    public NotFoundItemRequestException(Long id) {
        super("ItemRequest с ID " + id + " нет.");
    }
}
