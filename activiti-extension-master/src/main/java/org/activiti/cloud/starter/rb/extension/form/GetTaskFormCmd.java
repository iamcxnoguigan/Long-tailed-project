package org.activiti.cloud.starter.rb.extension.form;

import org.activiti.bpmn.model.FormProperty;
import org.activiti.bpmn.model.Process;
import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.ActivitiIllegalArgumentException;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.util.ProcessDefinitionUtil;

import java.io.Serializable;
import java.util.List;

public class GetTaskFormCmd implements Command<FormDefinition>, Serializable {
    protected String processDefinitionId;
    protected String taskDefinitionKey;
    public GetTaskFormCmd(String processDefinitionId ,String taskDefinitionKey){
        this.processDefinitionId = processDefinitionId;
        this.taskDefinitionKey = taskDefinitionKey;
    }
    @Override
    public FormDefinition execute(CommandContext commandContext) {
        if (this.processDefinitionId == null) {
            throw new ActivitiIllegalArgumentException("processDefinitionId is null");
        } else {
            Process process = ProcessDefinitionUtil.getProcess(this.processDefinitionId);
            List<UserTask> userTasks= process.findFlowElementsOfType((UserTask.class) , false);
            FormDefinition formDefinition = new FormDefinition();
            Boolean flag = false;
            if(userTasks.size() > 0){
                for(UserTask userTask : userTasks){
                    if(userTask.getId().equals(taskDefinitionKey)){
                        List<FormProperty> formProperties = userTask.getFormProperties();
                        formProperties.forEach((formProperty)->{
                                FormField formField = FormUtils.toFormField(formProperty);
                                formDefinition.getFields().add(formField);
                            });
                    }
                }
                return formDefinition;

            }else{
                throw new ActivitiIllegalArgumentException("UserTask is  null");
            }
        }
    }
}
