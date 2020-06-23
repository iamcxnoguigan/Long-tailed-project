package com.l2l.contextsharing.aws.bean;

import com.l2l.contextsharing.connectors.annotatoionDomain.AnnotationIntegrationRequestImpl;
import com.l2l.contextsharing.connectors.model.Annotation;
import org.springframework.stereotype.Component;
import java.util.*;

@Component
public class AnnotationIntegrationRequestManager {
    private Map<String,AnnotationIntegrationRequestImpl> annotationIntegrationRequestMap = new HashMap<>();

    public AnnotationIntegrationRequestManager() {

    }

    public Map<String, AnnotationIntegrationRequestImpl> getAnnotationIntegrationRequestMap() {
        return annotationIntegrationRequestMap;
    }

    public void setAnnotationIntegrationRequestMap(Map<String, AnnotationIntegrationRequestImpl> annotationIntegrationRequestMap) {
        this.annotationIntegrationRequestMap = annotationIntegrationRequestMap;
    }
}
