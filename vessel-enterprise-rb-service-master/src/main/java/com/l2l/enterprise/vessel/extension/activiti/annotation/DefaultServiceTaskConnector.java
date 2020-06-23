package com.l2l.enterprise.vessel.extension.activiti.annotation;

import org.activiti.runtime.api.connector.Connector;
import org.activiti.runtime.api.model.IntegrationContext;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service("defaultServiceTaskConnector")
public class DefaultServiceTaskConnector implements Connector {
    @Override
    public IntegrationContext execute(IntegrationContext integrationContext) {
        System.out.println("execute service task...");
        return integrationContext;
    }
}
