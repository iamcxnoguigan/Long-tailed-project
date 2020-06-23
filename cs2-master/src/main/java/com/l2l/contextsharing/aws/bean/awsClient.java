package com.l2l.contextsharing.aws.bean;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import com.amazonaws.services.sns.model.SubscribeRequest;
import com.l2l.contextsharing.aws.util.Message;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.net.URL;
import java.security.Signature;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.logging.Logger;

@Component
public class awsClient {

    public static AmazonSNS snsClient = AmazonSNSClientBuilder.defaultClient();
    public void setSnsClient(){
        AmazonSNS snsClient = AmazonSNSClientBuilder.defaultClient();
    }

    public AmazonSNS getSnsClient() {
        return snsClient;
    }

    public void publish(String topicArn, String msg){
        PublishRequest publishRequest = new PublishRequest(topicArn, msg);
//MessageAttributes 用于筛选
        PublishResult publishResponse = this.snsClient.publish(publishRequest);

// Print the MessageId of the message.
        System.out.println("MessageId: " + publishResponse.getMessageId());
    }
    public void subscribe(String topicArn, String protocol, String endpoint){
        final SubscribeRequest subscribeRequest = new SubscribeRequest(topicArn, protocol, endpoint);
        this.snsClient.subscribe(subscribeRequest);

// Print the request ID for the SubscribeRequest action.
        System.out.println("SubscribeRequest: " + this.snsClient.getCachedResponseMetadata(subscribeRequest));
        System.out.println("To confirm the subscription, check your endpoint.");

    }
}
