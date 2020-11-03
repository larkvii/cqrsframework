package com.github.larkvii.cqrsframework.commons.exception;

/**
 *
 */
public class AppTimeOutException extends AppAuthenticationException {

  public AppTimeOutException() {
    super();
  }

  public AppTimeOutException(String message) {
    super(message);
  }

  public AppTimeOutException(final String message, final Throwable throwable) {
    super(message, throwable);
  }

}