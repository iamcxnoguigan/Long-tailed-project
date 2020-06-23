package org.activiti.cloud.starter.rb.configuration;

import org.activiti.cloud.services.events.converter.RuntimeBundleInfoAppender;
import org.activiti.cloud.starter.rb.extension.annotation.*;
import org.activiti.engine.impl.delegate.ActivityBehavior;
import org.activiti.engine.impl.persistence.entity.integration.IntegrationContextManager;
import org.activiti.spring.boot.EndpointAutoConfiguration;
import org.activiti.spring.boot.ProcessEngineAutoConfiguration;
import org.activiti.spring.identity.ActivitiUserGroupManagerImpl;
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
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE , value = ActivitiUserGroupManagerImpl.class)
    }
 )
public class ActivitiRuntimeBundleAutoConfiguration {
    public ActivitiRuntimeBundleAutoConfiguration(){
        System.out.println("===ActivitiRuntimeBundleAutoConfiguration===");
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
        DefaultAnnotationBehavior annotationBehavior = new DefaultAnnotationBehavior(integrationContextManager , eventPublisher , applicationContext ,annotationContextBuilder ,  runtimeBundleInfoAppender);
        if(annotationBehavior instanceof ActivityBehavior){
            annotationManager.setBehavior(annotationBehavior);
        }
        annotationCollector.collectAnnotaions();
        return annotationCollector;
    }
}
