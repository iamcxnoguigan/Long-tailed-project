# Developement Workshop
## Start RabbitMQ with Docker
Please ensure that you have install docker on your PC.
```bash
# Get image from pub repository
docker pull rabbitmq:management
# Run a rabbitmq instance
docker run -d --hostname my-rabbit --name rabbit -p 5672:5672 -p 15672:15672 rabbitmq:management
# Log in to rabbitmq client in browser
url : localhost:15672
usr:pwd -->guest:guest
```
## Intergration activiti cloud with JHipster.
- A bug related to swagger2 is reported as follow:
```bash
Caused by: java.lang.IllegalStateException: Multiple
 Dockets with the same group name are not supported.
  The following duplicate groups were discovere......
```
  - Exclude `io.springfox`dependencies and only keep them imported from `org activiti.cloud` , the bug seems  to have been fixed.

## Endpoints
- Before you access to any other endpoint, You shoud send an authentication request  to gateway for acquiring JWT  with `basic auth : admin:admin`.  if you test the endpoints in `Postman` , you must fill the `Bear Token` field with the token.
```bash
GET http://127.0.1.1:8080/api/authenticate
```
-  GET http://127.0.1.1:8080/api-vessel-rb/management/beansif －> Inspect the beans in spring container on the fly.

- GET <http://127.0.1.1:8080/api-vessel-rb/v1/process-definitions/> —> get all the **inmutable** list of process definitions available for this `vessel enterprise runtime bundle`.
- GET <http://127.0.1.1:8080/api-vessel-rb/v1/process-instances/> —>  get all the in-flight process instances
- POST http://127.0.1.1:8080/api-vessel-rb/v2/{processDefinitionId} —> start a process instance with the given `processDefinitionId`
