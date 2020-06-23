package com.l2l.contextsharing.aws.Controller;


import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.l2l.contextsharing.aws.bean.*;

@RestController//处理http请求，返回json格式
@RequestMapping(value = "/publisher")//配置url，让该类下的所有接口url都映射在/users下

public class publisher {
    @Autowired
    public awsClient client;


    @RequestMapping(value = "/publish", method = RequestMethod.GET)
    public String say() {
        client.publish("s","s");

        return "publish";
    }
}
