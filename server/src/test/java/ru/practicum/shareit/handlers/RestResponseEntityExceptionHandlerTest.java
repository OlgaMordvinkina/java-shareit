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
    public void notFoundItemExceptionTest() throws JsonProcessingException {
        ResponseEntity<Object> response = handler.handleConflict(new NotFoundItemException(id));

        assertNotNull(response);
        assertEquals(response.getStatusCode().value(), notFound);
    }

    @Test
    public void notFoundUserExceptionTest() throws JsonProcessingException {
        ResponseEntity<Object> response = handler.handleConflict(new NotFoundUserException(id));

        assertNotNull(response);
        assertEquals(response.getStatusCode().value(), notFound);
    }

    @Test
    public void notFoundBookingExceptionTest() throws JsonProcessingException {
        ResponseEntity<Object> response = handler.handleConflict(new NotFoundBookingException(id));

        assertNotNull(response);
        assertEquals(response.getStatusCode().value(), notFound);
    }

    @Test
    public void notFoundItemRequestExceptionTest() throws JsonProcessingException {
        ResponseEntity<Object> response = handler.handleConflict(new NotFoundItemRequestException(id));

        assertNotNull(response);
        assertEquals(response.getStatusCode().value(), notFound);
    }

    @Test
    public void registerExceptionTest() throws JsonProcessingException {
        ResponseEntity<Object> response = handler.handleConflict(new RegisterException(""));

        assertNotNull(response);
        assertEquals(response.getStatusCode().value(), conflict);
    }

    @Test
    public void accessExceptionTest() throws JsonProcessingException {
        ResponseEntity<Object> response = handler.handleConflict(new AccessException(""));

        assertNotNull(response);
        assertEquals(response.getStatusCode().value(), notFound);
    }

    @Test
    public void nullPointerExceptionTest() throws JsonProcessingException {
        ResponseEntity<Object> response = handler.handleConflict(new NullPointerException());

        assertNotNull(response);
        assertEquals(response.getStatusCode().value(), badRequest);
    }

    @Test
    public void nonUniqueResultExceptionTest() throws JsonProcessingException {
        ResponseEntity<Object> response = handler.handleConflict(new NonUniqueResultException());

        assertNotNull(response);
        assertEquals(response.getStatusCode().value(), badRequest);
    }

    @Test
    public void notValidDataExceptionTest() throws JsonProcessingException {
        ResponseEntity<Object> response = handler.handleConflict(new NotValidDataException(""));

        assertNotNull(response);
        assertEquals(response.getStatusCode().value(), badRequest);
    }

    @Test
    public void constraintViolationExceptionTest() throws JsonProcessingException {
        ResponseEntity<Object> response = handler.handleConflict(new ConstraintViolationException(new HashSet<>()));

        assertNotNull(response);
        assertEquals(response.getStatusCode().value(), badRequest);
    }

    @Test
    public void illegalArgumentExceptionTest() throws JsonProcessingException {
        ResponseEntity<Object> response = handler.handleConflict(new IllegalArgumentException());

        assertNotNull(response);
        assertEquals(response.getStatusCode().value(), badRequest);
    }
}
