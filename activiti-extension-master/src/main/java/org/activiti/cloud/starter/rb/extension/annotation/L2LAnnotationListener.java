package org.activiti.cloud.starter.rb.extension.annotation;

import org.activiti.bpmn.model.ExtensionElement;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("l2LAnnotationListener")
public class L2LAnnotationListener implements ExecutionListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(L2LAnnotationListener.class);
    @Override
    public void notify(DelegateExecution delegateExecution) {
        Map<String , List<ExtensionElement>> extensionElements = delegateExecution.getCurrentFlowElement().getExtensionElements();
        //only one annotation per one activity by default.
//        Map<String, List<ExtensionAttribute>> attributes = extensionElements.get(AnnotationConstants.ELEMENT_NAME).get(0).getAttributes();
//        String script = attributes.get(AnnotationConstants.ATTR_SCRIPT).get(0).getValue().trim();
//        String language = attributes.get(AnnotationConstants.ATTR_LANGUAGE).get(0).getValue().trim();
//        if(script != null && language != null){
//            if(!AnnotationConstants.LAN_TYPE_GROOVY.equals(language) && ! AnnotationConstants.LAN_TYPE_JAVASCRIPT.equals(language)){
//                LOGGER.debug("unsupported script language!!!");
//                return;
//            }
//            LOGGER.debug("---" + language+" language---");
//            ScriptingEngines scriptingEngines = Context.getProcessEngineConfiguration().getScriptingEngines();
//            try {
//                Object result = scriptingEngines.evaluate(script, language, delegateExecution, false);
//
//                Message<String> result = annotationSender.annotationSend(script);
//            } catch (ActivitiException var6) {
//                LOGGER.warn("Exception while executing " + delegateExecution.getCurrentFlowElement().getId() + " : " + var6.getMessage());
//                Throwable rootCause = ExceptionUtils.getRootCause(var6);
//                if (!(rootCause instanceof BpmnError)) {
//                    throw var6;
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//        }

    }
}
