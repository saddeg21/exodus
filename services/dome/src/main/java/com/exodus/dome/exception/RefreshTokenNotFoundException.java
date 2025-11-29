package com.exodus.dome.exception;

public class RefreshTokenNotFoundException extends NotFoundException {
  public RefreshTokenNotFoundException(String message) {
    super(message);
  }

  public RefreshTokenNotFoundException(String message,
                                       java.util.List<com.exodus.dome.entity.valueObject.ExceptionMessageParameter> parameters) {
    super(message, parameters);
  }
}
