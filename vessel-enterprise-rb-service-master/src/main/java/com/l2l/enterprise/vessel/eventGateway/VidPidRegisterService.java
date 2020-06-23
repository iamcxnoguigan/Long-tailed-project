package com.l2l.enterprise.vessel.eventGateway;

import com.l2l.enterprise.vessel.extension.activiti.annotation.AnnotationConnector;
import com.l2l.enterprise.vessel.extension.activiti.annotation.AnnotationIntergrationContextImpl;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.runtime.api.connector.Connector;
import org.activiti.runtime.api.model.IntegrationContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("vidPidRegisterService")
public class VidPidRegisterService implements AnnotationConnector {
    private static Logger logger = LoggerFactory.getLogger(VidPidRegisterService.class);
    @Autowired
    VidPidRegistry vidPidRegistry;

    @Autowired
    RuntimeService runtimeService;

    @Override
    public AnnotationIntergrationContextImpl execute(AnnotationIntergrationContextImpl integrationContext,DelegateExecution execution) {
        String pid = integrationContext.getProcessInstanceId();
        String vid = runtimeService.getVariable(execution.getId() , "vid").toString();
        vidPidRegistry.p2vMap.put(pid,vid);
        vidPidRegistry.v2pMap.put(vid,pid);
      //  integrationContext.getAnnotation().getInputVariables();
        return integrationContext;
    }
}
