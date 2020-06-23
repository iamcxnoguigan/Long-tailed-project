package com.l2l.enterprise.vessel.extension.activiti.model;

import java.util.*;

public class Annotation {
    public String id = UUID.randomUUID().toString();
    public String name;
    public String pointcutType;
    public String implementationType;
    public String destination;
    public String handler;
    public String script;
    public String processDefinitionId;
    public String targetElementId;
    public String policy;
    public Map<String,Object> awsVariables = new HashMap<>();//map
    public Map<String,Object> processVariables = new HashMap<>();
    public String iftttRules;
    public String iothub;
    public String topic;
    public Map<String,Object> executionvars = new HashMap<>();
    public String valueExchange;
    public String type;
    public String constraints;

    @Override
    public String toString() {
        return '{' +
            "id='" + id + '\'' +
            ", name='" + name + '\'' +
            ", pointcutType='" + pointcutType + '\'' +
            ", implementationType='" + implementationType + '\'' +
            ", destination='" + destination + '\'' +
            ", handler='" + handler + '\'' +
            ", script='" + script + '\'' +
            ", processDefinitionId='" + processDefinitionId + '\'' +
            ", targetElementId='" + targetElementId + '\'' +
            ", policy='" + policy + '\'' +
            ", awsVariables=" + null +
            ", processVariables=" + null +
            ", iftttRules='" + iftttRules + '\'' +
            ", IOThub='" + iothub + '\'' +
            ", Topic='" + topic + '\'' +
            ", valueExchange='" + valueExchange + '\'' +
            ", type='" + type + '\'' +
            ", constraints='" + constraints + '\'' +
            '}';
    }

    public String toJson(){
        return '{' +
            "\"id\""+ ":\"" + id + '\"' +
            ", \"name\"" + ":\"" + name + '\"' +
            ", \"pointcutType\"" + ":\"" + pointcutType + '\"' +
            ", \"implementationType\"" + ":\"" + implementationType + '\"' +
            ", \"destination\"" + ":\"" + destination + '\"' +
            ", \"handler\"" + ":\"" + handler + '\"' +
            ", \"script\"" + ":\"" + script + '\"' +
            ", \"processDefinitionId\"" + ":\"" + processDefinitionId + '\"' +
            ", \"targetElementId\"" + ":\"" + targetElementId + '\"' +
            ", \"policy\"" + ":\"" + policy + '\"' +
            ", \"awsVariables\"" + ":\"" + awsVariablestoString() + '\"' +
            ", \"processVariables\"" + ":\"" + processVariablestoString() + '\"' +
            ", \"iftttRules\"" + ":\"" + iftttRules + '\"' +
            ", \"IOThub\"" + ":\"" + iothub + '\"' +
            ", \"Topic\"" + ":\"" + topic + '\"' +
            ", \"valueExchange\"" + ":\"" + valueExchange + '\"' +
            ", \"type\"" + ":\"" + type + '\"' +
            ", \"constraints\"" + ":\"" + constraints + '\"' +
            '}';
    }

    public Annotation(){

    }


    public Map<String, Object> getExecutionvars() {
        return executionvars;
    }

    public void setExecutionvars(Map<String, Object> executionvars) {
        this.executionvars = executionvars;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String awsVariablestoString(){
        if(!this.getAwsVariables().isEmpty()){
            StringBuilder builder = new StringBuilder();
            for (Map.Entry<String, Object> entry : this.getAwsVariables().entrySet()) {
                builder.append(entry.getKey()+":"+entry.getValue()+",");
            }
            builder.deleteCharAt(builder.length()-1);
            return String.valueOf(builder);
        }
        return null;
    }
    public String processVariablestoString(){
        if(!this.getAwsVariables().isEmpty()){
            StringBuilder builder = new StringBuilder();
            for (Map.Entry<String, Object> entry : this.getProcessVariables().entrySet()) {
                builder.append(entry.getKey()+":"+entry.getValue()+",");
            }
            builder.deleteCharAt(builder.length()-1);
            return String.valueOf(builder);
        }return null;
    }
    public String getIOThub() {
        return iothub;
    }

    public void setIOThub(String IOThub) {
        this.iothub = IOThub;
    }

    public String getPolicy() {
        return policy;
    }

    public void setPolicy(String policy) {
        this.policy = policy;
    }

    public Map<String, Object> getAwsVariables() {
        return awsVariables;
    }

    public void setAwsVariables(Map<String, Object> awsVariables) {
        this.awsVariables = awsVariables;
    }

    public Map<String, Object> getProcessVariables() {
        return processVariables;
    }

    public void setProcessVariables(Map<String, Object> processVariables) {
        this.processVariables = processVariables;
    }

    public String getIftttRules() {
        return iftttRules;
    }

    public void setIftttRules(String iftttRules) {
        this.iftttRules = iftttRules;
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

    public String getValueExchange() {
        return valueExchange;
    }

    public void setValueExchange(String valueExchange) {
        this.valueExchange = valueExchange;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getConstraints() {
        return constraints;
    }

    public void setConstraints(String constraints) {
        this.constraints = constraints;
    }
}
