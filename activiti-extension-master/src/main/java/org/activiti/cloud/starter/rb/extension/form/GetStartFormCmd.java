package org.activiti.cloud.starter.rb.extension.form;

import org.activiti.bpmn.model.FormProperty;
import org.activiti.bpmn.model.Process;
import org.activiti.bpmn.model.StartEvent;
import org.activiti.engine.ActivitiIllegalArgumentException;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.util.ProcessDefinitionUtil;

import java.io.Serializable;
import java.util.List;

public class GetStartFormCmd implements Command<FormDefinition>, Serializable{
    protected String processDefinitionId;
    public GetStartFormCmd(String processDefinitionId){
        this.processDefinitionId = processDefinitionId;
    }
    @Override
    public FormDefinition execute(CommandContext commandContext) {
        if (this.processDefinitionId == null) {
            throw new ActivitiIllegalArgumentException("processDefinitionId is null");
        } else {
            Process process = ProcessDefinitionUtil.getProcess(this.processDefinitionId);
            List<StartEvent> startEvents = process.findFlowElementsOfType((StartEvent.class) , false);
            FormDefinition formDefinition = new FormDefinition();
            if(startEvents.size() > 0){
                StartEvent startEvent = startEvents.get(0);
                List<FormProperty> formProperties = startEvent.getFormProperties();
                formProperties.forEach((formProperty)->{
                    FormField formField = FormUtils.toFormField(formProperty);
                    formDefinition.getFields().add(formField);

                });
                return formDefinition;
            }else{
                throw new ActivitiIllegalArgumentException("StartEvent is  null");
            }
        }
    }
}
