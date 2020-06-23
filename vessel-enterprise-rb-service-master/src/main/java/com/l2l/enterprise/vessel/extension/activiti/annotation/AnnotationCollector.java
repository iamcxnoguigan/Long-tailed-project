package com.l2l.enterprise.vessel.extension.activiti.annotation;

import com.l2l.enterprise.vessel.extension.activiti.model.Annotation;
import com.l2l.enterprise.vessel.extension.activiti.utils.L2LProcessDefinitionUtil;
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
            List<MsgAnnotation> mAns = annotationService.getAllMsgAnnotations(pdId);
            annotationManager.getMsgAnnotations().addAll(mAns);
        }
        log.debug("All annotations are collected from all deployed process definitions");
    }
}
