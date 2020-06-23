package com.l2l.enterprise.vessel.extension.activiti.config;

import com.l2l.enterprise.vessel.extension.activiti.annotation.*;
import com.l2l.enterprise.vessel.extension.activiti.boot.L2LProcessEngineConfiguration;
import com.l2l.enterprise.vessel.extension.activiti.model.Annotation;
import com.l2l.enterprise.vessel.service.ListObjects;
import com.l2l.enterprise.vessel.service.TestListObject;
import org.activiti.cloud.services.events.converter.RuntimeBundleInfoAppender;
import org.activiti.engine.impl.delegate.ActivityBehavior;
import org.activiti.engine.impl.persistence.entity.integration.IntegrationContextManager;
import org.activiti.runtime.api.connector.IntegrationContextBuilder;
import org.activiti.services.connectors.IntegrationRequestSender;
import org.activiti.spring.boot.EndpointAutoConfiguration;
import org.activiti.spring.boot.ProcessEngineAutoConfiguration;
import org.activiti.spring.identity.ActivitiUserGroupManagerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

import java.util.List;

@Configuration
@ComponentScan(
    basePackages = {"org.activiti"} ,
    excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE , value = ProcessEngineAutoConfiguration.class),
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE , value = EndpointAutoConfiguration.class),
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE , value = ActivitiUserGroupManagerImpl.class),
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE , value = IntegrationRequestSender.class)
    }
 )
public class L2LActivitiRuntimeBundleAutoConfiguration {
    public L2LActivitiRuntimeBundleAutoConfiguration(){
        System.out.println("===ActivitiRuntimeBundleAutoConfiguration===");
    }

    @Bean
    public TestListObject testListObject(){
        return new TestListObject();
    }
    @Bean
    public TestListObject testListObject1(){
        return new TestListObject();
    }


    @Bean
    public ListObjects listObjects(@Autowired(required = false) List<TestListObject> listObjects){
        return new ListObjects(listObjects);
    }

    @Bean
    public AnnotationContextBuilder annotationContextBuilder() {
        return new AnnotationContextBuilder();
    }

    @Bean
    public AnnotationCollector annotationCollector(AnnotationService annotationService,
        IntegrationContextManager integrationContextManager, ApplicationEventPublisher eventPublisher,
        ApplicationContext applicationContext, AnnotationContextBuilder annotationContextBuilder, RuntimeBundleInfoAppender runtimeBundleInfoAppender){
        AnnotationCollector annotationCollector = new AnnotationCollector(annotationService);
        AnnotationManager annotationManager = annotationService.getL2LProcessEngineConfiguration().getAnnotationManager();
        DefaultAnnotationBehavior annotationBehavior = new DefaultAnnotationBehavior(annotationService,integrationContextManager , eventPublisher , applicationContext ,annotationContextBuilder ,  runtimeBundleInfoAppender);
        if(annotationBehavior instanceof ActivityBehavior){
            annotationManager.setBehavior(annotationBehavior);
        }
        annotationCollector.collectAnnotaions();
        return annotationCollector;
    }
}
