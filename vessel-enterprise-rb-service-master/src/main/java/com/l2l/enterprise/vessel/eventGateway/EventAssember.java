package com.l2l.enterprise.vessel.eventGateway;

public interface EventAssember<T> {
    void handle(String topic , T event);
}
