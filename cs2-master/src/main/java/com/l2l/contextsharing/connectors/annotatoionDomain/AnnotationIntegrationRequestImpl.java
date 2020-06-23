package com.l2l.contextsharing.connectors.annotatoionDomain;


import org.activiti.runtime.api.model.IntegrationContext;
import org.activiti.runtime.api.model.IntegrationRequest;
import org.activiti.runtime.api.model.impl.CloudRuntimeEntityImpl;

public class AnnotationIntegrationRequestImpl extends CloudRuntimeEntityImpl implements IntegrationRequest {
    private AnnotationIntergrationContextImpl annotationIntergrationContext;

    public AnnotationIntegrationRequestImpl() {
    }

    public AnnotationIntegrationRequestImpl(AnnotationIntergrationContextImpl annotationIntergrationContext) {
        this.annotationIntergrationContext = annotationIntergrationContext;
    }

    public AnnotationIntergrationContextImpl getAnnotationIntergrationContext() {
        return this.annotationIntergrationContext;
    }

    @Override
    public IntegrationContext getIntegrationContext() {
        return null;
    }


}
