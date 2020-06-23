package com.l2l.enterprise.vessel.coordinator;

import com.amazonaws.services.iot.client.AWSIotException;
import com.amazonaws.services.iot.client.AWSIotMessage;
import com.amazonaws.services.iot.client.AWSIotMqttClient;
import com.amazonaws.services.iot.client.AWSIotQos;
import com.amazonaws.services.iot.client.sample.sampleUtil.SampleUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.l2l.enterprise.vessel.domain.Destination;
import com.l2l.enterprise.vessel.eventGateway.EventPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AwsClient {
    private static  final Logger logger = LoggerFactory.getLogger(AwsClient.class);
    private static final String delayTopic = "activiti/delay";
    private static final String initTopic = "activiti/vessel/init";
    private static final String voyagingTopic = "activiti/vessel/voyaging";

    @Autowired
    private AWSIotMqttClient awsIotMqttClient;

    @Autowired
    private ObjectMapper objectMapper;


//    public  AwsClient(Environment environment) throws AWSIotException {
//        this.awsIotMqttClient = createMqttClient(environment);
//    }


    /**
     * delay or postpone
     * @param msgType
     * @param to
     * @param destinations
     */
    public void sendDestinations(String msgType , String to , List<Destination> destinations) throws JsonProcessingException {
        String payload = null;
        ObjectNode payloadObjectNode = objectMapper.createObjectNode();
        payloadObjectNode.put("msgType", msgType);
        payloadObjectNode.put("To", to);
        payloadObjectNode.putPOJO("destinations", objectMapper.writeValueAsString(destinations));
        payload = payloadObjectNode.toString();
        logger.debug(payload);
        AWSIotMessage pub = new EventPublisher("IoT/V"+to+"/delay", AWSIotQos.QOS0, payloadObjectNode.toString());
        try {
            awsIotMqttClient.publish(pub);
        } catch (AWSIotException e) {
            e.printStackTrace();
        }
        logger.debug("destinations is sent");
    }


    public  void sendInitiation(String msgTyoe , String to , int defaultDelayHour , int zoomInVal){
        ObjectNode payloadObjectNode = objectMapper.createObjectNode();
        payloadObjectNode.put("msgType" , msgTyoe);
        payloadObjectNode.put("To" , to);
        payloadObjectNode.put("defaultDelayHour" , defaultDelayHour);
        payloadObjectNode.put("zoomInVal" ,  zoomInVal);
        logger.debug("payload :"+payloadObjectNode.toString());
        AWSIotMessage pub = new EventPublisher("IoT/V"+to+"/track", AWSIotQos.QOS0, payloadObjectNode.toString());
        try {
            awsIotMqttClient.publish(pub);
        } catch (AWSIotException e) {
            e.printStackTrace();
        }
    }

    public void notifyVoyaging(String msgTyoe , String to ){
        ObjectNode payloadObjectNode = objectMapper.createObjectNode();
        payloadObjectNode.put("msgType", msgTyoe);
        payloadObjectNode.put("To", to);
        logger.debug("payload :" + payloadObjectNode.toString());
        AWSIotMessage pub = new EventPublisher("IoT/V"+to+"/voyaging", AWSIotQos.QOS0, payloadObjectNode.toString());
        try {
            awsIotMqttClient.publish(pub);
        } catch (AWSIotException e) {
            e.printStackTrace();
        }
    }


    private AWSIotMqttClient createMqttClient(Environment environment) throws AWSIotException {
        AWSIotMqttClient awsIotMqttClient = null;
        String clientEndpoint = environment.getProperty("awsiot.clientEndpoint");
        String clientId = environment.getProperty("awsiot.clientId");
        String certificateFile = environment.getProperty("awsiot.certificateFile");
        String privateKeyFile = environment.getProperty("awsiot.privateKeyFile");
        String algorithm = environment.getProperty("keyAlgorithm");
        String awsAccessKeyId = environment.getProperty("awsAccessKeyId");
        String awsSecretAccessKey = environment.getProperty("awsSecretAccessKey");
        String sessionToken = environment.getProperty("sessionToken");
        if (awsIotMqttClient == null && certificateFile != null && privateKeyFile != null) {
            SampleUtil.KeyStorePasswordPair pair = SampleUtil.getKeyStorePasswordPair(certificateFile, privateKeyFile, algorithm);
            awsIotMqttClient = new AWSIotMqttClient(clientEndpoint, clientId, pair.keyStore, pair.keyPassword);
        }
        if (awsIotMqttClient == null) {
            if (awsAccessKeyId != null && awsSecretAccessKey != null) {
                awsIotMqttClient = new AWSIotMqttClient(clientEndpoint, clientId, awsAccessKeyId, awsSecretAccessKey,
                        sessionToken);
            }
        }

        if (awsIotMqttClient == null) {
            throw new IllegalArgumentException("Failed to construct client due to missing certificate or credentials.");
        }

        awsIotMqttClient.setWillMessage(new AWSIotMessage("client/disconnect", AWSIotQos.QOS0, awsIotMqttClient.getClientId()));
        awsIotMqttClient.connect();
        return  awsIotMqttClient;
    }

    public static String getDelayTopic() {
        return delayTopic;
    }

    public static String getInitTopic() {
        return initTopic;
    }

    public static String getVoyagingTopic() {
        return voyagingTopic;
    }

    public AWSIotMqttClient getAwsIotMqttClient() {
        return awsIotMqttClient;
    }

    public void setAwsIotMqttClient(AWSIotMqttClient awsIotMqttClient) {
        this.awsIotMqttClient = awsIotMqttClient;
    }

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }
}
