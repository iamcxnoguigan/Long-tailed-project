package org.activiti.cloud.starter.rb.extension.form;

public interface FormService  {
    FormDefinition getStartForm(String processDefinitionId);
    FormDefinition getUserTaskForm(String processDefinitionId, String taskDefinitionKey);
}
