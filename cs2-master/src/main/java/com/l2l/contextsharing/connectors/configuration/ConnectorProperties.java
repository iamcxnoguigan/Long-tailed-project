package com.l2l.contextsharing.connectors.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConnectorProperties {
    @Value("${spring.application.name")
    private String serviceName;
    @Value("${activiti.cloud.service.type:}")
    private String serviceType;
    @Value("${activiti.cloud.service.version:}")
    private String serviceVersion;
    @Value("${activiti.cloud.application.name:}")
    private String appName;
    @Value("${activiti.cloud.application.version:}")
    private String appVersion;

    public ConnectorProperties() {
    }

    public String getServiceName() {
        return this.serviceName;
    }

    public String getServiceFullName() {
        return this.serviceName;
    }

    public String getServiceType() {
        return this.serviceType;
    }

    public String getServiceVersion() {
        return this.serviceVersion;
    }

    public String getAppName() {
        return this.appName;
    }

    public String getAppVersion() {
        return this.appVersion;
    }
}

