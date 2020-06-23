package org.activiti.cloud.starter.rb.extension.form;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class FormDefinition implements Serializable {
    private static final long serialVersionUID = 1L;

    protected List<FormField> fields = new ArrayList<FormField>();


    public FormDefinition() {
    }

    public List<FormField> getFields() {
        return this.fields;
    }

    public void setFields(List<FormField> fields) {
        this.fields = fields;
    }
}
