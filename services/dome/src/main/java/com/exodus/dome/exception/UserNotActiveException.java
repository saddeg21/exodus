package com.exodus.dome.exception;

import com.exodus.dome.contract.CustomException;
import com.exodus.dome.entity.valueObject.ExceptionMessageParameter;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class UserNotActiveException extends RuntimeException implements CustomException {
  private String message;
  private List<ExceptionMessageParameter> parameters;

  public UserNotActiveException(String message) {
    this.message = message;
  }

  public UserNotActiveException(String message,
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
