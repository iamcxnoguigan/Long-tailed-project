package com.l2l.enterprise.vessel.extension.activiti.annotation;

import org.activiti.engine.delegate.DelegateExecution;

public interface AnnotationConnector{
    AnnotationIntergrationContextImpl execute(AnnotationIntergrationContextImpl var1, DelegateExecution execution);

}
