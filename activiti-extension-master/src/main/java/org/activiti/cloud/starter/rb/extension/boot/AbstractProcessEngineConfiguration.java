package org.activiti.cloud.starter.rb.extension.boot;

import org.activiti.engine.*;
import org.activiti.engine.impl.persistence.entity.integration.IntegrationContextManager;
import org.activiti.engine.integration.IntegrationContextService;
import org.activiti.spring.ProcessEngineFactoryBean;
import org.activiti.spring.SpringAsyncExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AbstractProcessEngineConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(org.activiti.spring.boot.AbstractProcessEngineConfiguration.class);

    public AbstractProcessEngineConfiguration() {
    }

    public ProcessEngineFactoryBean l2LProcessEngineBean(L2LProcessEngineConfiguration configuration) {
        ProcessEngineFactoryBean processEngineFactoryBean = new ProcessEngineFactoryBean();
        processEngineFactoryBean.setProcessEngineConfiguration(configuration);
        return processEngineFactoryBean;
    }

    public L2LProcessEngineConfiguration processEngineConfigurationBean(ApplicationEventPublisher eventPublisher, Resource[] processDefinitions, DataSource dataSource, PlatformTransactionManager transactionManager, SpringAsyncExecutor springAsyncExecutor) throws IOException {
        L2LProcessEngineConfiguration engine = new L2LProcessEngineConfiguration();
        engine.setEventPublisher(eventPublisher);
        if (processDefinitions != null && processDefinitions.length > 0) {
            engine.setDeploymentResources(processDefinitions);
        }

        engine.setDataSource(dataSource);
        engine.setTransactionManager(transactionManager);
        if (null != springAsyncExecutor) {
            engine.setAsyncExecutor(springAsyncExecutor);
        }

        return engine;
    }

    public List<Resource> discoverProcessDefinitionResources(ResourcePatternResolver applicationContext, String prefix, List<String> suffixes, boolean checkPDs) throws IOException {
        if (!checkPDs) {
            return new ArrayList();
        } else {
            List<Resource> result = new ArrayList();
            Iterator var6 = suffixes.iterator();

            while(true) {
                Resource[] resources;
                do {
                    do {
                        if (!var6.hasNext()) {
                            if (result.isEmpty()) {
                                logger.info(String.format("No processes definitions were found for autodeployment"));
                            }

                            return result;
                        }

                        String suffix = (String)var6.next();
                        String path = prefix + suffix;
                        resources = applicationContext.getResources(path);
                    } while(resources == null);
                } while(resources.length <= 0);

                Resource[] var10 = resources;
                int var11 = resources.length;

                for(int var12 = 0; var12 < var11; ++var12) {
                    Resource resource = var10[var12];
                    result.add(resource);
                }
            }
        }
    }

    public RuntimeService runtimeServiceBean(ProcessEngine processEngine) {
        return processEngine.getRuntimeService();
    }

    public RepositoryService repositoryServiceBean(ProcessEngine processEngine) {
        return processEngine.getRepositoryService();
    }

    public TaskService taskServiceBean(ProcessEngine processEngine) {
        return processEngine.getTaskService();
    }

    public HistoryService historyServiceBean(ProcessEngine processEngine) {
        return processEngine.getHistoryService();
    }

    public ManagementService managementServiceBeanBean(ProcessEngine processEngine) {
        return processEngine.getManagementService();
    }

    public IntegrationContextManager integrationContextManagerBean(ProcessEngine processEngine) {
        return processEngine.getProcessEngineConfiguration().getIntegrationContextManager();
    }

    public IntegrationContextService integrationContextServiceBean(ProcessEngine processEngine) {
        return processEngine.getProcessEngineConfiguration().getIntegrationContextService();
    }
}
