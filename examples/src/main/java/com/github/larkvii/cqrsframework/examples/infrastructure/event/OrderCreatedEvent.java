package com.github.larkvii.cqrsframework.examples.infrastructure.event;

import java.io.Serializable;

/**
 */
public interface OrderCreatedEvent extends Serializable {

    String getId();

}