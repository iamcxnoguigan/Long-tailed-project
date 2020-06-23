package com.l2l.enterprise.vessel.extension.activiti.form;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
