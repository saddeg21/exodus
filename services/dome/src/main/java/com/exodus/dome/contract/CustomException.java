package com.exodus.dome.contract;

public interface CustomException {
  String getMessageWithParameters();

  String getMessage();

  StackTraceElement[] getStackTrace();
}
