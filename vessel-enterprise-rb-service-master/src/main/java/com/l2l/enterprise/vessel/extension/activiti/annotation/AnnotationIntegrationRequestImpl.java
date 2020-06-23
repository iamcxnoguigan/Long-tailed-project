package com.l2l.enterprise.vessel.extension.activiti.annotation;

import org.activiti.runtime.api.model.IntegrationContext;
import org.activiti.runtime.api.model.IntegrationRequest;
import org.activiti.runtime.api.model.impl.CloudRuntimeEntityImpl;
import org.activiti.runtime.api.model.impl.IntegrationRequestImpl;

public class AnnotationIntegrationRequestImpl extends CloudRuntimeEntityImpl implements IntegrationRequest{
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
