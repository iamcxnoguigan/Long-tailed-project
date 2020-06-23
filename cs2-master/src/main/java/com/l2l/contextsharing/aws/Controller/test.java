package com.l2l.contextsharing.aws.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController//处理http请求，返回json格式
@RequestMapping
public class test {
    @RequestMapping(value = "/hello")
    public String hello(){
        return "hello";
    }
}
