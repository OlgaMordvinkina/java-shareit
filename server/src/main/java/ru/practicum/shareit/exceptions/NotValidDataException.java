package ru.practicum.shareit.exceptions;

import javax.xml.bind.ValidationException;

public class NotValidDataException extends ValidationException {
    public NotValidDataException(String message) {
        super(message);
    }
}
