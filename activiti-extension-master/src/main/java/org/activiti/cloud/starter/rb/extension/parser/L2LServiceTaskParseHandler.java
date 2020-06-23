package org.activiti.cloud.starter.rb.extension.parser;

import org.activiti.bpmn.model.ImplementationType;
import org.activiti.bpmn.model.ServiceTask;
import org.activiti.engine.impl.bpmn.parser.BpmnParse;
import org.activiti.engine.impl.bpmn.parser.handler.ServiceTaskParseHandler;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
