# Runtime Bundle Extension
This project is intend to develop(for extension), test, and package the `Activiti Runtime Bundle Service`.
To Package the extension of the original `activiti-runtime-bundle-service` in jar, you can go through steps as follow.
- make sure the extension works well alongside the original `activiti-runtime-bundle-service'.
- run `mvn clean package` to package the extension not including its dependencies.
- install the generated jar into the local maven repository. Note that the `-Dfile` denotes the jar file path on your own host
```
mvn install:install-file -Dfile=l2l-activiti-cloud-starter-runtime-bundle-7.0.0.Beta1.jar 
-DgroupId=org.activiti.l2l -DartifactId=l2l-activiti-runtime-bundle-service -Dversion=7.0.0.
-Dpackaging=jar
```
- import the dependency into other projects, eg. in gradle.
```
compile "org.activiti.l2l:l2l-activiti-runtime-bundle-service:7.0.0.Beta1"
```
