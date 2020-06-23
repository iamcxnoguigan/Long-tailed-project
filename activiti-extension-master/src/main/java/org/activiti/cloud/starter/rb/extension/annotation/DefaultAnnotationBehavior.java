package org.activiti.cloud.starter.rb.extension.annotation;


import org.activiti.cloud.services.events.converter.RuntimeBundleInfoAppender;
import org.activiti.cloud.starter.rb.extension.boot.L2LProcessEngineConfiguration;
import org.activiti.cloud.starter.rb.extension.model.Annotation;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.impl.bpmn.behavior.AbstractBpmnActivityBehavior;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.integration.IntegrationContextEntity;
import org.activiti.engine.impl.persistence.entity.integration.IntegrationContextManager;
import org.activiti.runtime.api.model.impl.IntegrationRequestImpl;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Date;

public class DefaultAnnotationBehavior extends AbstractBpmnActivityBehavior {
    private final IntegrationContextManager integrationContextManager;
    private final ApplicationEventPublisher eventPublisher;
    private final AnnotationContextBuilder annotationContextBuilder;
    private final RuntimeBundleInfoAppender runtimeBundleInfoAppender;
    private final ApplicationContext applicationContext;
    private Annotation annotation;


    public DefaultAnnotationBehavior(IntegrationContextManager integrationContextManager, ApplicationEventPublisher eventPublisher, ApplicationContext applicationContext, AnnotationContextBuilder annotationContextBuilder, RuntimeBundleInfoAppender runtimeBundleInfoAppender) {
        this.applicationContext = applicationContext;
        this.annotationContextBuilder = annotationContextBuilder;
        this.integrationContextManager = integrationContextManager;
        this.eventPublisher = eventPublisher;
        this.runtimeBundleInfoAppender = runtimeBundleInfoAppender;
    }

    public void execute(DelegateExecution execution) {
        IntegrationContextEntity integrationContext = this.storeIntegrationContext(execution);
        this.publishSpringEvent(execution, integrationContext);
    }

    private void publishSpringEvent(DelegateExecution execution, IntegrationContextEntity integrationContext) {
        IntegrationRequestImpl integrationRequest = new IntegrationRequestImpl(this.annotationContextBuilder.from(integrationContext, execution ,annotation));
        this.runtimeBundleInfoAppender.appendRuntimeBundleInfoTo(integrationRequest);
        this.eventPublisher.publishEvent(integrationRequest);
    }

    private IntegrationContextEntity storeIntegrationContext(DelegateExecution execution) {
        IntegrationContextEntity integrationContext = this.buildIntegrationContext(execution);
        this.integrationContextManager.insert(integrationContext);
        return integrationContext;
    }

    private IntegrationContextEntity buildIntegrationContext(DelegateExecution execution) {
        IntegrationContextEntity integrationContext = (IntegrationContextEntity)this.integrationContextManager.create();
        integrationContext.setExecutionId(execution.getId());
        integrationContext.setProcessInstanceId(execution.getProcessInstanceId());
        integrationContext.setProcessDefinitionId(execution.getProcessDefinitionId());
        integrationContext.setFlowNodeId(execution.getCurrentActivityId());
        integrationContext.setCreatedDate(new Date());
        return integrationContext;
    }

    public void trigger(DelegateExecution execution, String signalEvent, Object signalData) {
        // After the sync request corresponding to the current annotation is replied from third party , it should be deleted.

        if(Context.getProcessEngineConfiguration() instanceof L2LProcessEngineConfiguration){
            ((L2LProcessEngineConfiguration) Context.getProcessEngineConfiguration())
                .getAnnotationManager().getAnnotations()
                .remove(annotation);
        }
        // continuing running the current execution which the annotation attached.
        Context.getAgenda().planContinueProcessOperation((ExecutionEntity)execution);

    }

    public Annotation getAnnotation() {
        return annotation;
    }

    public void setAnnotation(Annotation annotation) {
        this.annotation = annotation;
    }
}
