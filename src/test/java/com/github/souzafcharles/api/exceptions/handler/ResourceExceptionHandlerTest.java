package com.github.souzafcharles.api.exceptions.handler;

import com.github.souzafcharles.api.exceptions.custom.ResourceNotFoundException;
import com.github.souzafcharles.api.exceptions.model.StandardError;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ResourceExceptionHandlerTest {

    private ResourceExceptionHandler handler;

    @Mock
    private HttpServletRequest request;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        handler = new ResourceExceptionHandler();
    }

    @Test
    void handleNotFound_ShouldReturnStandardErrorWithNotFoundStatus() {

        when(request.getRequestURI()).thenReturn("/products/99");
        ResourceNotFoundException exception = new ResourceNotFoundException("99");

        ResponseEntity<StandardError> response = handler.handleNotFound(exception, request);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(404, response.getBody().getStatus());
        assertEquals("Resource not found with the specified identifier or criteria.", response.getBody().getError());
        assertTrue(response.getBody().getMessage().contains("99"));
        assertEquals("/products/99", response.getBody().getPath());
        assertNotNull(response.getBody().getTimestamp());
    }
}
