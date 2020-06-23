package com.l2l.contextsharing.aws.util;

import java.util.*;
import org.apache.commons.jexl3.JexlBuilder;
import org.apache.commons.jexl3.JexlContext;
import org.apache.commons.jexl3.JexlEngine;
import org.apache.commons.jexl3.JexlExpression;
import org.apache.commons.jexl3.MapContext;

public class iftttutil {
    //使用commons的jexl可实现将字符串变成可执行代码的功能
    public static Object convertToCode(String jexlExp,Map<String,Object> map){
        JexlEngine jexl = new JexlBuilder().create();
        JexlExpression e = jexl.createExpression(jexlExp);
        JexlContext jc = new MapContext();
        for(String key:map.keySet()){
            jc.set(key, map.get(key));
        }
        if(null==e.evaluate(jc)){
            return "";
        }
        return e.evaluate(jc);
    }
    public static void main(String[] args) {
        try {
            Map<String,Object> map=new HashMap<String,Object>();
            map.put("money",2100);
            String expression="money>=2000&&money<=4000";
            Object code = convertToCode(expression,map);
            System.out.println("ss");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
