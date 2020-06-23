package com.l2l.enterprise.vessel.eventGateway;

import com.l2l.enterprise.vessel.extension.activiti.annotation.MsgAnnotation;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Component
public class MsgAnnotationUtil {
    private final MsgMatch msgMatch;

    public MsgAnnotationUtil(MsgMatch msgMatch) {
        this.msgMatch = msgMatch;
    }

    public List<MsgAnnotation> getMmp(String topic){
        Map<String, List<MsgAnnotation>> msgAnnoationMap = new HashMap<>();
        List<MsgAnnotation> mmp = new ArrayList<>();
        msgAnnoationMap = msgMatch.initMsgAnnotationMap();
        for(Map.Entry<String,List<MsgAnnotation>> entry : msgAnnoationMap.entrySet()){
            if(entry.getKey().equals(topic)){

//                if(delayMsg.getMsgAnnotationList() == null) delayMsg.setMsgAnnotationList(new ArrayList<MsgAnnotation>());
                mmp.addAll(entry.getValue());
            }
        }
        return mmp;
    }
}
