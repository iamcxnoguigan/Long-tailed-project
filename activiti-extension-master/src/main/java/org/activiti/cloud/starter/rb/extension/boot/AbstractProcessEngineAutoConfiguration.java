package org.activiti.cloud.starter.rb.extension.boot;

import org.activiti.engine.*;
import org.activiti.engine.impl.persistence.StrongUuidGenerator;
import org.activiti.engine.impl.persistence.entity.integration.IntegrationContextManager;
import org.activiti.engine.integration.IntegrationContextService;
import org.activiti.runtime.api.identity.UserGroupManager;
import org.activiti.spring.ProcessEngineFactoryBean;
import org.activiti.spring.SpringAsyncExecutor;
import org.activiti.spring.SpringCallerRunsRejectedJobsHandler;
import org.activiti.spring.SpringRejectedJobsHandler;
import org.activiti.spring.boot.ActivitiProperties;
import org.activiti.spring.boot.ProcessEngineConfigurationConfigurer;
import org.activiti.spring.bpmn.parser.CloudActivityBehaviorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class AbstractProcessEngineAutoConfiguration extends AbstractProcessEngineConfiguration {
    protected ActivitiProperties activitiProperties;
    @Autowired
    private ResourcePatternResolver resourceLoader;
    @Autowired(
        required = false
    )
    private ProcessEngineConfigurationConfigurer processEngineConfigurationConfigurer;

    public AbstractProcessEngineAutoConfiguration() {
    }

    @Bean
    public SpringAsyncExecutor springAsyncExecutor(TaskExecutor taskExecutor) {
        return new SpringAsyncExecutor(taskExecutor, this.springRejectedJobsHandler());
    }

    @Bean
    public SpringRejectedJobsHandler springRejectedJobsHandler() {
        return new SpringCallerRunsRejectedJobsHandler();
    }

    protected L2LProcessEngineConfiguration baseL2LProcessEngineConfiguration(ApplicationEventPublisher eventPublisher , DataSource dataSource, PlatformTransactionManager platformTransactionManager, SpringAsyncExecutor springAsyncExecutor, UserGroupManager userGroupManager) throws IOException {
        List<Resource> procDefResources = this.discoverProcessDefinitionResources(this.resourceLoader, this.activitiProperties.getProcessDefinitionLocationPrefix(), this.activitiProperties.getProcessDefinitionLocationSuffixes(), this.activitiProperties.isCheckProcessDefinitions());
        L2LProcessEngineConfiguration conf = super.processEngineConfigurationBean(eventPublisher, (Resource[])procDefResources.toArray(new Resource[procDefResources.size()]), dataSource, platformTransactionManager, springAsyncExecutor);
        conf.setDeploymentName(this.defaultText(this.activitiProperties.getDeploymentName(), conf.getDeploymentName()));
        conf.setDatabaseSchema(this.defaultText(this.activitiProperties.getDatabaseSchema(), conf.getDatabaseSchema()));
        conf.setDatabaseSchemaUpdate(this.defaultText(this.activitiProperties.getDatabaseSchemaUpdate(), conf.getDatabaseSchemaUpdate()));
        conf.setDbHistoryUsed(this.activitiProperties.isDbHistoryUsed());
        conf.setAsyncExecutorActivate(this.activitiProperties.isAsyncExecutorActivate());
        conf.setMailServerHost(this.activitiProperties.getMailServerHost());
        conf.setMailServerPort(this.activitiProperties.getMailServerPort());
        conf.setMailServerUsername(this.activitiProperties.getMailServerUserName());
        conf.setMailServerPassword(this.activitiProperties.getMailServerPassword());
        conf.setMailServerDefaultFrom(this.activitiProperties.getMailServerDefaultFrom());
        conf.setMailServerUseSSL(this.activitiProperties.isMailServerUseSsl());
        conf.setMailServerUseTLS(this.activitiProperties.isMailServerUseTls());
        if (userGroupManager != null) {
            conf.setUserGroupManager(userGroupManager);
        }

        conf.setHistoryLevel(this.activitiProperties.getHistoryLevel());
        if (this.activitiProperties.getCustomMybatisMappers() != null) {
            conf.setCustomMybatisMappers(this.getCustomMybatisMapperClasses(this.activitiProperties.getCustomMybatisMappers()));
        }

        if (this.activitiProperties.getCustomMybatisXMLMappers() != null) {
            conf.setCustomMybatisXMLMappers(new HashSet(this.activitiProperties.getCustomMybatisXMLMappers()));
        }

        if (this.activitiProperties.getCustomMybatisXMLMappers() != null) {
            conf.setCustomMybatisXMLMappers(new HashSet(this.activitiProperties.getCustomMybatisXMLMappers()));
        }

        if (this.activitiProperties.isUseStrongUuids()) {
            conf.setIdGenerator(new StrongUuidGenerator());
        }

        conf.setActivityBehaviorFactory(new CloudActivityBehaviorFactory());
        if (this.processEngineConfigurationConfigurer != null) {
            this.processEngineConfigurationConfigurer.configure(conf);
        }

        return conf;
    }

    protected Set<Class<?>> getCustomMybatisMapperClasses(List<String> customMyBatisMappers) {
        Set<Class<?>> mybatisMappers = new HashSet();
        Iterator var3 = customMyBatisMappers.iterator();

        while(var3.hasNext()) {
            String customMybatisMapperClassName = (String)var3.next();

            try {
                Class customMybatisClass = Class.forName(customMybatisMapperClassName);
                mybatisMappers.add(customMybatisClass);
            } catch (ClassNotFoundException var6) {
                throw new IllegalArgumentException("Class " + customMybatisMapperClassName + " has not been found.", var6);
            }
        }

        return mybatisMappers;
    }

    protected String defaultText(String deploymentName, String deploymentName1) {
        return StringUtils.hasText(deploymentName) ? deploymentName : deploymentName1;
    }

    @Autowired
    protected void setActivitiProperties(ActivitiProperties activitiProperties) {
        this.activitiProperties = activitiProperties;
    }

    protected ActivitiProperties getActivitiProperties() {
        return this.activitiProperties;
    }

    @Bean
    public ProcessEngineFactoryBean processEngine(L2LProcessEngineConfiguration configuration) throws Exception {
        ProcessEngineFactoryBean processEngineFactoryBean = super.l2LProcessEngineBean(configuration);
        return processEngineFactoryBean;
    }

    @Bean
    @ConditionalOnMissingBean
    public RuntimeService runtimeServiceBean(ProcessEngine processEngine) {
        ProcessEngineConfiguration conf = processEngine.getProcessEngineConfiguration();
        return super.runtimeServiceBean(processEngine);
    }

    @Bean
    @ConditionalOnMissingBean
    public RepositoryService repositoryServiceBean(ProcessEngine processEngine) {
        return super.repositoryServiceBean(processEngine);
    }

    @Bean
    @ConditionalOnMissingBean
    public TaskService taskServiceBean(ProcessEngine processEngine) {
        return super.taskServiceBean(processEngine);
    }

    @Bean
    @ConditionalOnMissingBean
    public HistoryService historyServiceBean(ProcessEngine processEngine) {
        return super.historyServiceBean(processEngine);
    }

    @Bean
    @ConditionalOnMissingBean
    public ManagementService managementServiceBeanBean(ProcessEngine processEngine) {
        return super.managementServiceBeanBean(processEngine);
    }

    @Bean
    @ConditionalOnMissingBean
    public TaskExecutor taskExecutor() {
        return new SimpleAsyncTaskExecutor();
    }

    @Bean
    @ConditionalOnMissingBean
    public IntegrationContextManager integrationContextManagerBean(ProcessEngine processEngine) {
        return super.integrationContextManagerBean(processEngine);
    }

    @Bean
    @ConditionalOnMissingBean
    public IntegrationContextService integrationContextServiceBean(ProcessEngine processEngine) {
        return super.integrationContextServiceBean(processEngine);
    }
}
