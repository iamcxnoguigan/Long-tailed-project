package com.l2l.enterprise.vessel.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;

@SuppressWarnings("all")
public class CommonUtil {
    private  final Logger logger = LoggerFactory.getLogger(CommonUtil.class);

    public static int Guid=100;

    public static String getGuid() {

        CommonUtil.Guid+=1;

        long now = System.currentTimeMillis();
        //获取4位年份数字
        SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy");
        //获取时间戳
        String time=dateFormat.format(now);
        String info=now+"";
        //获取三位随机数
        //int ran=(int) ((Math.random()*9+1)*100);
        //要是一段时间内的数据连过大会有重复的情况，所以做以下修改
        int ran=0;
        if(CommonUtil.Guid>999){
            CommonUtil.Guid=100;
        }
        ran=CommonUtil.Guid;

        return time+info.substring(2, info.length())+ran;
    }
}
