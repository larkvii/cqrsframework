package com.github.dolphinai.cqrsframework.commons.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;

import java.io.Closeable;
import java.io.IOException;
import java.util.Locale;

/**
 * MessageSource bundle.
 */
public final class MessageBundle implements Closeable {

  private static final Logger log = LoggerFactory.getLogger(MessageBundle.class);
  private MessageSource messageSource;

  public MessageSource source() {
    if (messageSource == null) {
      messageSource = SpringContextUtils.getBean(MessageSource.class);
    }
    return messageSource;
  }

  public String getMessage(final String key, final Locale locale) {
    return getMessage(key, null, locale);
  }

  public String getMessage(final String key, final Object[] args, final Locale locale) {
    String result = "";
    try {
      result = source().getMessage(key, args, locale);
    } catch (NoSuchMessageException e) {
      if (locale != null) {
        try {
          result = source().getMessage(key, args, Locale.ENGLISH);
        } catch (Exception e1) {
          log.error("Not found the message key:" + key, e1);
        }
      }
    } catch (Exception e) {
      log.error("Failed to get message for the key:" + key, e);
    }
    return result;
  }

  @Override
  public void close() throws IOException {
    messageSource = null;
  }

  public static MessageBundle getInstance() {
    return MessageBundleHolder.instance;
  }

  private static class MessageBundleHolder {
    private static final MessageBundle instance = new MessageBundle();
  }
}