package ru.practicum.shareit.handlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.exceptions.*;
import ru.practicum.shareit.exceptions.model.ErrorResponse;
import ru.practicum.shareit.exceptions.model.ErrorsDescription;

import javax.persistence.NonUniqueResultException;
import javax.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
@RequiredArgsConstructor
public class RestResponseEntityExceptionHandler {
    private final ObjectMapper objectMapper;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<Object> handleConflict(MethodArgumentNotValidException ex) throws JsonProcessingException {
        ErrorResponse response = ErrorResponse.builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .errors(
                        ex.getBindingResult().getFieldErrors().stream()
                                .map(it -> ErrorsDescription.builder()
                                        .fieldName(it.getField())
                                        .message(it.getDefaultMessage())
                                        .build())
                                .collect(Collectors.toList())
                ).build();

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(objectMapper.writeValueAsString(response));
    }

    @ExceptionHandler(NotFoundItemException.class)
    protected ResponseEntity<Object> handleConflict(NotFoundItemException ex) throws JsonProcessingException {
        ErrorResponse response = ErrorResponse.builder()
                .code(HttpStatus.NOT_FOUND.value())
                .errors(
                        List.of(ErrorsDescription.builder()
                                .message(ex.getMessage())
                                .build())
                ).build();

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .body(objectMapper.writeValueAsString(response));
    }

    @ExceptionHandler(NotFoundUserException.class)
    protected ResponseEntity<Object> handleConflict(NotFoundUserException ex) throws JsonProcessingException {
        ErrorResponse response = ErrorResponse.builder()
                .code(HttpStatus.NOT_FOUND.value())
                .errors(
                        List.of(ErrorsDescription.builder()
                                .message(ex.getMessage())
                                .build())
                ).build();

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .body(objectMapper.writeValueAsString(response));
    }

    @ExceptionHandler(NotFoundBookingException.class)
    protected ResponseEntity<Object> handleConflict(NotFoundBookingException ex) throws JsonProcessingException {
        ErrorResponse response = ErrorResponse.builder()
                .code(HttpStatus.NOT_FOUND.value())
                .errors(
                        List.of(ErrorsDescription.builder()
                                .message(ex.getMessage())
                                .build())
                ).build();

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .body(objectMapper.writeValueAsString(response));
    }

    @ExceptionHandler(NotFoundItemRequestException.class)
    protected ResponseEntity<Object> handleConflict(NotFoundItemRequestException ex) throws JsonProcessingException {
        ErrorResponse response = ErrorResponse.builder()
                .code(HttpStatus.NOT_FOUND.value())
                .errors(
                        List.of(ErrorsDescription.builder()
                                .message(ex.getMessage())
                                .build())
                ).build();

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .body(objectMapper.writeValueAsString(response));
    }

    @ExceptionHandler(RegisterException.class)
    protected ResponseEntity<Object> handleConflict(RegisterException ex) throws JsonProcessingException {
        ErrorResponse response = ErrorResponse.builder()
                .code(HttpStatus.CONFLICT.value())
                .errors(
                        List.of(ErrorsDescription.builder()
                                .message(ex.getMessage())
                                .build())
                ).build();

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .contentType(MediaType.APPLICATION_JSON)
                .body(objectMapper.writeValueAsString(response));
    }

    @ExceptionHandler(AccessException.class)
    protected ResponseEntity<Object> handleConflict(AccessException ex) throws JsonProcessingException {
        ErrorResponse response = ErrorResponse.builder()
                .code(HttpStatus.NOT_FOUND.value())
                .errors(
                        List.of(ErrorsDescription.builder()
                                .message(ex.getMessage())
                                .build())
                ).build();

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .body(objectMapper.writeValueAsString(response));
    }

    @ExceptionHandler(NullPointerException.class)
    protected ResponseEntity<Object> handleConflict(NullPointerException ex) throws JsonProcessingException {
        ErrorResponse response = ErrorResponse.builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .errors(
                        List.of(ErrorsDescription.builder()
                                .message("Не может быть null")
                                .build())
                ).build();

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(objectMapper.writeValueAsString(response));
    }

    @ExceptionHandler(NonUniqueResultException.class)
    protected ResponseEntity<Object> handleConflict(NonUniqueResultException ex) throws JsonProcessingException {
        ErrorResponse response = ErrorResponse.builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .errors(
                        List.of(ErrorsDescription.builder()
                                .message(ex.getMessage())
                                .build())
                ).build();

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(objectMapper.writeValueAsString(response));
    }

    @ExceptionHandler(NotValidDataException.class)
    protected ResponseEntity<Object> handleConflict(NotValidDataException ex) throws JsonProcessingException {
        Map<String, String> errors = new HashMap<>();
        errors.put("error", ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(objectMapper.writeValueAsString(errors));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<Object> handleConflict(ConstraintViolationException ex) throws JsonProcessingException {
        ErrorResponse response = ErrorResponse.builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .errors(
                        List.of(ErrorsDescription.builder()
                                .message(ex.getMessage())
                                .build())
                ).build();

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(objectMapper.writeValueAsString(response));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    protected ResponseEntity<Object> handleConflict(IllegalArgumentException ex) throws JsonProcessingException {
        ErrorResponse response = ErrorResponse.builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .errors(
                        List.of(ErrorsDescription.builder()
                                .message(ex.getMessage())
                                .build())
                ).build();

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(objectMapper.writeValueAsString(response));
    }
}
