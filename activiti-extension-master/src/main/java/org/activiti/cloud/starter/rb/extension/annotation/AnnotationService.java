package org.activiti.cloud.starter.rb.extension.annotation;



import org.activiti.cloud.starter.rb.extension.boot.L2LProcessEngineConfiguration;
import org.activiti.cloud.starter.rb.extension.model.Annotation;

import java.util.List;

public interface AnnotationService {
    List<Annotation>  getActivityAnnotations(String processDefinitionId, String targetElementId);

    List<Annotation> getMsgAnnotations(String processDefinitionId);

    List<Annotation> getAllActivitiesAnnotations(String processDefinitionId);

    L2LProcessEngineConfiguration getL2LProcessEngineConfiguration();
}
