package org.activiti.cloud.starter.rb.extension.parser;

import org.activiti.bpmn.converter.ServiceTaskXMLConverter;
import org.activiti.bpmn.converter.util.BpmnXMLUtil;
import org.activiti.bpmn.model.*;
import org.apache.commons.lang3.StringUtils;

import javax.xml.stream.XMLStreamReader;
import java.util.Arrays;
import java.util.List;

/**
 * Customize the ServiceTaskXMLConverter to parse extensive attributes , like  "annotation".
 * @author bqzhu
 */
public class L2LServiceTaskXMLConverter extends ServiceTaskXMLConverter {
    protected static final List<ExtensionAttribute> defaultElementAttributes = Arrays.asList(
        new ExtensionAttribute("http://www.l2l.com" , "l2l:annotation")
    );

    protected BaseElement convertXMLToElement(XMLStreamReader xtr, BpmnModel model) throws Exception {
        ServiceTask serviceTask = new ServiceTask();
        BpmnXMLUtil.addXMLLocation(serviceTask, xtr);
        if (StringUtils.isNotEmpty(xtr.getAttributeValue("http://activiti.org/bpmn", "class"))) {
            serviceTask.setImplementationType(ImplementationType.IMPLEMENTATION_TYPE_CLASS);
            serviceTask.setImplementation(xtr.getAttributeValue("http://activiti.org/bpmn", "class"));
        } else if (StringUtils.isNotEmpty(xtr.getAttributeValue("http://activiti.org/bpmn", "expression"))) {
            serviceTask.setImplementationType(ImplementationType.IMPLEMENTATION_TYPE_EXPRESSION);
            serviceTask.setImplementation(xtr.getAttributeValue("http://activiti.org/bpmn", "expression"));
        } else if (StringUtils.isNotEmpty(xtr.getAttributeValue("http://activiti.org/bpmn", "delegateExpression"))) {
            serviceTask.setImplementationType(ImplementationType.IMPLEMENTATION_TYPE_DELEGATEEXPRESSION);
            serviceTask.setImplementation(xtr.getAttributeValue("http://activiti.org/bpmn", "delegateExpression"));
        } else if ("##WebService".equals(xtr.getAttributeValue((String)null, "implementation"))) {
            serviceTask.setImplementationType(ImplementationType.IMPLEMENTATION_TYPE_WEBSERVICE);
            serviceTask.setOperationRef(this.parseOperationRef(xtr.getAttributeValue((String)null, "operationRef"), model));
        } else {
            serviceTask.setImplementation(xtr.getAttributeValue((String)null, "implementation"));
        }

        serviceTask.setResultVariableName(xtr.getAttributeValue("http://activiti.org/bpmn", "resultVariableName"));
        if (StringUtils.isEmpty(serviceTask.getResultVariableName())) {
            serviceTask.setResultVariableName(xtr.getAttributeValue("http://activiti.org/bpmn", "resultVariable"));
        }

        serviceTask.setType(xtr.getAttributeValue("http://activiti.org/bpmn", "type"));
        serviceTask.setExtensionId(xtr.getAttributeValue("http://activiti.org/bpmn", "extensionId"));
        if (StringUtils.isNotEmpty(xtr.getAttributeValue("http://activiti.org/bpmn", "skipExpression"))) {
            serviceTask.setSkipExpression(xtr.getAttributeValue("http://activiti.org/bpmn", "skipExpression"));
        }

        BpmnXMLUtil.addCustomAttributes(xtr , serviceTask , defaultElementAttributes);
        this.parseChildElements(this.getXMLElementName(), serviceTask, model, xtr);
        return serviceTask;
    }

    protected String getXMLElementName() {
        return "serviceTask";
    }


//    @Test
//    public void printType(){
//        System.out.println("type　: " + getBpmnElementType());
//    }


}
