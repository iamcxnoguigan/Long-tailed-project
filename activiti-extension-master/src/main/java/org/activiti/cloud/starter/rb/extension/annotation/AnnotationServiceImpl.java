package org.activiti.cloud.starter.rb.extension.annotation;




import org.activiti.cloud.starter.rb.extension.boot.L2LProcessEngineConfiguration;
import org.activiti.cloud.starter.rb.extension.model.Annotation;
import org.activiti.engine.impl.ServiceImpl;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;

import java.util.List;

public class AnnotationServiceImpl extends ServiceImpl implements  AnnotationService{
    public AnnotationServiceImpl(ProcessEngineConfigurationImpl processEngineConfiguration) {
        super(processEngineConfiguration);
    }

    @Override
    public List<Annotation> getActivityAnnotations(String processDefinitionId, String targetElementId) {
        return this.commandExecutor.execute(new GetActivityAnnotationsCmd(processDefinitionId ,targetElementId));
    }

    @Override
    public List<Annotation> getMsgAnnotations(String processDefinitionId) {
        return this.commandExecutor.execute(new GetMsgAnnotationsCmd(processDefinitionId));
    }

    @Override
    public List<Annotation> getAllActivitiesAnnotations(String processDefinitionId) {
        return this.commandExecutor.execute(new GetAllActivitiesAnnotationsCmd(processDefinitionId));
    }

    @Override
    public L2LProcessEngineConfiguration getL2LProcessEngineConfiguration(){
        return  (L2LProcessEngineConfiguration)this.processEngineConfiguration;
    }
}
