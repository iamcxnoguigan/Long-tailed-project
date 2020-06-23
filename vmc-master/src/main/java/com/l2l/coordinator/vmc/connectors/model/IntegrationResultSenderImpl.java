package com.l2l.coordinator.vmc.connectors.model;

import com.l2l.coordinator.vmc.connectors.annotatoionDomain.AnnotationIntegrationResultImpl;
import org.activiti.runtime.api.model.IntegrationResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.binding.BinderAwareChannelResolver;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Component
public class IntegrationResultSenderImpl implements IntegrationResultSender {
    @Value("${ACT_INT_RES_CONSUMER}")
    private String resultDestinationOverride;
    private final BinderAwareChannelResolver resolver;
    @Autowired
    public IntegrationResultSenderImpl(BinderAwareChannelResolver resolver) {
        this.resolver = resolver;
    }
    @Override
    public void send(Message<AnnotationIntegrationResultImpl> message) {
        String destination = this.resultDestinationOverride != null && !this.resultDestinationOverride.isEmpty() ? this.resultDestinationOverride : "integrationResult:" + ((AnnotationIntegrationResultImpl)message.getPayload()).getIntegrationRequest().getServiceFullName();
        this.resolver.resolveDestination(destination).send(message);
    }
}
