package org.activiti.cloud.starter.rb.extension.annotation;


import org.activiti.cloud.starter.rb.extension.model.Annotation;
import org.activiti.cloud.starter.rb.extension.utils.L2LProcessDefinitionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class AnnotationCollector {
    private static Logger log = LoggerFactory.getLogger(AnnotationCollector.class);
    protected final  AnnotationService annotationService;
    public AnnotationCollector(AnnotationService annotationService){
        this.annotationService = annotationService;
    }

    public void collectAnnotaions(){
        List<String> pdIds = L2LProcessDefinitionUtil.getProcessDefinitionIdsOfAllVersions(annotationService.getL2LProcessEngineConfiguration());
        AnnotationManager annotationManager = annotationService.getL2LProcessEngineConfiguration().getAnnotationManager();
        for(String pdId: pdIds){
            List<Annotation> tAns = annotationService.getAllActivitiesAnnotations(pdId);
            annotationManager.getAnnotations().addAll(tAns);
        }
        log.debug("All annotations are collected from all deployed processes definitions");
    }
}
