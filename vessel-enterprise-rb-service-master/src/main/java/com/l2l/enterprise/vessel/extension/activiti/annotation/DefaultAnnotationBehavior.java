package com.l2l.enterprise.vessel.extension.activiti.annotation;

import com.l2l.enterprise.vessel.extension.activiti.agenda.L2LActivitiEngineAgenda;
import com.l2l.enterprise.vessel.extension.activiti.agenda.L2LContinueProcessOperation;
import com.l2l.enterprise.vessel.extension.activiti.boot.L2LProcessEngineConfiguration;
import com.l2l.enterprise.vessel.extension.activiti.config.EventListenerConfiguration;
import com.l2l.enterprise.vessel.extension.activiti.model.Annotation;
import org.activiti.cloud.services.events.converter.RuntimeBundleInfoAppender;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.impl.bpmn.behavior.AbstractBpmnActivityBehavior;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.integration.IntegrationContextEntity;
import org.activiti.engine.impl.persistence.entity.integration.IntegrationContextManager;
import org.activiti.runtime.api.connector.Connector;
import org.activiti.runtime.api.model.IntegrationContext;
import org.activiti.runtime.api.model.impl.IntegrationRequestImpl;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Date;
import java.util.logging.Logger;

public class DefaultAnnotationBehavior extends AbstractBpmnActivityBehavior {
    private final IntegrationContextManager integrationContextManager;
    private final ApplicationEventPublisher eventPublisher;
    private final AnnotationContextBuilder annotationContextBuilder;
    private final RuntimeBundleInfoAppender runtimeBundleInfoAppender;
    private final ApplicationContext applicationContext;
    private final AnnotationService annotationService;
    private static org.slf4j.Logger log = LoggerFactory.getLogger(DefaultAnnotationBehavior.class);


    public DefaultAnnotationBehavior(AnnotationService annotationService,IntegrationContextManager integrationContextManager, ApplicationEventPublisher eventPublisher, ApplicationContext applicationContext, AnnotationContextBuilder annotationContextBuilder, RuntimeBundleInfoAppender runtimeBundleInfoAppender) {
        this.applicationContext = applicationContext;
        this.annotationContextBuilder = annotationContextBuilder;
        this.integrationContextManager = integrationContextManager;
        this.eventPublisher = eventPublisher;
        this.runtimeBundleInfoAppender = runtimeBundleInfoAppender;
        this.annotationService = annotationService;
    }

    public void execute(DelegateExecution execution,Annotation annotation) {
        IntegrationContextEntity integrationContext = this.storeIntegrationContext(execution);
        if (this.hasConnectorBean(execution , annotation)) {
            AnnotationConnector connector = (AnnotationConnector)this.applicationContext.getBean(annotation.getDestination(), AnnotationConnector.class);
            AnnotationIntergrationContextImpl context = this.annotationContextBuilder.from(execution , annotation);
            connector.execute(context,execution);
            execution.setVariables(context.getOutBoundVariables());
            //trigger to leave
            this.annotationService.trigger(execution.getId(),integrationContext ,  context);//判断有没有本地实现 没有就抛给第三方
//            super.execute(execution);
        } else {
            this.publishSpringEvent(execution, integrationContext,annotation);
            log.info("annotationbehavior execute");
        }
    }

    private void publishSpringEvent(DelegateExecution execution, IntegrationContextEntity integrationContext,Annotation annotation) {
        AnnotationIntegrationRequestImpl integrationRequest = new AnnotationIntegrationRequestImpl(this.annotationContextBuilder.from(integrationContext, execution ,annotation));
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

//    public void trigger(DelegateExecution execution,String integrationContextEntityId, Object integrationContextEntity) {
//        // After the sync request corresponding to the current annotation is replied from third party , it should be deleted.
//
////        if(Context.getProcessEngineConfiguration() instanceof L2LProcessEngineConfiguration){
////            ((L2LProcessEngineConfiguration) Context.getProcessEngineConfiguration())
////                .getAnnotationManager().getAnnotations()
////                .remove(annotation);
////         continuing running the current execution which the annotation attached.
//        ((L2LActivitiEngineAgenda)Context.getAgenda()).planintegrationContextEntityContinueProcessOperation((ExecutionEntity)execution,(IntegrationContextEntity) integrationContextEntity);
//
//    }

//    public Annotation getAnnotation() {
//        return annotation;
//    }
//
//    public void setAnnotation(Annotation annotation) {
//        this.annotation = annotation;
//    }


    protected boolean hasConnectorBean(DelegateExecution execution,Annotation annotation) {
        String implementation = annotation.getDestination();
        return this.applicationContext.containsBean(implementation) && this.applicationContext.getBean(implementation) instanceof AnnotationConnector;
    }
}
