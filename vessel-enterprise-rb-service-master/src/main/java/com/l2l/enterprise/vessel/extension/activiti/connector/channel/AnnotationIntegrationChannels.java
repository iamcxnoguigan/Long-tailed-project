package com.l2l.enterprise.vessel.extension.activiti.connector.channel;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface AnnotationIntegrationChannels {
    String INTEGRATION_RESULTS_CONSUMER = "AnnotationIntegrationResultsConsumer";
    String D = "delaymsgConsumer";
    String F = "delayDestinationUpdate";
    @Input("AnnotationIntegrationResultsConsumer")
    SubscribableChannel AnnotationIntegrationResultsConsumer();
//    @Input("delaymsgConsumer")
//    SubscribableChannel delaymsgConsumer();
//    @Input("delayDestinationUpdate")
//    SubscribableChannel delayDestinationUpdate();
}
