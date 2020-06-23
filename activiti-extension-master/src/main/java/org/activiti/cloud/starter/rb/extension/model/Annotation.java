package org.activiti.cloud.starter.rb.extension.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Annotation {
    protected String id = UUID.randomUUID().toString();
    protected String name;
    protected String pointcutType;
    protected String implementationType;
    protected String destination;
    protected String handler;
    protected String script;
    private String processDefinitionId;
    protected String targetElementId;
    protected List<String> inputVariables = new ArrayList();
    protected List<String> outputVariables = new ArrayList();

    public Annotation(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPointcutType() {
        return pointcutType;
    }

    public void setPointcutType(String pointcutType) {
        this.pointcutType = pointcutType;
    }

    public String getTargetElementId() {
        return targetElementId;
    }

    public void setTargetElementId(String targetElementId) {
        this.targetElementId = targetElementId;
    }

    public String getImplementationType() {
        return implementationType;
    }

    public void setImplementationType(String implementationType) {
        this.implementationType = implementationType;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public List<String> getInputVariables() {
        return inputVariables;
    }

    public void setInputVariables(List<String> inputVariables) {
        this.inputVariables = inputVariables;
    }

    public List<String> getOutputVariables() {
        return outputVariables;
    }

    public void setOutputVariables(List<String> outputVariables) {
        this.outputVariables = outputVariables;
    }
    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script = script;
    }

    public Annotation(String handler) {
        this.handler = handler;
    }

    public String getHandler() {
        return handler;
    }

    public void setHandler(String handler) {
        this.handler = handler;
    }

    public String getProcessDefinitionId() {
        return processDefinitionId;
    }

    public void setProcessDefinitionId(String processDefinitionId) {
        this.processDefinitionId = processDefinitionId;
    }
}
