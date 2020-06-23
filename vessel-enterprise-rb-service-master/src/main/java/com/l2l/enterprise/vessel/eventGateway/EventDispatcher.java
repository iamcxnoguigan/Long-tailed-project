package com.l2l.enterprise.vessel.eventGateway;

import com.amazonaws.services.iot.client.AWSIotMessage;
import com.amazonaws.services.iot.client.AWSIotQos;
import com.amazonaws.services.iot.client.AWSIotTopic;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.l2l.enterprise.vessel.extension.activiti.annotation.MsgAnnotation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@SuppressWarnings("all")
public class EventDispatcher extends AWSIotTopic {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private static ObjectMapper objectMapper = new ObjectMapper();

    private EventHandler eventHandler;

    private Map<String, List<MsgAnnotation>> msgAnnoationMap;

    public void setMsgAnnoationMap(Map<String, List<MsgAnnotation>> msgAnnoationMap) {
        this.msgAnnoationMap = msgAnnoationMap;
    }

    public EventDispatcher(String topic, AWSIotQos qos) {
        super(topic, qos);
    }

    public  void setEventHandler(EventHandler eventHandler){
        this.eventHandler = eventHandler;
    }

    @Autowired
    private MsgMatch msgMatch;

    @Override
    public void onMessage(AWSIotMessage message) {
        String receivedTopic = message.getTopic();
//        if(this.msgAnnoationMap == null){
//            setMsgAnnoationMap(msgMatch.initMsgAnnotationMap());
//        }
//        if(this.msgAnnoationMap.containsKey(receivedTopic)){
//
//        }
        try {
            if(receivedTopic.equals("$aws/things/V413362260/shadow/update/accepted")){
                updateShadowHandler(message);
            }
            if(receivedTopic.equals("IoT/V413362260/status")){
                changeStatusHandler(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateShadowHandler(AWSIotMessage message) throws IOException {
        //TODO:update vessel shadow and forward shadow to Monitor
        this.eventHandler.vesselShadowForwarding(message);
    }

    public void changeStatusHandler(AWSIotMessage message) throws IOException {
        //TODO: report the signal of reaching port to process engine
        this.eventHandler.changeStatus(message);
    }




}
