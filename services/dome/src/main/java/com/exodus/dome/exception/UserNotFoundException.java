package com.exodus.dome.exception;

import com.exodus.dome.entity.valueObject.ExceptionMessageParameter;
import java.util.List;

public class UserNotFoundException extends NotFoundException {
  public UserNotFoundException(String message) {
    super(message);
  }

  public UserNotFoundException(String message,
                               List<ExceptionMessageParameter> parameters) {
    super(message, parameters);
  }
}
