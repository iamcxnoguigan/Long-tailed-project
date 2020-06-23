package com.l2l.enterprise.vessel.extension.activiti.annotation;

import com.l2l.enterprise.vessel.extension.activiti.boot.L2LProcessEngineConfiguration;
import com.l2l.enterprise.vessel.extension.activiti.form.FormDefinition;
import com.l2l.enterprise.vessel.extension.activiti.model.Annotation;
import org.activiti.engine.impl.persistence.entity.integration.IntegrationContextEntity;

import java.util.List;
import java.util.Map;

public interface AnnotationService {
    List<Annotation>  getActivityAnnotations(String processDefinitionId , String targetElementId);

//    List<Annotation> getMsgAnnotations(String processDefinitionId);

    List<Annotation> getAllActivitiesAnnotations(String processDefinitionId);

    L2LProcessEngineConfiguration getL2LProcessEngineConfiguration();

    List<MsgAnnotation> getAllMsgAnnotations(String processDefinitionId);

    void trigger(String var1,IntegrationContextEntity integrationContextEntity,AnnotationIntergrationContextImpl annotationIntergrationContext);

    void trigger(String var1,IntegrationContextEntity integrationContextEntity,AnnotationIntergrationContextImpl annotationIntergrationContext, Map<String, Object> var2);

    void trigger(String var1, IntegrationContextEntity integrationContextEntity, AnnotationIntergrationContextImpl annotationIntergrationContext,Map<String, Object> var2, Map<String, Object> var3);
}
