package com.l2l.contextsharing.connectors.channels;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface ProcessRuntimeChannels {
    String RUNTIME_CMD_PRODUCER = "runtimeCmdProducer";
    String RUNTIME_CMD_RESULTS = "runtimeCmdResults";

    @Output("runtimeCmdProducer")
    MessageChannel runtimeCmdProducer();
    @Input("runtimeCmdResults")
    SubscribableChannel runtimeCmdResults();
}
