package com.l2l.enterprise.vessel.config;

import com.amazonaws.services.iot.client.AWSIotException;
import com.amazonaws.services.iot.client.AWSIotMqttClient;
import com.amazonaws.services.iot.client.sample.sampleUtil.SampleUtil;
import com.amazonaws.services.iot.client.sample.sampleUtil.SampleUtil.KeyStorePasswordPair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.inject.Inject;

@Configuration
public class AWSConfiguration {
    private static  final Logger logger = LoggerFactory.getLogger(AWSConfiguration.class);
    @Inject
    private Environment environment;

    @Bean
    public AWSIotMqttClient awsIotMqttClient() throws AWSIotException, InterruptedException {
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
            KeyStorePasswordPair pair = SampleUtil.getKeyStorePasswordPair(certificateFile, privateKeyFile, algorithm);
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

        System.out.println(environment.getProperty("awsiot.clientEndpoint"));
        awsIotMqttClient.connect();

        return  awsIotMqttClient;
    }


}
