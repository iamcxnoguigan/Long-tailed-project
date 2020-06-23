package org.activiti.cloud.starter.rb.extension.form;

import org.activiti.bpmn.model.FormProperty;

public class FormUtils {
    public static  FormField toFormField(FormProperty formProperty){
        FormField formField = new FormField();
        formField.setId(formProperty.getId());
        formField.setType(formProperty.getType());
        formField.setName(formProperty.getName());
        formField.setReadOnly(formProperty.isReadable());
        formField.setRequired(formProperty.isRequired());
        return formField;
    }
}
