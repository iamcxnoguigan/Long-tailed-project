package com.l2l.contextsharing.aws.Controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.l2l.contextsharing.aws.bean.awsClient;
import com.l2l.contextsharing.aws.bean.receiver;
import com.l2l.contextsharing.aws.util.Message;
import com.l2l.contextsharing.connectors.model.Annotation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;
import java.util.*;

import static com.l2l.contextsharing.aws.util.MessageReadUtil.readAnnotationFromJson;
import static com.l2l.contextsharing.aws.util.iftttutil.convertToCode;

@RestController//处理http请求，返回json格式
@RequestMapping
public class varFromAws {
    private static Logger log = Logger.getLogger("log");
    @Autowired
    public receiver re;
    @Autowired
    public awsClient client;
    @RequestMapping(value = "/varFromAws", method = RequestMethod.POST)
    @ResponseBody
    public void getVar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SecurityException {//接受来自aws的额外流程变量
        Message message = re.doPost(request,response);//需要控制台筛选策略
        if(message.getType().equals("SubscriptionConfirmation")){
//            || !message.getSubject().isEmpty()|| !message.getSubject().equals("vars")){
            return;
        }
        Annotation annotation = readAnnotationFromJson(message.getMessage());
        if(annotation == null) {
         return;
        }
        //ifttt处理逻辑
        String s = annotation.getIftttRules();
        s.replace("\t","");
        StringBuilder builder = new StringBuilder(s);
        List<StringBuilder> list = new ArrayList<>();
        int head = 0,end;
        for(int i =0;i<builder.length();i++){
            if(builder.charAt(i) == '{') head =i;
            if(builder.charAt(i) == '}'){
                end = i;
                list.add(new StringBuilder(builder.substring(head,end+1)));
            }
        }
        for(StringBuilder rule : list){
            int h=0,e;
            String expression = null,topic = null;
            for (int i = 0;i<rule.length();i++){
                if(rule.substring(i,i+2).equals("if")){
                    i = i+3;
                    h = i;
                    while(rule.charAt(i) != ','){i++;}
                    e = i;
                    expression = rule.substring(h,e);
                }
                if(rule.substring(i,i+4).equals("then")){
                    i = i+5;
                    h = i;
                    while (rule.charAt(i) != '}'){i++;}
                    e = i;
                    topic = rule.substring(h,e);
                }
            }
            Object code = convertToCode(expression,annotation.getAwsVariables());
            if((boolean)code){
                annotation.setTopic(topic);
                log.info("publish aws for topic");
                ObjectMapper mapper = new ObjectMapper();
                mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
                String outJson = mapper.writeValueAsString(annotation);
                System.out.println(outJson);
                client.publish("arn:aws-cn:sns:cn-northwest-1:148543509440:test3",outJson);
                annotation.setTopic(null);
            }
        }
    }
}
