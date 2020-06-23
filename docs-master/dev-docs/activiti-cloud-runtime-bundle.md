# Activiti Cloud Runtime Bundle
## Official Introduction
*`Runtime Bundle is the Cloud version of the Process Engine`* , if you ever exposed Activiti as a service, you were is defining a Runtime Bundle. The new key features as follow:
- Runtime Bundles in the context of Activiti Cloud represent a stateless instance of the process engine which is in charge of excuting *`an immutable  set of process definitions`*.
- **You cannot deploy new process definitions to a Runtime Bundle, instead you will create a new immutable version of your Runtime Bundle if you want to update your process definitions**.
- Runtime Bundles expose a (Sync) REST and (Async) Message Based API to interact with them.
- Runtime Bundles emit events(**in a fire & forget  fashion**) using a set of implementatios of the internal `ActivitiEventListener` interface. (*Listen to internal Process Engine events and transform them into messages containing all the events generated inside a transaction*).
-  Runtime Bundles, by default when excuting Service Tasks(BPMN), will emit `intergration Events` to perform System to System intergration. These intergration Events will be picked up by `Activiti Cloud Connectors` to perform system to system intergration.

## Intergation with JHipster
### Set of the required dependencies.
- Minmum set with gradle , and other dependencies are provisioned via JHipster Microservices by default.
```java
dependencyManagement {
  imports {
  mavenBom 'org.activiti.cloud:activiti-cloud-starter-runtime-bundle:'+activiti_cloud_version  }
}
dependencies{
  compile "org.activiti.cloud:activiti-cloud-services-api"
  compile "org.activiti:activiti-engine"
  compile "org.activiti:activiti-spring-boot-starter"
  compile "org.activiti.cloud:activiti-cloud-services-events"
  compile "org.activiti.cloud:activiti-cloud-services-rest-impl"
  compile "org.activiti.cloud:activiti-cloud-services-connectors"
  compile "org.activiti.cloud:activiti-cloud-services-subscriptions"
}
```

### Event Dispatcher Mechanism
- [Cloud Event Listeners](https://www.processon.com/view/link/5b7d4a7fe4b0d4d65be23316)
- [Internal Event Listeners](https://www.processon.com/view/link/5bc861fee4b08faf8c82aaea)

### Impementation scheme of annotation
- annota
