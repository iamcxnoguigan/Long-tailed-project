package com.l2l.enterprise.vessel.eventGateway.channel;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface AcquireProcessBusinessInformationMsgChannel {
    String APBI = "AcquireProcessBusinessInformation";

    @Input("AcquireProcessBusinessInformation")
    SubscribableChannel AcquireProcessBusinessInformation();
}
