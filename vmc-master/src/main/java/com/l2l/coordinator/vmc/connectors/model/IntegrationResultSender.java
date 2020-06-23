package com.l2l.coordinator.vmc.connectors.model;


import com.l2l.coordinator.vmc.connectors.annotatoionDomain.AnnotationIntegrationResultImpl;
import org.activiti.runtime.api.model.IntegrationResult;
import org.springframework.messaging.Message;

public interface IntegrationResultSender {
    void send(Message<AnnotationIntegrationResultImpl> var1);
}

