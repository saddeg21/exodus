package com.exodus.dome.exception;

import com.exodus.dome.entity.valueObject.ExceptionMessageParameter;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ExceptionTests {

    // ==================== NotFoundException Tests ====================

    @Test
    void notFoundException_withMessage_shouldReturnMessage() {
        NotFoundException exception = new NotFoundException("Resource not found");
        assertEquals("Resource not found", exception.getMessage());
        assertEquals("Resource not found", exception.getMessageWithParameters());
    }

    @Test
    void notFoundException_withMessageAndParameters_shouldReturnFormattedMessage() {
        List<ExceptionMessageParameter> params = Arrays.asList(
                new ExceptionMessageParameter("id", "123"),
                new ExceptionMessageParameter("type", "user"));
        NotFoundException exception = new NotFoundException("Resource not found", params);

        assertEquals("Resource not found", exception.getMessage());
        String result = exception.getMessageWithParameters();
        assertTrue(result.contains("Resource not found"));
        assertTrue(result.contains("id => 123"));
        assertTrue(result.contains("type => user"));
    }

    @Test
    void notFoundException_withEmptyParameters_shouldReturnMessage() {
        NotFoundException exception = new NotFoundException("Resource not found", Collections.emptyList());
        assertEquals("Resource not found", exception.getMessageWithParameters());
    }

    @Test
    void notFoundException_withNullParameters_shouldReturnMessage() {
        NotFoundException exception = new NotFoundException("Resource not found", null);
        assertEquals("Resource not found", exception.getMessageWithParameters());
    }

    // ==================== UserNotFoundException Tests ====================

    @Test
    void userNotFoundException_withMessage_shouldReturnMessage() {
        UserNotFoundException exception = new UserNotFoundException("User not found");
        assertEquals("User not found", exception.getMessage());
        assertEquals("User not found", exception.getMessageWithParameters());
    }

    @Test
    void userNotFoundException_withMessageAndParameters_shouldReturnFormattedMessage() {
        List<ExceptionMessageParameter> params = Arrays.asList(
                new ExceptionMessageParameter("email", "test@test.com"));
        UserNotFoundException exception = new UserNotFoundException("User not found", params);

        String result = exception.getMessageWithParameters();
        assertTrue(result.contains("User not found"));
        assertTrue(result.contains("email => test@test.com"));
    }

    // ==================== RefreshTokenNotFoundException Tests ====================

    @Test
    void refreshTokenNotFoundException_withMessage_shouldReturnMessage() {
        RefreshTokenNotFoundException exception = new RefreshTokenNotFoundException("Token not found");
        assertEquals("Token not found", exception.getMessage());
        assertEquals("Token not found", exception.getMessageWithParameters());
    }

    @Test
    void refreshTokenNotFoundException_withMessageAndParameters_shouldReturnFormattedMessage() {
        List<ExceptionMessageParameter> params = Arrays.asList(
                new ExceptionMessageParameter("tokenId", "abc123"));
        RefreshTokenNotFoundException exception = new RefreshTokenNotFoundException("Token not found", params);

        String result = exception.getMessageWithParameters();
        assertTrue(result.contains("Token not found"));
        assertTrue(result.contains("tokenId => abc123"));
    }

    // ==================== DuplicateValueException Tests ====================

    @Test
    void duplicateValueException_withMessage_shouldReturnMessage() {
        DuplicateValueException exception = new DuplicateValueException("Duplicate value");
        assertEquals("Duplicate value", exception.getMessage());
        assertEquals("Duplicate value", exception.getMessageWithParameters());
    }

    @Test
    void duplicateValueException_withMessageAndParameters_shouldReturnFormattedMessage() {
        List<ExceptionMessageParameter> params = Arrays.asList(
                new ExceptionMessageParameter("field", "email"),
                new ExceptionMessageParameter("value", "test@test.com"));
        DuplicateValueException exception = new DuplicateValueException("Duplicate value", params);

        String result = exception.getMessageWithParameters();
        assertTrue(result.contains("Duplicate value"));
        assertTrue(result.contains("field => email"));
        assertTrue(result.contains("value => test@test.com"));
    }

    @Test
    void duplicateValueException_withEmptyParameters_shouldReturnMessage() {
        DuplicateValueException exception = new DuplicateValueException("Duplicate value", Collections.emptyList());
        assertEquals("Duplicate value", exception.getMessageWithParameters());
    }

    @Test
    void duplicateValueException_withNullParameters_shouldReturnMessage() {
        DuplicateValueException exception = new DuplicateValueException("Duplicate value", null);
        assertEquals("Duplicate value", exception.getMessageWithParameters());
    }

    // ==================== UserNotActiveException Tests ====================

    @Test
    void userNotActiveException_withMessage_shouldReturnMessage() {
        UserNotActiveException exception = new UserNotActiveException("User not active");
        assertEquals("User not active", exception.getMessage());
        assertEquals("User not active", exception.getMessageWithParameters());
    }

    @Test
    void userNotActiveException_withMessageAndParameters_shouldReturnFormattedMessage() {
        List<ExceptionMessageParameter> params = Arrays.asList(
                new ExceptionMessageParameter("userId", "12345"));
        UserNotActiveException exception = new UserNotActiveException("User not active", params);

        String result = exception.getMessageWithParameters();
        assertTrue(result.contains("User not active"));
        assertTrue(result.contains("userId => 12345"));
    }

    @Test
    void userNotActiveException_withEmptyParameters_shouldReturnMessage() {
        UserNotActiveException exception = new UserNotActiveException("User not active", Collections.emptyList());
        assertEquals("User not active", exception.getMessageWithParameters());
    }

    @Test
    void userNotActiveException_withNullParameters_shouldReturnMessage() {
        UserNotActiveException exception = new UserNotActiveException("User not active", null);
        assertEquals("User not active", exception.getMessageWithParameters());
    }

    // ==================== PasswordNotCorrectException Tests ====================

    @Test
    void passwordNotCorrectException_withMessage_shouldReturnMessage() {
        PasswordNotCorrectException exception = new PasswordNotCorrectException("Wrong password");
        assertEquals("Wrong password", exception.getMessage());
        assertEquals("Wrong password", exception.getMessageWithParameters());
    }

    @Test
    void passwordNotCorrectException_withMessageAndParameters_shouldReturnFormattedMessage() {
        List<ExceptionMessageParameter> params = Arrays.asList(
                new ExceptionMessageParameter("attempts", "3"));
        PasswordNotCorrectException exception = new PasswordNotCorrectException("Wrong password", params);

        String result = exception.getMessageWithParameters();
        assertTrue(result.contains("Wrong password"));
        assertTrue(result.contains("attempts => 3"));
    }

    @Test
    void passwordNotCorrectException_withEmptyParameters_shouldReturnMessage() {
        PasswordNotCorrectException exception = new PasswordNotCorrectException("Wrong password",
                Collections.emptyList());
        assertEquals("Wrong password", exception.getMessageWithParameters());
    }

    @Test
    void passwordNotCorrectException_withNullParameters_shouldReturnMessage() {
        PasswordNotCorrectException exception = new PasswordNotCorrectException("Wrong password", null);
        assertEquals("Wrong password", exception.getMessageWithParameters());
    }

    // ==================== InvalidRefreshTokenException Tests ====================

    @Test
    void invalidRefreshTokenException_withMessage_shouldReturnMessage() {
        InvalidRefreshTokenException exception = new InvalidRefreshTokenException("Invalid token");
        assertEquals("Invalid token", exception.getMessage());
        assertEquals("Invalid token", exception.getMessageWithParameters());
    }

    @Test
    void invalidRefreshTokenException_withMessageAndParameters_shouldReturnFormattedMessage() {
        List<ExceptionMessageParameter> params = Arrays.asList(
                new ExceptionMessageParameter("reason", "expired"));
        InvalidRefreshTokenException exception = new InvalidRefreshTokenException("Invalid token", params);

        String result = exception.getMessageWithParameters();
        assertTrue(result.contains("Invalid token"));
        assertTrue(result.contains("reason => expired"));
    }

    @Test
    void invalidRefreshTokenException_withEmptyParameters_shouldReturnMessage() {
        InvalidRefreshTokenException exception = new InvalidRefreshTokenException("Invalid token",
                Collections.emptyList());
        assertEquals("Invalid token", exception.getMessageWithParameters());
    }

    @Test
    void invalidRefreshTokenException_withNullParameters_shouldReturnMessage() {
        InvalidRefreshTokenException exception = new InvalidRefreshTokenException("Invalid token", null);
        assertEquals("Invalid token", exception.getMessageWithParameters());
    }

    // ==================== TokenExpiredException Tests ====================

    @Test
    void tokenExpiredException_withMessage_shouldReturnMessage() {
        TokenExpiredException exception = new TokenExpiredException("Token expired");
        assertEquals("Token expired", exception.getMessage());
        assertEquals("Token expired", exception.getMessageWithParameters());
    }

    @Test
    void tokenExpiredException_withMessageAndParameters_shouldReturnFormattedMessage() {
        List<ExceptionMessageParameter> params = Arrays.asList(
                new ExceptionMessageParameter("expiredAt", "2024-01-01"));
        TokenExpiredException exception = new TokenExpiredException("Token expired", params);

        String result = exception.getMessageWithParameters();
        assertTrue(result.contains("Token expired"));
        assertTrue(result.contains("expiredAt => 2024-01-01"));
    }

    @Test
    void tokenExpiredException_withEmptyParameters_shouldReturnMessage() {
        TokenExpiredException exception = new TokenExpiredException("Token expired", Collections.emptyList());
        assertEquals("Token expired", exception.getMessageWithParameters());
    }

    @Test
    void tokenExpiredException_withNullParameters_shouldReturnMessage() {
        TokenExpiredException exception = new TokenExpiredException("Token expired", null);
        assertEquals("Token expired", exception.getMessageWithParameters());
    }

    // ==================== AuthenticationRequiredException Tests
    // ====================

    @Test
    void authenticationRequiredException_withMessage_shouldReturnMessage() {
        AuthenticationRequiredException exception = new AuthenticationRequiredException("Auth required");
        assertEquals("Auth required", exception.getMessage());
        assertEquals("Auth required", exception.getMessageWithParameters());
    }

    @Test
    void authenticationRequiredException_withMessageAndParameters_shouldReturnFormattedMessage() {
        List<ExceptionMessageParameter> params = Arrays.asList(
                new ExceptionMessageParameter("endpoint", "/api/protected"));
        AuthenticationRequiredException exception = new AuthenticationRequiredException("Auth required", params);

        String result = exception.getMessageWithParameters();
        assertTrue(result.contains("Auth required"));
        assertTrue(result.contains("endpoint => /api/protected"));
    }

    @Test
    void authenticationRequiredException_withEmptyParameters_shouldReturnMessage() {
        AuthenticationRequiredException exception = new AuthenticationRequiredException("Auth required",
                Collections.emptyList());
        assertEquals("Auth required", exception.getMessageWithParameters());
    }

    @Test
    void authenticationRequiredException_withNullParameters_shouldReturnMessage() {
        AuthenticationRequiredException exception = new AuthenticationRequiredException("Auth required", null);
        assertEquals("Auth required", exception.getMessageWithParameters());
    }
}
