package com.l2l.enterprise.vessel.extension.activiti.parser;

import com.l2l.enterprise.vessel.extension.activiti.boot.L2LProcessEngineConfiguration;
import com.l2l.enterprise.vessel.extension.activiti.model.Annotation;
import org.activiti.bpmn.model.*;
import org.activiti.engine.impl.bpmn.parser.BpmnParse;
import org.activiti.engine.impl.bpmn.parser.handler.ServiceTaskParseHandler;
import org.activiti.engine.impl.context.Context;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class L2LServiceTaskParseHandler extends ServiceTaskParseHandler {
    private static final Logger log = LoggerFactory.getLogger(L2LServiceTaskParseHandler.class);

    protected void executeParse(BpmnParse bpmnParse, ServiceTask serviceTask) {
        log.debug("---executeParse---");

        if (StringUtils.isNotEmpty(serviceTask.getType())) {
            this.createActivityBehaviorForServiceTaskType(bpmnParse, serviceTask);
        } else if (ImplementationType.IMPLEMENTATION_TYPE_CLASS.equalsIgnoreCase(serviceTask.getImplementationType())) {
            this.createClassDelegateServiceTask(bpmnParse, serviceTask);
        } else if (ImplementationType.IMPLEMENTATION_TYPE_DELEGATEEXPRESSION.equalsIgnoreCase(serviceTask.getImplementationType())) {
            this.createServiceTaskDelegateExpressionActivityBehavior(bpmnParse, serviceTask);
        } else if (ImplementationType.IMPLEMENTATION_TYPE_EXPRESSION.equalsIgnoreCase(serviceTask.getImplementationType())) {
            this.createServiceTaskExpressionActivityBehavior(bpmnParse, serviceTask);
        } else if (ImplementationType.IMPLEMENTATION_TYPE_WEBSERVICE.equalsIgnoreCase(serviceTask.getImplementationType()) && StringUtils.isNotEmpty(serviceTask.getOperationRef())) {
            this.createWebServiceActivityBehavior(bpmnParse, serviceTask);
        } else {
            this.createDefaultServiceTaskActivityBehavior(bpmnParse, serviceTask);
        }


    }
}
