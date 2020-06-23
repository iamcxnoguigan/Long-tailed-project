package com.l2l.enterprise.vessel.web.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.MessageChannel;


@EnableBinding(value = {SenderTest.SinkSender.class})
public class SenderTest {
    @Autowired
    private SenderTest.SinkSender sinkSender;

    public void sinkSenderTester() {
        sinkSender.output().send(MessageBuilder.withPayload("produce a message to " + Sink.INPUT + " channel").build());
    }

    public interface SinkSender {
        @Output(Sink.INPUT)
        MessageChannel output();
    }
}
