# Hierarchical BPMN Message Routing Implementation against Diverse Scenarios
> Here we discuss the message routing spanning the third-party systems and  the process engine of the host system

### Messaging Routing Schematic
To see the schematic diagram:  :point_right: <https://www.processon.com/view/link/5be3d30fe4b0993bf7213010/>  


### Msg-Exchange
- First we assume that the enterprise system has two roles, end users and business analysts. the end users use the traditional modeler to create one process definition(PD) and submit it to the system. Then the business analysts attach different `Msg-Exchanges` and `annotations` to the  process against diverse scenarios and take responsiblity for the deployment, operation, and monitoring of the process.
- An example of the `Msg-Exchange` structure:
```xml
<process id="vessel-process1" name="vessel-process" isExecutable="true">
<exchange scenario="cold-chain">
      <message topic= "/temperature">
          <annotation script="do something" inputvars="temprature , ..." ouputvars="pv1,...">
            </annotation>
      </message>
</exchange>
<exchange scenario="IoT">
      <message topic= "/vid/delay">
          <annotation script="do something" inputvars="duration, ..." ouputvars="delay_time,...">
            </annotation>
      </message>
</exchange>
  // ignore other default bpmn elements
    ...
</process>
```

### Message Broker at the Systems-to-Systems layer
- The external messages is published  from third-party systems,  including the `IoT Hub` and other `Enterprise Information Systems(EIS)`. Our host system can subscribe to these messages autonomously, Here are two examples, one for temperature - messages and one for delay messages.
  - `Temperature message` : <`topic`: "/temperature" ,`type`: "cooling" , `temperature`: "10℃" >
  - `Delay message` : < `topic` : "/vid/delay" , `type`: "postpone"，`duration`: "1h" >
-  All async messages from the external systems are given equal treatment, regardless of its business scenario.

### Event Gateway at the process engine layer
- Event Gateway takes responsibility for identifying the business scenarios  to which the exteranal messages belongs. Note that maybe the external messages belongs to more than one scenario.
- Event Gateway will assemble all scenario and message declarations  in the  Msg-Exchanges of the process definitions. And one Msg-Exchange corresponds to one scenarion.
- For example, when the process definition(PD) is deployed into the process engine, the engine will parse the `Msg-Exchange`. if the PD declares the `exchange` element with the spcefic scenario. Following the above  `Msg-Exchange` example, the engine will map the message "/temperature" to the scenario `cold-chain` , and `/vid/delay` to `IoT`. Event Gateway will maintain these mapping infomation.

### Scenario Aggregator at the process definition layer
- The Scenario Aggregator  is in charge of identifying  the target Process Pefinition(PD) to which  the message will flow.  Note  that the PD can declare many Msg-Exchanges as long as they are inclusive in business level.
- The Scenario Aggregator will collect all PDs with the same scenario in the  Msg-Exchanges of the process definitions.
- For example, In the deployment phrase of the PD1, the paser will parse the Msg-Exchanges of the PD1,  if the parser find the PD1 declares the message.  the `scenario ID` will be mapped to the `PD ID`. Ultimately, the one scenario will correspond to a set of PDs. And with the  known`PD ID` and `scenario ID`, It's easy to know what external messages the specific process definition declares under specific scenario .

### Interested Message Selector at the process instance layer
- Interested Message Selector(IMS) is responsible for identifying the target Process Instances(PI) interested in the external messages, the IMS help each PI  maintain an Interested Messages Collection(IMC) at runtime. the IMC of the PI is a subset  of its PD's IMC.
- For example, Although the PD declares IMC that needs to be subscribed under diverse scenarios , we also allow process instances to ultimately determine the messages they are interested in at runtime. when the "/temperatature" comes , the PI1 will do something according to the decision the corresponding annotation declare, but the PI2 just ignore.
