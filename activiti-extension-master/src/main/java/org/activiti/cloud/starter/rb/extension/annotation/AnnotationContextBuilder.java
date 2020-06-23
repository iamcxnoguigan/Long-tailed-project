package org.activiti.cloud.starter.rb.extension.annotation;

import org.activiti.cloud.starter.rb.extension.model.Annotation;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.impl.persistence.entity.integration.IntegrationContextEntity;
import org.activiti.runtime.api.model.IntegrationContext;
import org.activiti.runtime.api.model.impl.IntegrationContextImpl;

public class AnnotationContextBuilder {
    public AnnotationContextBuilder() {
    }

    public IntegrationContext from(IntegrationContextEntity integrationContextEntity, DelegateExecution execution , Annotation annotation) {
        IntegrationContextImpl integrationContext = this.buildFromExecution(execution , annotation);
        integrationContext.setId(integrationContextEntity.getId());
        return integrationContext;
    }

    public IntegrationContext from(DelegateExecution execution, Annotation annotation) {
        IntegrationContextImpl integrationContext = this.buildFromExecution(execution, annotation);
        return integrationContext;
    }
    private IntegrationContextImpl buildFromExecution(DelegateExecution execution  , Annotation annotation) {
        IntegrationContextImpl integrationContext = new IntegrationContextImpl();
        integrationContext.setProcessInstanceId(execution.getProcessInstanceId());
        integrationContext.setProcessDefinitionId(execution.getProcessDefinitionId());
        integrationContext.setActivityElementId(annotation.getTargetElementId());
        integrationContext.setConnectorType((annotation.getDestination()));
        integrationContext.setInBoundVariables(execution.getVariables());
        return integrationContext;
    }
}
