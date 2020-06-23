package com.l2l.contextsharing.aws.Controller;


import com.l2l.contextsharing.connectors.annotatoionDomain.AnnotationIntegrationRequestImpl;
import com.l2l.contextsharing.connectors.annotatoionDomain.AnnotationIntegrationResultImpl;
import com.l2l.contextsharing.connectors.configuration.ConnectorProperties;
import com.l2l.contextsharing.connectors.model.Annotation;
import com.l2l.contextsharing.connectors.model.AnnotationIntegrationResultBuilder;
import com.l2l.contextsharing.connectors.model.IntegrationResultSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.l2l.contextsharing.aws.bean.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import static com.l2l.contextsharing.aws.util.MessageReadUtil.readAnnotationFromJson;

@RestController//处理http请求，返回json格式
@RequestMapping//配置url，让该类下的所有接口url都映射在/users下
public class Receive {
    private static Logger log = Logger.getLogger("log");
    @Autowired
    public receiver re;
    @Autowired
    private ConnectorProperties connectorProperties;
    @Autowired
    private IntegrationResultSender integrationResultSender;
    @Autowired
    private AnnotationIntegrationRequestManager annotationIntegrationRequestManager;

    @RequestMapping(value = "/receive", method = RequestMethod.POST)
    @ResponseBody
    public void receive(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SecurityException {

        com.l2l.contextsharing.aws.util.Message message = re.doPost(request,response);
        if(message.getType().equals("SubscriptionConfirmation") ){
//            || !message.getSubject().isEmpty() || !message.getSubject().equals("annotation")){
            return;
        }
        //这里需要 message 去除换行 所有字符到同一行 方便json转化
        Annotation annotation = readAnnotationFromJson(message.getMessage());
        if(annotation == null){
            return;
        }
        Map<String, Object> results = new HashMap<>();
        //读取msatrbuit 包装成event intergration 创建event
        results.put("rewards", "test");//也许可以去除
        if(annotationIntegrationRequestManager.getAnnotationIntegrationRequestMap().containsKey(annotation.getId())){
            AnnotationIntegrationRequestImpl event = annotationIntegrationRequestManager.getAnnotationIntegrationRequestMap().get(annotation.getId());
            event.getAnnotationIntergrationContext().setAnnotation(annotation);
            Message<AnnotationIntegrationResultImpl> msg = AnnotationIntegrationResultBuilder.resultFor(event,
                connectorProperties)
                .withOutboundVariables(results)
                .buildMessage();
            integrationResultSender.send(msg);
            annotationIntegrationRequestManager.getAnnotationIntegrationRequestMap().remove(annotation.getId());
//            annotationIntegrationRequestManager.getAnnotationIntegrationRequestMap().remove(annotation.getId());
        }else{
            log.info("no matched annotationRequest");
        }

    }

}
