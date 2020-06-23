package org.activiti.cloud.starter.rb.extension.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EventListenerConfiguration {
    private static Logger log = LoggerFactory.getLogger(EventListenerConfiguration.class);

    public EventListenerConfiguration() {

    }

    /**
     * register event listener for listening to `ActivitiEventType.ENTITY_INITIALIZED`
     * @param runtimeService
     * @return
     */
//    @Bean
//    public InitializingBean registerVariableCreatedListenerDelegate(RuntimeService runtimeService) {
//        return () -> {
//            runtimeService.addEventListener(new InternalSampleEventListener() ,  new ActivitiEventType[]{ActivitiEventType.ENTITY_INITIALIZED});
//        };
//    }
}
