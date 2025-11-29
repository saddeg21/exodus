package com.exodus.dome.exception;

import com.exodus.dome.contract.CustomException;
import com.exodus.dome.entity.valueObject.ExceptionMessageParameter;
import java.util.List;
import java.util.stream.Collectors;

public class PasswordNotCorrectException extends RuntimeException implements CustomException {
  private String message;
  private List<ExceptionMessageParameter> parameters;

  public PasswordNotCorrectException(String message) {
    this.message = message;
  }

  public PasswordNotCorrectException(String message,
                                         List<ExceptionMessageParameter> parameters) {
    this.message = message;
    this.parameters = parameters;
  }

  public String getMessageWithParameters() {
    if (parameters == null || parameters.isEmpty()) {
      return message;
    }

    return parameters.stream().map(
        exceptionMessageParameter -> " || " + exceptionMessageParameter.getKey() + " => " +
            exceptionMessageParameter.getValue()).collect(Collectors.joining("", message, ""));
  }

  public String getMessage() {
    return message;
  }
}
