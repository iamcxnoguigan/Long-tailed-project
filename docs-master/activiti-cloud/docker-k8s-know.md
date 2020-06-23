 # kubernetes
 - 几乎所有的资源对象都可以通过Kubernetes提供kubectl工具执行CRUD并将其保存在etcd的持久化存储中；
 - k8s是一个高度自动化的资源控制系统，通过追踪对比etcd库中保存的"资源期望状态"与当前环境中的
 "实际资源状态"的差异来实现自动控制和自动纠错的高级功能
 ## Master
 ### 1. API Server
 -	对外提供REST API , 对所有资源对象CRUD操作的唯一入口
 ### 2. Controller Manager
 ### 3. Scheduler
 - 负责pod调度到Node上
 ### 4. etcd服务
 ## Node
 ### 1. kubelete
 -	负责pod对应容器的创建，启停等
 - kubelet会定时像Master节点汇报所在Node的状态
 ### 2. kube-proxy
 - 实现Service通信与负载均衡机制
 ### 3. Docker Engine
 ## 资源对象
 ### Pod
 普通的pod一旦创建会被放入etcd中存储，随后会被k8s调度得到某个具体的Node上binding,随后kubelet进程实例化一组相关的docker容器并启动起来
 一个Pod的创建，调度，绑定节点，在目标Node上启动对应容器的过程需要一定时间，我们期待系统启动N个Pod副本的目标状态，实际上是一个连续变化的"部署过程"导致的最终状态

 - 一个特殊的pause容器，被称为**根容器**: 引入业务无关并且不易死亡的Pause容器作为pod的根容器，以它的状态表示整个容器组的整体状态
 - 一个或多个业务相关的容器: 共享Pause容器的pod IP以及挂在的Volume
 - 一个Pod里的容器与另外主机上的Pod可以直接通信：k8s要求底层网络支持集群内任意两个Pod之间的TCP/IP直接通信，依赖于虚拟二层网络技术，比如Flannel
 ### Label
 LableSelector, 选择符合标签匹配规则的资源对象
 - kube-controller进程通过RC上定义Label Selector来筛选要监控的Pod副本的数量，从而实现Pod副本数量始终符合预期状态
 - kube-proxy进程通过Service的Label Selector来选择对应的Pod,自动建立起每个Service到对应Pod的请求转发路由表，从而实现Service的智能负载均衡
 - 通过对某些Node上定义特定的Label, 并且在Pod定义文件中使用NodeSelector这种标签调度策略, kube-scheduler进程可以实现Pod“定向调度”的特性
### RC / RS
定义了一个期望场景中声明某种Pod的副本数量在任意时刻都符合预期状态,RS支持集合的Label selector
- Pod 预期的副本数Replicas，　用于筛选目标Pod的Label Selector，　当Pod的副本数小于预期数量时，用于创建新Pod 的Pod模板
- 自动控制：Label Selector机制
- 扩/缩容：改变RC里的Pod副本数量
- 滚动升级 : 通过改变RC中Pod模板的镜像版本
### Deployment
内部使用了RS实现，可以看做是RC升级版，升级点在于可以随时知道Pod”部署“进度
- 创建一个Deployment对象来生成对应RS并完成Pod副本的创建过程
- 检查Deployment状态来看部署动作是否完成
- 更新Deployment的状态创建新的Pod（镜像升级）
### Service
k8s中的每个Service其实就是一个微服务吧　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　
### Stateful Set
- 每个Pod都有稳定的，唯一的网络标示
-　所控制的启停顺序是受控的，操作第n个pod的时候，前n-1个pod已经是运行且准备好的状态
- 采用稳定的持久化PV,删除Pod时，默认不会删除与StatefulSet相关的存储卷
### Horizontal Pod Autoscaler(HPA)
- 手动扩容: kubectl scale
- 自动扩容：kubectl autoscale deployment xxx --cpu-percent=90 --min=1 --max=10
