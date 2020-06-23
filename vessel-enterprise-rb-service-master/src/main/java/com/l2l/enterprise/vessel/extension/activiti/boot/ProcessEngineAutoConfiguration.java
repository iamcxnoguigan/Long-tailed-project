package com.l2l.enterprise.vessel.extension.activiti.boot;


import com.l2l.enterprise.vessel.extension.activiti.annotation.AnnotationService;
import com.l2l.enterprise.vessel.extension.activiti.annotation.AnnotationServiceImpl;
import com.l2l.enterprise.vessel.extension.activiti.behavior.L2LActivityBehaviorFactory;
import com.l2l.enterprise.vessel.extension.activiti.form.FormService;
import com.l2l.enterprise.vessel.extension.activiti.form.FormServiceImpl;
import com.l2l.enterprise.vessel.extension.activiti.parser.L2LProcessParseHandler;
import com.l2l.enterprise.vessel.extension.activiti.parser.L2LServiceTaskParseHandler;
import com.l2l.enterprise.vessel.extension.activiti.parser.L2LServiceTaskXMLConverter;
import com.l2l.enterprise.vessel.extension.activiti.parser.L2LTimerDefinitionParseHandler;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.engine.impl.ServiceImpl;
import org.activiti.engine.parse.BpmnParseHandler;
import org.activiti.runtime.api.identity.UserGroupManager;
import org.activiti.spring.SpringAsyncExecutor;
import org.activiti.spring.SpringProcessEngineConfiguration;
import org.activiti.spring.boot.ActivitiProperties;
import org.activiti.spring.boot.ProcessEngineConfigurationConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Configuration
@AutoConfigureAfter({DataSourceAutoConfiguration.class})
@EnableConfigurationProperties({ActivitiProperties.class})
public class ProcessEngineAutoConfiguration extends AbstractProcessEngineAutoConfiguration {

    @Qualifier("l2LUserGroupManagerImpl")
    @Autowired
    protected UserGroupManager userGroupManager;

    @Autowired
    private ResourcePatternResolver resourceLoader;
    @Autowired
    ApplicationEventPublisher eventPublisher;
    @Autowired(
        required = false
    )
    private ProcessEngineConfigurationConfigurer processEngineConfigurationConfigurer;


    public ProcessEngineAutoConfiguration() {
    }

//    @Bean
//    public DeployedEventListener deployedEventListener(){
//        return new DeployedEventListener();
//    }

    @Bean
    @ConditionalOnMissingBean
    public L2LProcessEngineConfiguration l2LProcessEngineConfiguration(ApplicationEventPublisher eventPublisher, DataSource dataSource, PlatformTransactionManager transactionManager, SpringAsyncExecutor springAsyncExecutor) throws IOException {
        springAsyncExecutor.setDefaultTimerJobAcquireWaitTimeInMillis(5000);
        L2LProcessEngineConfiguration conf =  super.baseL2LProcessEngineConfiguration(eventPublisher, dataSource, transactionManager, springAsyncExecutor, this.userGroupManager);
//        conf.setBpmnParseFactory(new BpmnParseFactory() {
//            @Override
//            public BpmnParse createBpmnParse(BpmnParser bpmnParser) {
//                return new BpmnParse(conf.getBpmnParser());
//            }
//        });
        // replace the default 'ServiceTaskXMLConverter' with custom one.
        BpmnXMLConverter.addConverter(new L2LServiceTaskXMLConverter());
        // customize the default activity behavior factory
        L2LActivityBehaviorFactory l2LActivityBehaviorFactory = new L2LActivityBehaviorFactory();
        conf.setActivityBehaviorFactory(l2LActivityBehaviorFactory);
        // customize the parse handlers.
        List<BpmnParseHandler> customDefaultBpmnParseHandlers = new ArrayList<BpmnParseHandler>();
        customDefaultBpmnParseHandlers.add(new L2LServiceTaskParseHandler());
        customDefaultBpmnParseHandlers.add(new L2LTimerDefinitionParseHandler());
        customDefaultBpmnParseHandlers.add(new L2LProcessParseHandler());
        conf.setCustomDefaultBpmnParseHandlers(customDefaultBpmnParseHandlers);

        // The asynchronous job executor is disabled by default and needs to be activated.
//        conf.setAsyncExecutorDefaultTimerJobAcquireWaitTime(5000);
//        conf.setAsyncExecutorActivate(true);


        return conf;
    }

    @Bean
    @ConditionalOnMissingBean
    public FormService formService(L2LProcessEngineConfiguration l2LProcessEngineConfiguration) {
        FormService formService = new FormServiceImpl();
        if (formService instanceof ServiceImpl) {
            ((ServiceImpl)formService).setCommandExecutor(l2LProcessEngineConfiguration.getCommandExecutor());
        }
        return formService;
    }

    @Bean
    @ConditionalOnMissingBean
    public AnnotationService annotationService(L2LProcessEngineConfiguration l2LProcessEngineConfiguration) {
        AnnotationService annotationService = new AnnotationServiceImpl(l2LProcessEngineConfiguration);
        if (annotationService instanceof ServiceImpl) {
            ((ServiceImpl)annotationService).setCommandExecutor(l2LProcessEngineConfiguration.getCommandExecutor());
         }
        return annotationService;
    }
}
