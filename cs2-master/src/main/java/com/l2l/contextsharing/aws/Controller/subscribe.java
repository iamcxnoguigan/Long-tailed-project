package com.l2l.contextsharing.aws.Controller;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.sns.model.SubscribeRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.l2l.contextsharing.aws.bean.*;

@RestController//处理http请求，返回json格式
@RequestMapping//配置url，让该类下的所有接口url都映射在/users下
public class subscribe {
    @Autowired
    public awsClient client;
    @RequestMapping(value = "/subscribe",method = RequestMethod.GET)
    public String subscribe(){
        // Subscribe an email endpoint to an Amazon SNS topic.

        client.subscribe("s","s","s");
        return "subscribe";
    }
}
