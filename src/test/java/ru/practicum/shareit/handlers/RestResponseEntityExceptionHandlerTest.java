package ru.practicum.shareit.handlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import ru.practicum.shareit.exceptions.*;

import javax.persistence.NonUniqueResultException;
import javax.validation.ConstraintViolationException;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class RestResponseEntityExceptionHandlerTest {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final RestResponseEntityExceptionHandler handler = new RestResponseEntityExceptionHandler(objectMapper);
    private final Long id = 1L;
    private final int badRequest = 400;
    private final int notFound = 404;
    private final int conflict = 409;

    @Test
    public void NotFoundItemExceptionTest() throws JsonProcessingException {
        ResponseEntity<Object> response = handler.handleConflict(new NotFoundItemException(id));

        assertNotNull(response);
        assertEquals(response.getStatusCode().value(), notFound);
    }

    @Test
    public void NotFoundUserExceptionTest() throws JsonProcessingException {
        ResponseEntity<Object> response = handler.handleConflict(new NotFoundUserException(id));

        assertNotNull(response);
        assertEquals(response.getStatusCode().value(), notFound);
    }

    @Test
    public void NotFoundBookingExceptionTest() throws JsonProcessingException {
        ResponseEntity<Object> response = handler.handleConflict(new NotFoundBookingException(id));

        assertNotNull(response);
        assertEquals(response.getStatusCode().value(), notFound);
    }

    @Test
    public void NotFoundItemRequestExceptionTest() throws JsonProcessingException {
        ResponseEntity<Object> response = handler.handleConflict(new NotFoundItemRequestException(id));

        assertNotNull(response);
        assertEquals(response.getStatusCode().value(), notFound);
    }

    @Test
    public void RegisterExceptionTest() throws JsonProcessingException {
        ResponseEntity<Object> response = handler.handleConflict(new RegisterException(""));

        assertNotNull(response);
        assertEquals(response.getStatusCode().value(), conflict);
    }

    @Test
    public void AccessExceptionTest() throws JsonProcessingException {
        ResponseEntity<Object> response = handler.handleConflict(new AccessException(""));

        assertNotNull(response);
        assertEquals(response.getStatusCode().value(), notFound);
    }

    @Test
    public void NullPointerExceptionTest() throws JsonProcessingException {
        ResponseEntity<Object> response = handler.handleConflict(new NullPointerException());

        assertNotNull(response);
        assertEquals(response.getStatusCode().value(), badRequest);
    }

    @Test
    public void NonUniqueResultExceptionTest() throws JsonProcessingException {
        ResponseEntity<Object> response = handler.handleConflict(new NonUniqueResultException());

        assertNotNull(response);
        assertEquals(response.getStatusCode().value(), badRequest);
    }

    @Test
    public void NotValidDataExceptionTest() throws JsonProcessingException {
        ResponseEntity<Object> response = handler.handleConflict(new NotValidDataException(""));

        assertNotNull(response);
        assertEquals(response.getStatusCode().value(), badRequest);
    }

    @Test
    public void ConstraintViolationExceptionTest() throws JsonProcessingException {
        ResponseEntity<Object> response = handler.handleConflict(new ConstraintViolationException(new HashSet<>()));

        assertNotNull(response);
        assertEquals(response.getStatusCode().value(), badRequest);
    }

    @Test
    public void IllegalArgumentExceptionTest() throws JsonProcessingException {
        ResponseEntity<Object> response = handler.handleConflict(new IllegalArgumentException());

        assertNotNull(response);
        assertEquals(response.getStatusCode().value(), badRequest);
    }
}
