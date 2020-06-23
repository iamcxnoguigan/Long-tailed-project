package com.l2l.enterprise.vessel.extension.activiti.annotation;

import com.l2l.enterprise.vessel.extension.activiti.boot.L2LProcessEngineConfiguration;
import com.l2l.enterprise.vessel.extension.activiti.model.Annotation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DefaultAnnotationManagerImpl implements AnnotationManager {
    protected L2LProcessEngineConfiguration processEngineConfiguration;
    protected List<Annotation> annotations;
    protected List<MsgAnnotation> msgAnnotations = new ArrayList<>();
    // distinct annotations by type ...
    protected Map<String , List<Annotation>> activityAnnotationCache;
    protected Map<String , List<Annotation>> msgAnnotationCache;

    protected Object behavior;

    public DefaultAnnotationManagerImpl(){

    }
    public L2LProcessEngineConfiguration getProcessEngineConfiguration() {
        return processEngineConfiguration;
    }

    public void setProcessEngineConfiguration(L2LProcessEngineConfiguration processEngineConfiguration) {
        this.processEngineConfiguration = processEngineConfiguration;
    }

    public List<Annotation> getAnnotations() {
        return annotations;
    }

    public void setAnnotations(List<Annotation> annotations) {
        this.annotations = annotations;
    }


    public Object getBehavior() {
        return this.behavior;
    }

    public void setBehavior(Object behavior) {
        this.behavior = behavior;
    }

    public List<MsgAnnotation> getMsgAnnotations() {
        return msgAnnotations;
    }

    public void setMsgAnnotations(List<MsgAnnotation> msgAnnotations) {
        this.msgAnnotations = msgAnnotations;
    }
}
