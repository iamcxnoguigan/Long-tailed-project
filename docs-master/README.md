# L2L Framework
Documentation and Utilities related to the L2L Framework.
- [Scenario](scenario.md)
- [Workshop](workshop.md)

## L2L Infrastructure
### Gateway
- [`repository addr`](https://github.com/i-qiqi/l2l-gateway)

### Registry
- [`repository addr`](https://github.com/i-qiqi/l2l-registry)
- registry bug : the front end is not present after the backend has started up.
```bash
# adjust to proper node version
nvm use 8
# add node-sass
yarn
yarn add node-sass
yarn start
# Ctrl + c , re-run registry
./gradlew
```

## L2L Core Components
### Vessel enterprise
Note : suggest that we can refer to the implementation [`ttc-rb-english-campaign`](https://github.com/Activiti/ttc-rb-english-campaign)
- [`repository addr`](https://github.com/i-qiqi/l2l-vesslel-enterprise)

### VMC(Vessel-Manager-Coordinator)
Note : suggest that we can refer to the implementation [ttc-connectors-reword](https://github.com/Activiti/ttc-connectors-reward)
- [`repository addr`](https://github.com/i-qiqi/l2l-vmc)

## L2L Frontend
## Reference
- [Activiti6-Guide-en](https://www.activiti.org/userguide/)
- [Activiti6-Guide-zn-5.16](http://www.cnblogs.com/boonya/p/4775471.html)
- [Activiti7 Guide](https://activiti.gitbook.io/activiti-7-developers-guide/components-architecture/activiti-cloud-infrastructure/gateway)
-  [Cloud native in kubernetes workshop](https://github.com/Activiti/ttc-docs/blob/develop/workshop.md)
- [L2L-v1 github repository](https://github.com/sonnyhcl/L2L)
