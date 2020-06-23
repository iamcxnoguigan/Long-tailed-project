package com.l2l.enterprise.vessel.extension.activiti.annotation;

import com.l2l.enterprise.vessel.extension.activiti.boot.L2LProcessEngineConfiguration;
import com.l2l.enterprise.vessel.extension.activiti.model.Annotation;
import org.activiti.engine.impl.ServiceImpl;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.engine.impl.cmd.TriggerCmd;
import org.activiti.engine.impl.persistence.entity.integration.IntegrationContextEntity;

import java.util.List;
import java.util.Map;

public class AnnotationServiceImpl extends ServiceImpl implements  AnnotationService{
    public AnnotationServiceImpl(ProcessEngineConfigurationImpl processEngineConfiguration) {
        super(processEngineConfiguration);
    }

    @Override
    public List<Annotation> getActivityAnnotations(String processDefinitionId, String targetElementId) {
        return this.commandExecutor.execute(new GetActivityAnnotationsCmd(processDefinitionId ,targetElementId));
    }

//    @Override
//    public List<Annotation> getMsgAnnotations(String processDefinitionId) {
//        return this.commandExecutor.execute(new GetMsgAnnotationsCmd(processDefinitionId));
//    }

    @Override
    public List<Annotation> getAllActivitiesAnnotations(String processDefinitionId) {
        return this.commandExecutor.execute(new GetAllActivitiesAnnotationsCmd(processDefinitionId));
    }

    @Override
    public  List<MsgAnnotation> getAllMsgAnnotations(String processDefinitionId){
        return this.commandExecutor.execute((new GetMsgAnnotationsCmd(processDefinitionId)));
    }

    @Override
    public L2LProcessEngineConfiguration getL2LProcessEngineConfiguration(){
        return  (L2LProcessEngineConfiguration)this.processEngineConfiguration;
    }
    @Override
    public void trigger(String executionId,IntegrationContextEntity integrationContextEntity,AnnotationIntergrationContextImpl annotationIntergrationContext) {
        this.commandExecutor.execute(new AnnotationTrigglerCmd(executionId,integrationContextEntity ,annotationIntergrationContext,(Map)null));
    }
    @Override
    public void trigger(String executionId, IntegrationContextEntity integrationContextEntity,AnnotationIntergrationContextImpl annotationIntergrationContext,Map<String, Object> processVariables) {
        this.commandExecutor.execute(new AnnotationTrigglerCmd(executionId, integrationContextEntity,annotationIntergrationContext,processVariables));
    }
    @Override
    public void trigger(String executionId, IntegrationContextEntity integrationContextEntity,AnnotationIntergrationContextImpl annotationIntergrationContext,Map<String, Object> processVariables, Map<String, Object> transientVariables) {
        this.commandExecutor.execute(new AnnotationTrigglerCmd(executionId,integrationContextEntity,annotationIntergrationContext,processVariables,transientVariables));
    }
}
