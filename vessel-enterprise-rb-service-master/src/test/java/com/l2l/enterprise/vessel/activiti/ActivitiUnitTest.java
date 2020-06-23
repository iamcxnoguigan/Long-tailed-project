package com.l2l.enterprise.vessel.activiti;

import com.l2l.enterprise.vessel.VesselApp;
import com.l2l.enterprise.vessel.extension.activiti.parser.L2LServiceTaskXMLConverter;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.ExtensionAttribute;
import org.activiti.bpmn.model.ExtensionElement;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.impl.util.io.InputStreamSource;
import org.activiti.engine.repository.ProcessDefinition;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertNotNull;

/**
 * Tests for  Activiti engine API
 * @bqzhu
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = VesselApp.class)
@ActiveProfiles("dev")
public class ActivitiUnitTest {
    private static final Logger log = LoggerFactory.getLogger(ActivitiUnitTest.class);

    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private RepositoryService repositoryService;
    @Test
    public void deploy(){
        List<ProcessDefinition> pds = repositoryService.createProcessDefinitionQuery().list();
        System.out.println("pds size = "+pds.size());
        pds.forEach(pd->{
            System.out.println(pd.toString());
        });
        System.out.println("just for test"+runtimeService);
    }

    @Test
    public void TestServiceTaskAnnotationAttr(){
        //process document
        InputStream xmlStream = this.getClass().getClassLoader().getResourceAsStream("annotation.bpmn");
        assertNotNull(xmlStream);
        InputStreamSource xmlSource = new InputStreamSource(xmlStream);
        // Instantiate the BpmnXMLConverter
        BpmnXMLConverter bpmnXMLConverter = new BpmnXMLConverter();
        BpmnXMLConverter.addConverter(new L2LServiceTaskXMLConverter());
        BpmnModel bpmnModel = bpmnXMLConverter.convertToBpmnModel(xmlSource , true , false , "UTF-8");
        FlowElement flowElement = bpmnModel.getProcesses().get(0).getFlowElement("serviceTask1");
        Map<String , List<ExtensionAttribute>> attributes = flowElement.getAttributes();
        List<ExtensionAttribute> list = attributes.get("annotation");
        log.debug("name : "+list.get(0).getName()+ " , value : "+list.get(0).getValue());
    }

    @Test
    public void ServiceTaskExtensionElementUnitTest(){
        //process document
        InputStream xmlStream = this.getClass().getClassLoader().getResourceAsStream("annotation.bpmn20.xml");
        assertNotNull(xmlStream);
        InputStreamSource xmlSource = new InputStreamSource(xmlStream);
        // Instantiate the BpmnXMLConverter
        BpmnXMLConverter bpmnXMLConverter = new BpmnXMLConverter();
        BpmnXMLConverter.addConverter(new L2LServiceTaskXMLConverter());
        BpmnModel bpmnModel = bpmnXMLConverter.convertToBpmnModel(xmlSource , true , false , "UTF-8");
        FlowElement flowElement = bpmnModel.getProcesses().get(0).getFlowElement("_3");
        Map<String , List<ExtensionElement>> extensionElements = flowElement.getExtensionElements();
        extensionElements.forEach((key , val1)->{
            if(key.equals("annotation")){
                val1.forEach(val2->{
                    val2.getAttributes().values().forEach(extensionAttributes -> {
                        extensionAttributes.forEach(extensionAttribute -> {
                            System.out.println(extensionAttribute.getName()+" : "+extensionAttribute.getValue());
                        });
                    });
                });
            }
        });
    }

}
