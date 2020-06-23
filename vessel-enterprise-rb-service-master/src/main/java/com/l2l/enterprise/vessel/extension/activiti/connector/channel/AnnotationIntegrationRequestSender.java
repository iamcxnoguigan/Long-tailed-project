package com.l2l.enterprise.vessel.extension.activiti.connector.channel;

import com.l2l.enterprise.vessel.extension.activiti.annotation.AnnotationIntegrationRequestImpl;
import org.activiti.cloud.services.events.configuration.RuntimeBundleProperties;
import org.activiti.cloud.services.events.converter.RuntimeBundleInfoAppender;
import org.activiti.runtime.api.event.impl.CloudIntegrationRequestedImpl;
import org.activiti.runtime.api.model.IntegrationRequest;
import org.springframework.cloud.stream.binding.BinderAwareChannelResolver;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class AnnotationIntegrationRequestSender {
    protected static final String CONNECTOR_TYPE = "connectorType";
    private final RuntimeBundleProperties runtimeBundleProperties;
    private final MessageChannel auditProducer;
    private final BinderAwareChannelResolver resolver;
    private final RuntimeBundleInfoAppender runtimeBundleInfoAppender;

    public AnnotationIntegrationRequestSender(RuntimeBundleProperties runtimeBundleProperties, MessageChannel auditProducer, BinderAwareChannelResolver resolver, RuntimeBundleInfoAppender runtimeBundleInfoAppender) {
        this.runtimeBundleProperties = runtimeBundleProperties;
        this.auditProducer = auditProducer;
        this.resolver = resolver;
        this.runtimeBundleInfoAppender = runtimeBundleInfoAppender;
    }

    @TransactionalEventListener(
        phase = TransactionPhase.AFTER_COMMIT
    )
    public void sendIntegrationRequest(AnnotationIntegrationRequestImpl event) {
        if(event instanceof AnnotationIntegrationRequestImpl){
            Message<AnnotationIntegrationRequestImpl> annotationIntegrationRequestMessage = MessageBuilder.withPayload(event).setHeader("connectorType", ((AnnotationIntegrationRequestImpl) event).getAnnotationIntergrationContext().getConnectorType()).build();
            this.resolver.resolveDestination(((AnnotationIntegrationRequestImpl) event).getAnnotationIntergrationContext().getConnectorType()).send(annotationIntegrationRequestMessage);
            this.sendAuditEvent(event);
        }else {
            this.resolver.resolveDestination(event.getIntegrationContext().getConnectorType()).send(this.buildIntegrationRequestMessage(event));
            this.sendAuditEvent(event);
        }
    }

    private void sendAuditEvent(IntegrationRequest integrationRequest) {
        if (this.runtimeBundleProperties.getEventsProperties().isIntegrationAuditEventsEnabled()) {
            CloudIntegrationRequestedImpl integrationRequested = new CloudIntegrationRequestedImpl(integrationRequest.getIntegrationContext());
            this.runtimeBundleInfoAppender.appendRuntimeBundleInfoTo(integrationRequested);
            this.auditProducer.send(MessageBuilder.withPayload(integrationRequested).build());
        }

    }

    private Message<IntegrationRequest> buildIntegrationRequestMessage(IntegrationRequest event) {
        return MessageBuilder.withPayload(event).setHeader("connectorType", event.getIntegrationContext().getConnectorType()).build();
    }
}
