package org.activiti.cloud.starter.rb.extension.annotation;



import org.activiti.cloud.starter.rb.extension.boot.L2LProcessEngineConfiguration;
import org.activiti.cloud.starter.rb.extension.model.Annotation;

import java.util.List;

public interface AnnotationManager  {

    L2LProcessEngineConfiguration getProcessEngineConfiguration();

   void setProcessEngineConfiguration(L2LProcessEngineConfiguration processEngineConfiguration);

   List<Annotation> getAnnotations();

   void setAnnotations(List<Annotation> annotations);

   void setBehavior(Object behavior);

   Object getBehavior();

}
