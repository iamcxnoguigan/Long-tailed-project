package com.l2l.contextsharing.connectors.channels;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface AnnotationMessageChannels {
    String ANNOTATION_PRODUCER = "annotationProducer";
    String ANNOTAION_CONSUMER = "annotationConsumer";

    @Output(ANNOTATION_PRODUCER)
    MessageChannel annotationProducer();

    @Input(ANNOTAION_CONSUMER)
    SubscribableChannel annotationConsumer();
}
