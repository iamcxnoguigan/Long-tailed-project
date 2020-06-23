package com.l2l.enterprise.vessel.extension.activiti.utils;

import com.l2l.enterprise.vessel.extension.activiti.boot.L2LProcessEngineConfiguration;
import com.l2l.enterprise.vessel.extension.activiti.cache.L2LDeploymentCache;
import org.activiti.bpmn.model.Process;
import org.activiti.engine.ActivitiException;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.impl.DeploymentQueryImpl;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.persistence.deploy.DeploymentCache;
import org.activiti.engine.impl.persistence.deploy.DeploymentManager;
import org.activiti.engine.impl.persistence.deploy.ProcessDefinitionCacheEntry;
import org.activiti.engine.impl.persistence.entity.DeploymentEntity;
import org.activiti.engine.impl.util.Activiti5Util;
import org.activiti.engine.impl.util.ProcessDefinitionUtil;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentQuery;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class L2LProcessDefinitionUtil {
    private static Logger log = LoggerFactory.getLogger(L2LProcessDefinitionUtil.class);

    public L2LProcessDefinitionUtil() {
    }

    public static List<Process> getProcessesOfAllVersions(L2LProcessEngineConfiguration l2LProcessEngineConfiguration) {
        List<Process> res = new ArrayList<Process>();
        List<String> pids = getProcessDefinitionIdsOfAllVersions(l2LProcessEngineConfiguration);
        return res;

    }

    public static List<String> getProcessDefinitionIdsOfAllVersions(L2LProcessEngineConfiguration l2LProcessEngineConfiguration) {
        DeploymentManager deploymentManager = l2LProcessEngineConfiguration.getDeploymentManager();
        DeploymentCache<ProcessDefinitionCacheEntry> processDefinitionCache = deploymentManager.getProcessDefinitionCache();
        List<String> res = new ArrayList<String>();
        //if the new version of the bundled processDefinitions is deployed, get them from the `DeploymentManager`.
        if (processDefinitionCache instanceof L2LDeploymentCache) {
            Map<String, ProcessDefinitionCacheEntry> mp = ((L2LDeploymentCache<ProcessDefinitionCacheEntry>) processDefinitionCache).getCache();
            List<String> newPids = mp.entrySet().stream().map(e -> e.getKey()).collect(Collectors.toList());
            res.addAll(newPids);
        }
        //Get the old version of the deployed processDefinitions
        RepositoryService repositoryService = l2LProcessEngineConfiguration.getRepositoryService();
        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery();
        List<String> oldPids = processDefinitionQuery.list().stream().map(ProcessDefinition::getId).collect(Collectors.toList());
        res.addAll(oldPids);
        return res;
    }

    public static Process getProcess(String processDefinitionId , L2LProcessEngineConfiguration l2LProcessEngineConfiguration) {
        if (l2LProcessEngineConfiguration == null) {
            log.debug("l2LProcessEngineConfiguration is null");
            return  null;
         } else {
            DeploymentManager deploymentManager = l2LProcessEngineConfiguration.getDeploymentManager();
            RepositoryService repositoryService = l2LProcessEngineConfiguration.getRepositoryService();
            ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery();
            ProcessDefinition processDefinitionEntity = processDefinitionQuery.processDefinitionId(processDefinitionId).singleResult();
            if(processDefinitionEntity == null){
                processDefinitionEntity = deploymentManager.findDeployedProcessDefinitionById(processDefinitionId);
            }
            DeploymentQuery deploymentQuery = repositoryService.createDeploymentQuery();
//            DeploymentEntity deployment = (DeploymentEntity) deploymentQuery.deploymentId(processDefinitionEntity.getDeploymentId()).singleResult();
//            if(deployment == null){
                DeploymentEntity deployment = (DeploymentEntity)deploymentManager.getDeploymentEntityManager().findById(processDefinitionEntity.getDeploymentId());
//            }
            deployment.setNew(false);
            deploymentManager.deploy(deployment, (Map)null);
            ProcessDefinitionCacheEntry cachedProcessDefinition = (ProcessDefinitionCacheEntry) deploymentManager.getProcessDefinitionCache().get(processDefinitionId);
            if (cachedProcessDefinition == null) {
                throw new ActivitiException("deployment '" + deployment.getId() + "' didn't put process definition '" + processDefinitionId + "' in the cache");
            }
            return cachedProcessDefinition.getProcess();
        }
    }
}
