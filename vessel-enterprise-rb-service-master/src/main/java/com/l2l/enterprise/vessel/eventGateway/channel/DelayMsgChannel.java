package com.l2l.enterprise.vessel.eventGateway.channel;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface DelayMsgChannel {
    String DELAY_MSG = "delaymsgConsumer";

    String DELAY_SERVICE_CONFIRM = "delayServiceConfirm";

    String DELAY_DESTINATION_UPDATE = "delayDestinationUpdate";
    @Input("delayDestinationUpdate")
    SubscribableChannel delayDestinationUpdate();

    @Input(DELAY_MSG)
    SubscribableChannel delaymsgConsumer();

    @Output("delayServiceConfirm")
    MessageChannel delayServiceConfirm();


}
