package com.l2l.contextsharing.connectors.model;


import com.l2l.contextsharing.connectors.annotatoionDomain.AnnotationIntegrationResultImpl;
import org.springframework.messaging.Message;

public interface IntegrationResultSender {
    void send(Message<AnnotationIntegrationResultImpl> var1);
}

