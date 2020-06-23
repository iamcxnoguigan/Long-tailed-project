package com.l2l.contextsharing.aws.util;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.l2l.contextsharing.connectors.model.Annotation;

import java.io.IOException;

public class MessageReadUtil {
    public static Annotation readAnnotationFromJson(String json){
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        try {

            Annotation annotation = objectMapper.readValue(json, Annotation.class);

            return annotation;

        } catch (IOException e) {

            return null;
        }

    }

    public static Message readMessageFromJson(String json) {

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            try {

                Message msg = objectMapper.readValue(json, Message.class);

                return msg;

            } catch (IOException e) {

                return null;
            }

        }




}
