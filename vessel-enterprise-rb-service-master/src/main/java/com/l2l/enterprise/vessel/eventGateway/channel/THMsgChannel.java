package com.l2l.enterprise.vessel.eventGateway.channel;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface THMsgChannel {
    String Temperature_and_humidity = "temperatureAndHumidity";

    @Input("temperatureAndHumidity")
    SubscribableChannel temperatureAndHumidity();

}
