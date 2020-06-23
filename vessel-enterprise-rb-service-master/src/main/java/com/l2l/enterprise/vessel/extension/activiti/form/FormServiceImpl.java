package com.l2l.enterprise.vessel.extension.activiti.form;

import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.impl.ServiceImpl;
import org.activiti.engine.impl.cmd.GetBpmnModelCmd;
import org.springframework.stereotype.Service;


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
