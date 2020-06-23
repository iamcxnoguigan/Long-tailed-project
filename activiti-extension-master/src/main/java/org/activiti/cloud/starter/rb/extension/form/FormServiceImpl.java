package org.activiti.cloud.starter.rb.extension.form;

import org.activiti.engine.impl.ServiceImpl;


public class FormServiceImpl extends ServiceImpl implements FormService {
    public FormServiceImpl() {
    }
    @Override
    public FormDefinition getStartForm(String processDefinitionId) {
        return (FormDefinition)this.commandExecutor.execute(new GetStartFormCmd(processDefinitionId));
    }
    @Override
    public FormDefinition getUserTaskForm(String processDefinitionId,String taskDefinitionKey){
        return (FormDefinition)this.commandExecutor.execute(new GetTaskFormCmd(processDefinitionId,taskDefinitionKey));
    }
}
