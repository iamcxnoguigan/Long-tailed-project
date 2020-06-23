package com.l2l.contextsharing.connectors.annotatoionDomain;


import org.activiti.runtime.api.model.IntegrationContext;
import org.activiti.runtime.api.model.IntegrationRequest;
import org.activiti.runtime.api.model.IntegrationResult;
import org.activiti.runtime.api.model.impl.CloudRuntimeEntityImpl;
import org.springframework.beans.factory.annotation.Autowired;
import com.l2l.contextsharing.aws.bean.*;

public class AnnotationIntegrationResultImpl extends CloudRuntimeEntityImpl implements IntegrationResult {
    private AnnotationIntegrationRequestImpl annotationIntegrationRequest;
    private IntegrationContext integrationContext;

    public AnnotationIntegrationResultImpl() {
    }

    public AnnotationIntegrationResultImpl(AnnotationIntegrationRequestImpl integrationRequest, IntegrationContext integrationContext) {
        this.annotationIntegrationRequest = integrationRequest;
        this.integrationContext = integrationContext;
    }

    public IntegrationContext getIntegrationContext() {
        return this.integrationContext;
    }

    @Override
    public IntegrationRequest getIntegrationRequest() {
        return this.annotationIntegrationRequest;
    }

    public AnnotationIntegrationRequestImpl getAnnotationIntegrationRequest() {
        return annotationIntegrationRequest;
    }

    public void setAnnotationIntegrationRequest(AnnotationIntegrationRequestImpl annotationIntegrationRequest) {
        this.annotationIntegrationRequest = annotationIntegrationRequest;
    }

    public void setIntegrationContext(IntegrationContext integrationContext) {
        this.integrationContext = integrationContext;
    }
}
