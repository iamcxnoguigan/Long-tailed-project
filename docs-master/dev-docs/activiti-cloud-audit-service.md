# Activiti Cloud Audit Service
## Official Introduction
  - The `Audit Service` module provides `Audit capabilities`. This module is in charge of consume one or more `Runtime Bundle Events` and  store them into the `Event Store`.
  - Our default implementation consists of a simple `JPA implementation` which consumes messages emitted by Runtime Bundles and store them as they arrive. The Audit Service doesn't do any data manipulation.
  - There is also a `MongoDB implementation`, which makes your life easier if you want to query events that are stored in JSON format.\

## Intergation with JHipster
### Set of the required dependencies.
- Minmum set with gradle , and other dependencies are provisioned via JHipster Microservices by default.
```java
dependencyManagement {
  imports {
    mavenBom 'org.activiti.cloud:activiti-cloud-starter-audit:'+ activiti_cloud_version
  }
}
dependencies{
   compile "org.activiti.cloud:activiti-cloud-services-audit-jpa"
   compile "com.querydsl:querydsl-core"
}
```
