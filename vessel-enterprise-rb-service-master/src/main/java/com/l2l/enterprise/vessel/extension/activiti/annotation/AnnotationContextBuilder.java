package com.l2l.enterprise.vessel.extension.activiti.annotation;

import com.l2l.enterprise.vessel.extension.activiti.model.Annotation;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.impl.persistence.entity.integration.IntegrationContextEntity;
import org.activiti.runtime.api.model.IntegrationContext;
import org.activiti.runtime.api.model.impl.IntegrationContextImpl;

public class AnnotationContextBuilder {
    public AnnotationContextBuilder() {
    }

    public AnnotationIntergrationContextImpl from(IntegrationContextEntity integrationContextEntity, DelegateExecution execution , Annotation annotation) {
        AnnotationIntergrationContextImpl integrationContext = this.buildFromExecution(execution , annotation);
        integrationContext.setId(integrationContextEntity.getId());
        return integrationContext;
    }

    public AnnotationIntergrationContextImpl from(DelegateExecution execution, Annotation annotation) {
        AnnotationIntergrationContextImpl integrationContext = this.buildFromExecution(execution, annotation);
        return integrationContext;
    }
    private AnnotationIntergrationContextImpl buildFromExecution(DelegateExecution execution  , Annotation annotation) {
        AnnotationIntergrationContextImpl integrationContext = new AnnotationIntergrationContextImpl();
        integrationContext.setProcessInstanceId(execution.getProcessInstanceId());
        integrationContext.setProcessDefinitionId(execution.getProcessDefinitionId());
        integrationContext.setActivityElementId(annotation.getTargetElementId());
        integrationContext.setConnectorType((annotation.getDestination()));//connectortype 是目的第三方
        integrationContext.setInBoundVariables(execution.getVariables());
        integrationContext.setAnnotation(annotation);
        return integrationContext;
    }
}
