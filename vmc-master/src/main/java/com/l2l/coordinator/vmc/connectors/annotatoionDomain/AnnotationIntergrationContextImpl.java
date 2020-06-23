package com.l2l.coordinator.vmc.connectors.annotatoionDomain;

import com.l2l.coordinator.vmc.connectors.model.Annotation;
import org.activiti.runtime.api.model.impl.IntegrationContextImpl;

public class AnnotationIntergrationContextImpl extends IntegrationContextImpl {
    private Annotation annotation;
    public AnnotationIntergrationContextImpl() {
    }

    public Annotation getAnnotation() {
        return annotation;
    }

    public void setAnnotation(Annotation annotation) {
        this.annotation = annotation;
    }
}
