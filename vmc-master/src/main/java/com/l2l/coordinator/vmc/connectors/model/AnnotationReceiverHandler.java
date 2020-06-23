package com.l2l.coordinator.vmc.connectors.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.l2l.coordinator.vmc.connectors.annotatoionDomain.AnnotationIntegrationRequestImpl;
import com.l2l.coordinator.vmc.connectors.annotatoionDomain.AnnotationIntegrationResultImpl;
import com.l2l.coordinator.vmc.connectors.channels.AnnotationMessageChannels;
import com.l2l.coordinator.vmc.connectors.configuration.ConnectorProperties;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@EnableBinding(AnnotationMessageChannels.class)
public class AnnotationReceiverHandler {
    private final IntegrationResultSender integrationResultSender;
    private static org.slf4j.Logger log = LoggerFactory.getLogger(AnnotationReceiverHandler.class);
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private ConnectorProperties connectorProperties;

    public AnnotationReceiverHandler(IntegrationResultSender integrationResultSender) {
        this.integrationResultSender = integrationResultSender;
    }

    @StreamListener(value = AnnotationMessageChannels.ANNOTAION_CONSUMER)
    public void rewardTopRankedUsers(AnnotationIntegrationRequestImpl event) {
        // business logic goes here
        AnnotationIntegrationRequestImpl tv = null;
        if(event instanceof AnnotationIntegrationRequestImpl) {
            tv = (AnnotationIntegrationRequestImpl)event;
            log.debug(tv.getAppName());
        }
        //  build and send result back
        Map<String, Object> results = new HashMap<>();
        results.put("rewards", "test");
        Message<AnnotationIntegrationResultImpl> message = AnnotationIntegrationResultBuilder.resultFor(event,
            connectorProperties)
            .withOutboundVariables(results)
            .buildMessage();
        integrationResultSender.send(message);
    }
}
