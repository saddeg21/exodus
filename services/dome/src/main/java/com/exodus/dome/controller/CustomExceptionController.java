package com.exodus.dome.controller;

import com.exodus.dome.contract.CustomException;
import com.exodus.dome.entity.valueObject.ResponseMessage;
import com.exodus.dome.exception.AuthenticationRequiredException;
import com.exodus.dome.exception.DuplicateValueException;
import com.exodus.dome.exception.InvalidRefreshTokenException;
import com.exodus.dome.exception.NotFoundException;
import com.exodus.dome.exception.PasswordNotCorrectException;
import com.exodus.dome.exception.RefreshTokenNotFoundException;
import com.exodus.dome.exception.TokenExpiredException;
import com.exodus.dome.exception.UserNotActiveException;
import com.exodus.dome.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CustomExceptionController {

  @ExceptionHandler({ UserNotActiveException.class })
  public ResponseEntity<Object> userNotActiveExceptionHandler(
      UserNotActiveException exception) {
    return handleException(exception, HttpStatus.FORBIDDEN);
  }

  @ExceptionHandler({ AuthenticationRequiredException.class })
  public ResponseEntity<Object> authenticationRequiredExceptionHandler(
      AuthenticationRequiredException exception) {
    return handleException(exception, HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler({ DuplicateValueException.class })
  public ResponseEntity<Object> duplicateValueExceptionHandler(
      DuplicateValueException exception) {
    return handleException(exception, HttpStatus.CONFLICT);
  }

  @ExceptionHandler({ InvalidRefreshTokenException.class })
  public ResponseEntity<Object> invalidRefreshTokenExceptionHandler(
      InvalidRefreshTokenException exception) {
    return handleException(exception, HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler({ NotFoundException.class })
  public ResponseEntity<Object> notFoundExceptionHandler(
      NotFoundException exception) {
    return handleException(exception, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler({ PasswordNotCorrectException.class })
  public ResponseEntity<Object> passwordNotCorrectExceptionHandler(
      PasswordNotCorrectException exception) {
    return handleException(exception, HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler({ RefreshTokenNotFoundException.class })
  public ResponseEntity<Object> refreshTokenNotFoundExceptionHandler(
      RefreshTokenNotFoundException exception) {
    return handleException(exception, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler({ TokenExpiredException.class })
  public ResponseEntity<Object> tokenExpiredExceptionHandler(
      TokenExpiredException exception) {
    return handleException(exception, HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler({ UserNotFoundException.class })
  public ResponseEntity<Object> userNotFoundExceptionHandler(
      UserNotFoundException exception) {
    return handleException(exception, HttpStatus.NOT_FOUND);
  }

  private ResponseEntity<Object> handleException(CustomException exception, HttpStatus httpStatus) {
    return ResponseEntity.status(httpStatus).body(new ResponseMessage(exception.getMessageWithParameters()));
  }
}
