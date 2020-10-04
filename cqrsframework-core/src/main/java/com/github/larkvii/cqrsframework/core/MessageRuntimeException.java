package com.github.larkvii.cqrsframework.core;

/**
 *
 */
public class MessageRuntimeException extends RuntimeException {

  public MessageRuntimeException(Throwable throwable) {
    super(throwable);
  }

  public MessageRuntimeException(String message, Throwable throwable) {
    super(message, throwable);
  }

}
