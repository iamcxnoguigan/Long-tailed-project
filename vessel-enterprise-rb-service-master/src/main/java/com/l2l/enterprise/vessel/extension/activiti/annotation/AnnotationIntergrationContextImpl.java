package com.l2l.enterprise.vessel.extension.activiti.annotation;

import com.l2l.enterprise.vessel.extension.activiti.model.Annotation;
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
