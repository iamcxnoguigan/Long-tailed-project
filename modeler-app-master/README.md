# Modeler App
We borrow the modeler developed by Flowable Open Software to model BPMN , CMMN and DMN.
# How to use the modeler?
- **Step1** : The modeler works ahead of the start up of the [flowable-idm-app](https://github.com/i-qiqi/flowabe-idm-app/tree/master), 
We have offered the runable jar of the idm app under the root directory. you just run the following cmd to start it up. you can log in the idm app with default
credentials `admin/test` and url <http://localhost:8080/flowable-idm/index.html#/login>  
    ```
    java -jar flowable-idm.jar --spring.datasource.username=<your own username> --spring.datasource.password=<your own password>
    ```
- **Step2** :  run the following cmd to start up the modler app. you should adjust the configuration of `spring-boot-maven-plugin`  to  alternate default credentials
in `pom.xml`, we use the `mysql` profile by default, you can search the `mysql` profile in `pom.xml` to find the `spring.datasource`configuration.
    ```
    mvn clean install -Pmysql -DskipTests spring-boot:run
    ```
