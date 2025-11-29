package com.exodus.dome.controller;

import com.exodus.dome.entity.valueObject.ExceptionMessageParameter;
import com.exodus.dome.entity.valueObject.ResponseMessage;
import com.exodus.dome.exception.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CustomExceptionControllerTest {

    private CustomExceptionController controller;

    @BeforeEach
    void setUp() {
        controller = new CustomExceptionController();
    }

    @Test
    void userNotActiveExceptionHandler_shouldReturnForbidden() {
        UserNotActiveException exception = new UserNotActiveException("User is not active");

        ResponseEntity<Object> response = controller.userNotActiveExceptionHandler(exception);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof ResponseMessage);
        assertEquals("User is not active", ((ResponseMessage) response.getBody()).getMessage());
    }

    @Test
    void userNotActiveExceptionHandler_withParameters_shouldReturnForbidden() {
        List<ExceptionMessageParameter> params = Arrays.asList(
                new ExceptionMessageParameter("userId", "12345"));
        UserNotActiveException exception = new UserNotActiveException("User is not active", params);

        ResponseEntity<Object> response = controller.userNotActiveExceptionHandler(exception);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertNotNull(response.getBody());
        String message = ((ResponseMessage) response.getBody()).getMessage();
        assertTrue(message.contains("User is not active"));
        assertTrue(message.contains("userId => 12345"));
    }

    @Test
    void authenticationRequiredExceptionHandler_shouldReturnUnauthorized() {
        AuthenticationRequiredException exception = new AuthenticationRequiredException("Authentication required");

        ResponseEntity<Object> response = controller.authenticationRequiredExceptionHandler(exception);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof ResponseMessage);
        assertEquals("Authentication required", ((ResponseMessage) response.getBody()).getMessage());
    }

    @Test
    void authenticationRequiredExceptionHandler_withParameters_shouldReturnUnauthorized() {
        List<ExceptionMessageParameter> params = Arrays.asList(
                new ExceptionMessageParameter("endpoint", "/api/secure"));
        AuthenticationRequiredException exception = new AuthenticationRequiredException("Authentication required",
                params);

        ResponseEntity<Object> response = controller.authenticationRequiredExceptionHandler(exception);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        String message = ((ResponseMessage) response.getBody()).getMessage();
        assertTrue(message.contains("Authentication required"));
        assertTrue(message.contains("endpoint => /api/secure"));
    }

    @Test
    void duplicateValueExceptionHandler_shouldReturnConflict() {
        DuplicateValueException exception = new DuplicateValueException("Email already exists");

        ResponseEntity<Object> response = controller.duplicateValueExceptionHandler(exception);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof ResponseMessage);
        assertEquals("Email already exists", ((ResponseMessage) response.getBody()).getMessage());
    }

    @Test
    void duplicateValueExceptionHandler_withParameters_shouldReturnConflict() {
        List<ExceptionMessageParameter> params = Arrays.asList(
                new ExceptionMessageParameter("field", "email"),
                new ExceptionMessageParameter("value", "test@test.com"));
        DuplicateValueException exception = new DuplicateValueException("Duplicate value", params);

        ResponseEntity<Object> response = controller.duplicateValueExceptionHandler(exception);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        String message = ((ResponseMessage) response.getBody()).getMessage();
        assertTrue(message.contains("Duplicate value"));
        assertTrue(message.contains("field => email"));
    }

    @Test
    void invalidRefreshTokenExceptionHandler_shouldReturnUnauthorized() {
        InvalidRefreshTokenException exception = new InvalidRefreshTokenException("Invalid refresh token");

        ResponseEntity<Object> response = controller.invalidRefreshTokenExceptionHandler(exception);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof ResponseMessage);
        assertEquals("Invalid refresh token", ((ResponseMessage) response.getBody()).getMessage());
    }

    @Test
    void invalidRefreshTokenExceptionHandler_withParameters_shouldReturnUnauthorized() {
        List<ExceptionMessageParameter> params = Arrays.asList(
                new ExceptionMessageParameter("reason", "revoked"));
        InvalidRefreshTokenException exception = new InvalidRefreshTokenException("Invalid refresh token", params);

        ResponseEntity<Object> response = controller.invalidRefreshTokenExceptionHandler(exception);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        String message = ((ResponseMessage) response.getBody()).getMessage();
        assertTrue(message.contains("Invalid refresh token"));
        assertTrue(message.contains("reason => revoked"));
    }

    @Test
    void notFoundExceptionHandler_shouldReturnNotFound() {
        NotFoundException exception = new NotFoundException("Resource not found");

        ResponseEntity<Object> response = controller.notFoundExceptionHandler(exception);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof ResponseMessage);
        assertEquals("Resource not found", ((ResponseMessage) response.getBody()).getMessage());
    }

    @Test
    void notFoundExceptionHandler_withParameters_shouldReturnNotFound() {
        List<ExceptionMessageParameter> params = Arrays.asList(
                new ExceptionMessageParameter("resourceId", "abc123"));
        NotFoundException exception = new NotFoundException("Resource not found", params);

        ResponseEntity<Object> response = controller.notFoundExceptionHandler(exception);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        String message = ((ResponseMessage) response.getBody()).getMessage();
        assertTrue(message.contains("Resource not found"));
        assertTrue(message.contains("resourceId => abc123"));
    }

    @Test
    void passwordNotCorrectExceptionHandler_shouldReturnUnauthorized() {
        PasswordNotCorrectException exception = new PasswordNotCorrectException("Password is incorrect");

        ResponseEntity<Object> response = controller.passwordNotCorrectExceptionHandler(exception);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof ResponseMessage);
        assertEquals("Password is incorrect", ((ResponseMessage) response.getBody()).getMessage());
    }

    @Test
    void passwordNotCorrectExceptionHandler_withParameters_shouldReturnUnauthorized() {
        List<ExceptionMessageParameter> params = Arrays.asList(
                new ExceptionMessageParameter("attempts", "3"));
        PasswordNotCorrectException exception = new PasswordNotCorrectException("Password is incorrect", params);

        ResponseEntity<Object> response = controller.passwordNotCorrectExceptionHandler(exception);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        String message = ((ResponseMessage) response.getBody()).getMessage();
        assertTrue(message.contains("Password is incorrect"));
        assertTrue(message.contains("attempts => 3"));
    }

    @Test
    void refreshTokenNotFoundExceptionHandler_shouldReturnNotFound() {
        RefreshTokenNotFoundException exception = new RefreshTokenNotFoundException("Refresh token not found");

        ResponseEntity<Object> response = controller.refreshTokenNotFoundExceptionHandler(exception);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof ResponseMessage);
        assertEquals("Refresh token not found", ((ResponseMessage) response.getBody()).getMessage());
    }

    @Test
    void refreshTokenNotFoundExceptionHandler_withParameters_shouldReturnNotFound() {
        List<ExceptionMessageParameter> params = Arrays.asList(
                new ExceptionMessageParameter("tokenId", "xyz789"));
        RefreshTokenNotFoundException exception = new RefreshTokenNotFoundException("Refresh token not found", params);

        ResponseEntity<Object> response = controller.refreshTokenNotFoundExceptionHandler(exception);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        String message = ((ResponseMessage) response.getBody()).getMessage();
        assertTrue(message.contains("Refresh token not found"));
        assertTrue(message.contains("tokenId => xyz789"));
    }

    @Test
    void tokenExpiredExceptionHandler_shouldReturnUnauthorized() {
        TokenExpiredException exception = new TokenExpiredException("Token has expired");

        ResponseEntity<Object> response = controller.tokenExpiredExceptionHandler(exception);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof ResponseMessage);
        assertEquals("Token has expired", ((ResponseMessage) response.getBody()).getMessage());
    }

    @Test
    void tokenExpiredExceptionHandler_withParameters_shouldReturnUnauthorized() {
        List<ExceptionMessageParameter> params = Arrays.asList(
                new ExceptionMessageParameter("expiredAt", "2024-01-01T00:00:00Z"));
        TokenExpiredException exception = new TokenExpiredException("Token has expired", params);

        ResponseEntity<Object> response = controller.tokenExpiredExceptionHandler(exception);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        String message = ((ResponseMessage) response.getBody()).getMessage();
        assertTrue(message.contains("Token has expired"));
        assertTrue(message.contains("expiredAt => 2024-01-01T00:00:00Z"));
    }

    @Test
    void userNotFoundExceptionHandler_shouldReturnNotFound() {
        UserNotFoundException exception = new UserNotFoundException("User not found");

        ResponseEntity<Object> response = controller.userNotFoundExceptionHandler(exception);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof ResponseMessage);
        assertEquals("User not found", ((ResponseMessage) response.getBody()).getMessage());
    }

    @Test
    void userNotFoundExceptionHandler_withParameters_shouldReturnNotFound() {
        List<ExceptionMessageParameter> params = Arrays.asList(
                new ExceptionMessageParameter("email", "user@example.com"));
        UserNotFoundException exception = new UserNotFoundException("User not found", params);

        ResponseEntity<Object> response = controller.userNotFoundExceptionHandler(exception);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        String message = ((ResponseMessage) response.getBody()).getMessage();
        assertTrue(message.contains("User not found"));
        assertTrue(message.contains("email => user@example.com"));
    }
}
