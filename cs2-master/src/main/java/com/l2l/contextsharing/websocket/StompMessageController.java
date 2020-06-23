package com.l2l.contextsharing.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.l2l.contextsharing.Greeting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.security.Principal;

@Controller
public class StompMessageController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    public SimpMessagingTemplate simpMessagingTemplate;

    //Just for testing the communication of websocket
    @MessageMapping("/hello")
    public void test(String str) {
        System.out.println("***********/hello**********" + " : " + str);
//        simpMessagingTemplate.convertAndSend("/topic/greetings" , "haha...");
//        System.out.println(principal.getName());
        System.out.println("simpMessagingTemplate : " + simpMessagingTemplate);
        //simpMessagingTemplate.convertAndSendToUser(principal.getName(), "/queue/greetings", "haha...");
        Greeting greeting = new Greeting("Hello, Ketty");
        simpMessagingTemplate.convertAndSend("/topic/time", greeting);
    }
    @MessageMapping("/testPath")
    public void test1(String str , Principal principal) throws IOException {
        System.out.println("***********/hello1**********" + " : " + str);
//        simpMessagingTemplate.convertAndSend("/topic/greetings" , "haha...");
        System.out.println(principal.getName());
        System.out.println("simpMessagingTemplate : " + simpMessagingTemplate);


        ObjectNode payload = objectMapper.createObjectNode();
        payload.put("msgType" , "RENDEZVOUS");
        payload.put("from" , "WA1234");
        simpMessagingTemplate.convertAndSendToUser( "admin","/topic/route" , payload);
    }


}
