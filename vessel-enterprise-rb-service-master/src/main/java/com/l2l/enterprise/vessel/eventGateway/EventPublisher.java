package com.l2l.enterprise.vessel.eventGateway;

import com.amazonaws.services.iot.client.AWSIotMessage;
import com.amazonaws.services.iot.client.AWSIotQos;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class extends {@link AWSIotMessage} to provide customized handlers for
 * non-blocking message publishing.
 *
 * @Author bqzhu
 */
public class EventPublisher extends AWSIotMessage {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public EventPublisher(String topic, AWSIotQos qos, String payload) {
        super(topic, qos, payload);
    }

    @Override
    public void onSuccess() {
        super.onSuccess();
    }

    @Override
    public void onFailure() {
        super.onFailure();
        logger.error(topic+" : fail");
    }

    @Override
    public void onTimeout() {
        super.onTimeout();
        logger.error(topic+" : timeout");
    }
}
