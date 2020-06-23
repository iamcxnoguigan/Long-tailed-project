package com.l2l.enterprise.vessel.eventGateway;

import com.l2l.enterprise.vessel.extension.activiti.annotation.AnnotationService;
import com.l2l.enterprise.vessel.extension.activiti.annotation.MsgAnnotation;
import com.l2l.enterprise.vessel.extension.activiti.boot.L2LProcessEngineConfiguration;
import com.l2l.enterprise.vessel.extension.activiti.utils.L2LProcessDefinitionUtil;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class MsgMatch {
    //接受topic 返回 annotation 对应的 pdid 以及场景

    protected final AnnotationService annotationService;

    private Map<String,List<MsgAnnotation>> msgAnnotationMap = new HashMap<>();//map <topic,list<anno>>

    public Map<String, List<MsgAnnotation>> getMsgAnnotationMap() {
        return msgAnnotationMap;
    }

    public void setMsgAnnotationMap(Map<String, List<MsgAnnotation>> msgAnnotationMap) {
        this.msgAnnotationMap = msgAnnotationMap;
    }

    public MsgMatch(AnnotationService annotationService) {
        this.annotationService = annotationService;
    }

    public AnnotationService getAnnotationService() {
        return annotationService;
    }


    public Map<String, List<MsgAnnotation>> initMsgAnnotationMap() {//初始化获得所有msgann
        List<String>  allPdId = L2LProcessDefinitionUtil.getProcessDefinitionIdsOfAllVersions(annotationService.getL2LProcessEngineConfiguration());
        Map<String,List<MsgAnnotation>> msgAnnoMap = new HashMap<>();
        for(String PdId : allPdId){
            List<MsgAnnotation> msgAnnotations = new ArrayList<>();
            msgAnnotations = annotationService.getAllMsgAnnotations(PdId);
            for(MsgAnnotation msgAnno :msgAnnotations){
                String topic = msgAnno.getTopic();
                if(msgAnnoMap.containsKey(topic)){
                    msgAnnoMap.get(topic).add(msgAnno);
                }else {
                    List<MsgAnnotation> msgAnnos = new ArrayList<>();
                    msgAnnos.add(msgAnno);
                    msgAnnoMap.put(topic,msgAnnos);
                }
            }
            setMsgAnnotationMap(msgAnnoMap);
        }
        return msgAnnoMap;
    }


}
