package com.exodus.dome.exception;

import com.exodus.dome.contract.CustomException;
import com.exodus.dome.entity.valueObject.ExceptionMessageParameter;
import java.util.List;
import java.util.stream.Collectors;

public class NotFoundException extends RuntimeException implements CustomException {

  private final String message;
  private List<ExceptionMessageParameter> parameters;

  public NotFoundException(String message) {
    super(message);
    this.message = message;
  }

  public NotFoundException(String message, List<ExceptionMessageParameter> parameters) {
    this.message = message;
    this.parameters = parameters;
  }

  public String getMessageWithParameters() {
    if (parameters == null || parameters.isEmpty()) {
      return message;
    }

    return parameters
        .stream()
        .map(exceptionMessageParameter -> " || " + exceptionMessageParameter.getKey() + " => " +
            exceptionMessageParameter.getValue()).collect(
            Collectors.joining("", message, ""));
  }

  public String getMessage() {
    return message;
  }
}
