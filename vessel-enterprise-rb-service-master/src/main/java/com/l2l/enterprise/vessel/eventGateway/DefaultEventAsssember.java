package com.l2l.enterprise.vessel.eventGateway;

import org.springframework.stereotype.Service;

@Service
public class DefaultEventAsssember<T>  implements EventAssember<T>{
    @Override
    public void handle(String topic , T event) {

    }
}
