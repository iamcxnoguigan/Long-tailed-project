package com.l2l.enterprise.vessel.extension.activiti.boot;

import com.l2l.enterprise.vessel.extension.activiti.agenda.L2LActivitiEngineAgendaFactory;
import com.l2l.enterprise.vessel.extension.activiti.annotation.DefaultAnnotationBehavior;
import com.l2l.enterprise.vessel.extension.activiti.annotation.AnnotationManager;
import com.l2l.enterprise.vessel.extension.activiti.annotation.DefaultAnnotationManagerImpl;
import com.l2l.enterprise.vessel.extension.activiti.cache.L2LDeploymentCache;
import com.l2l.enterprise.vessel.extension.activiti.listener.InternalSampleEventListener;
import com.l2l.enterprise.vessel.extension.activiti.model.Annotation;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.delegate.event.ActivitiEventType;
import org.activiti.spring.SpringProcessEngineConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;

import java.util.ArrayList;
import java.util.List;

public class L2LProcessEngineConfiguration extends SpringProcessEngineConfiguration {
    private static Logger log = LoggerFactory.getLogger(L2LProcessEngineConfiguration.class);
    protected List<Annotation>  annotations; // delete
    protected AnnotationManager annotationManager;
    protected ApplicationEventPublisher eventPublisher;

    public L2LProcessEngineConfiguration(){

    }

    protected void initAnnotationManager(){
        if(this.annotations == null){
            this.annotations = new ArrayList<Annotation>();
        }
        if(this.annotationManager == null){
            this.annotationManager = new DefaultAnnotationManagerImpl();
            this.annotationManager.setAnnotations(this.annotations);
            this.annotationManager.setProcessEngineConfiguration(this);
        }
    }
    protected void postProcessEngineInitialisation() {
        initAnnotationManager();
        super.postProcessEngineInitialisation();
        this.eventDispatcher.addEventListener(new InternalSampleEventListener(eventPublisher), new ActivitiEventType[]{ActivitiEventType.ENTITY_INITIALIZED});
    }
    protected void autoDeployResources(ProcessEngine processEngine) {
        super.autoDeployResources(processEngine);
        postAutoDeployment();
    }

    public void initProcessDefinitionCache() {
        if (this.processDefinitionCache == null) {
            if (this.processDefinitionCacheLimit <= 0) {
                this.processDefinitionCache = new L2LDeploymentCache();
            } else {
                this.processDefinitionCache = new L2LDeploymentCache(this.processDefinitionCacheLimit);
            }
        }

    }

    public void initAgendaFactory() {
        if (this.engineAgendaFactory == null) {
            this.engineAgendaFactory = new L2LActivitiEngineAgendaFactory();
        }

    }


    public ApplicationEventPublisher getEventPublisher() {
        return eventPublisher;
    }

    public void setEventPublisher(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    public List<Annotation> getAnnotations() {
        return annotations;
    }

    public void setAnnotations(List<Annotation> annotations) {
        this.annotations = annotations;
    }

    public AnnotationManager getAnnotationManager() {
        return annotationManager;
    }

    public void setAnnotationManager(AnnotationManager annotationManager) {
        this.annotationManager = annotationManager;
    }

    protected  void postAutoDeployment(){
    }

}
