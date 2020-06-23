package com.l2l.enterprise.vessel.eventGateway;

import com.l2l.enterprise.vessel.domain.AcquireProcessBusinessInformationMsg;
import com.l2l.enterprise.vessel.domain.DelayMsg;
import com.l2l.enterprise.vessel.domain.THMsg;
import com.l2l.enterprise.vessel.eventGateway.channel.AcquireProcessBusinessInformationMsgChannel;
import com.l2l.enterprise.vessel.eventGateway.channel.DelayMsgChannel;
import com.l2l.enterprise.vessel.eventGateway.channel.THMsgChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Component;

@Component
@EnableBinding({DelayMsgChannel.class, THMsgChannel.class,AcquireProcessBusinessInformationMsgChannel.class})
public class msghandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(msghandler.class);
    @Autowired
    DelayMsgHandler delayMsgHandler;
    @StreamListener(DelayMsgChannel.DELAY_MSG)
    public void DelayMsgHandle(DelayMsg delayMsg){
        LOGGER.info("get");
        delayMsgHandler.handle(delayMsg);
    }
    @Autowired
    THMsgHandler thMsgHandler;

    @StreamListener(THMsgChannel.Temperature_and_humidity)
    public void magHandler(THMsg thMsg){
        LOGGER.info("get th");
        thMsgHandler.handle(thMsg);
    }

    @Autowired
    APBIMsgHandler apbiMsgHandler;

    @StreamListener(AcquireProcessBusinessInformationMsgChannel.APBI)
    public void magHandler(AcquireProcessBusinessInformationMsg acMsg){
        LOGGER.info("get th");
        apbiMsgHandler.handle(acMsg);
    }

}
//修改为集中接收点  bve所有信息通过一个或者多个通道发送 但是不根据通道的不同区分使用某种函数  接受到的消息都会有topic 和scenario 通过scenario+topic 反射的方式调用对应handler类 通常一个Handler中只有一个方法 多个方法通过step区分 msgmatch需要更改一下 map  <scenario:list<topic:list<ann>>>
//希望修改 但是无法解决统一接口的问题  在接受方需要指明接受对象的类型 这样代码复用太多
