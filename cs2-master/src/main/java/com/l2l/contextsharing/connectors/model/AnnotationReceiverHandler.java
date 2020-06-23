package com.l2l.contextsharing.connectors.model;

import afu.org.checkerframework.checker.units.qual.A;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.l2l.contextsharing.aws.bean.AnnotationIntegrationRequestManager;
import com.l2l.contextsharing.aws.bean.awsClient;
import com.l2l.contextsharing.connectors.annotatoionDomain.AnnotationIntegrationRequestImpl;
import com.l2l.contextsharing.connectors.annotatoionDomain.AnnotationIntegrationResultImpl;
import com.l2l.contextsharing.connectors.channels.AnnotationMessageChannels;
import com.l2l.contextsharing.connectors.configuration.ConnectorProperties;
import com.l2l.contextsharing.connectors.value.ConsSet;
import com.l2l.contextsharing.connectors.value.ValueExchangeSet;
import com.l2l.contextsharing.connectors.value.processVar;
import com.l2l.contextsharing.connectors.value.valueType;
import org.apache.commons.lang.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

@Component
@EnableBinding(AnnotationMessageChannels.class)
public class AnnotationReceiverHandler {
    private final IntegrationResultSender integrationResultSender;
    private static org.slf4j.Logger log = LoggerFactory.getLogger(AnnotationReceiverHandler.class);
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private ConnectorProperties connectorProperties;
    @Autowired
    public awsClient client;
    @Autowired
    public AnnotationIntegrationRequestManager annotationIntegrationRequestManager;
    @Autowired
    public valueType type;
    @Autowired
    public processVar processVar;

    public AnnotationReceiverHandler(IntegrationResultSender integrationResultSender) {
        this.integrationResultSender = integrationResultSender;
    }
    private static String captureName(String str) {
        // 进行字母的ascii编码前移，效率要高于截取字符串进行转换的操作
        char[] cs=str.toCharArray();
        cs[0]-=32;
        return String.valueOf(cs);
    }

    @StreamListener(value = AnnotationMessageChannels.ANNOTAION_CONSUMER)
    public void rewardTopRankedUsers(AnnotationIntegrationRequestImpl event) throws JsonProcessingException, ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        // business logic goes here
        AnnotationIntegrationRequestImpl tv = null;
        if(event instanceof AnnotationIntegrationRequestImpl) {
            tv = (AnnotationIntegrationRequestImpl)event;
            log.debug(tv.getAppName());
        }//publish to aws uuid作为map的key 并在annotation中扩展这一个属性
        //iftt处理  将没有的变量向aws发布获取消息 处理完毕
        Annotation annotation = event.getAnnotationIntergrationContext().getAnnotation();
        String annkey = annotation.getId();
        annotationIntegrationRequestManager.getAnnotationIntegrationRequestMap().put(annkey,event);
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String outJson = mapper.writeValueAsString(annotation);
        System.out.println(outJson);
//        client.publish("arn:aws-cn:sns:cn-northwest-1:148543509440:test", outJson);

        //  价值传递的补充部分 需要特异性编写
        String typeName = annotation.getType();
        String constraints = annotation.getConstraints();
        String valueExchange = annotation.getValueExchange();
        String varList = annotation.getId();
        String[] vararray=varList.split(",");//首位是流程代号　mymuffine之类的　后几位依次是需要的变量名字
        typeName = vararray[0]+typeName;
        List<Integer> list = new ArrayList<>();
        constraints = StringUtils.strip(constraints, "[]");
        if(valueExchange == null){
//            Class cs = Class.forName("com.l2l.contextsharing.connectors.value.ConsSet");
            Class c = Class.forName("com.l2l.contextsharing.connectors.value." + typeName);
            for(int i = 1;i< vararray.length;i++) {
//                String methodName = this.captureName(vararray[i]);
                list.add(processVar.getVar(vararray[i]));
            }
//            Object conset = cs.newInstance();
            Object Obj = c.newInstance();
            Method method = c.getMethod("SetallVar",new Class[] { List.class });
            method.invoke(Obj , list);//c变量已经生成
//            String[] cons = constraints.split(",");
//            for(String con : cons){
//
//            }//这里是自定义解析cons字符串内容的部分  需要string按照空格分开 每次便利一个字符串 取值 若不是加减号 则流程名字加变量。
//  变量取值 是符号则先记录 取下下一个 运算 最终得到结果。
//            Method csmethod = cs.getMethod(constraints);

//            boolean flag = (boolean) csmethod.invoke(conset,Obj);
            boolean flag = new ConsSet().setMethod(constraints,Obj);
            if(!flag){
                System.out.println("异常情况发生于" + constraints);
            }
            type.setVar(typeName,Obj);
        }else{
            valueExchange = StringUtils.strip(valueExchange, "[]");
            String[] valueList = valueExchange.split(",");
            HashMap<String,String> valueExchangeMap = new HashMap<>();
            for(String s : valueList){
                s = StringUtils.strip(s,"()");
                String[] ms = s.split(":");
                valueExchangeMap.put(ms[0], ms[1]);
                String inputType = valueExchangeMap.get("input");
                Object object = type.getVar(inputType);
                Object outputObject = new ValueExchangeSet().setValueExchange(valueExchangeMap.get("name"),object);//得到交换器出来的变量对象
                boolean flag = new ConsSet().setMethod(constraints,outputObject);
                if(!flag){
                    System.out.println("异常情况发生于" + constraints);
                }
            }

        }


        Map<String, Object> results = new HashMap<>();
        results.put("rewards", "test");
        Message<AnnotationIntegrationResultImpl> message = AnnotationIntegrationResultBuilder.resultFor(event,
            connectorProperties)
            .withOutboundVariables(results)
            .buildMessage();
        integrationResultSender.send(message);
    }
}
