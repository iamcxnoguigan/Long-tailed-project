package com.l2l.enterprise.vessel.eventGateway.channel;

import afu.org.checkerframework.checker.units.qual.A;
import com.l2l.enterprise.vessel.domain.DelayMsg;
import com.l2l.enterprise.vessel.eventGateway.EventAssember;
import com.l2l.enterprise.vessel.eventGateway.MsgEventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Component;

@Component
@EnableBinding({DelayMsgChannel.class})
public class DelayChannel {
//    @Autowired
//    EventAssember eventAssember;
//
//    @Autowired
//    MsgEventHandler eventHandler;
//    @StreamListener(value = "delaymsgConsumer")
//    public void comfirmDelayMsg(DelayMsg delayMsg){
////        eventAssember.handle("delaymsgConsumer" , delayMsg);
//
//        eventHandler.comfirmDelayMsg(delayMsg);
//    }
//    @StreamListener(value = "delayDestinationUpdate")
//    public void delayDestinationUpdate(DelayMsg delayMsg){
////        eventAssember.handle("delaymsgConsumer" , delayMsg);
//
//        eventHandler.delayDestinationUpdate(delayMsg);
//    }
}
