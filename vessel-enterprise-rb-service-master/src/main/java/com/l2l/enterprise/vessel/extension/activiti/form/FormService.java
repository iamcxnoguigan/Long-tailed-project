package com.l2l.enterprise.vessel.extension.activiti.form;

public interface FormService  {
    FormDefinition getStartForm(String processDefinitionId);
    FormDefinition getUserTaskForm(String processDefinitionId,String taskDefinitionKey );
}
