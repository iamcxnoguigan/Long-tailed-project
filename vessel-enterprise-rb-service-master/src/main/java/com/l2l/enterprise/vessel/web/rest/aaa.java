package com.l2l.enterprise.vessel.web.rest;

import org.springframework.stereotype.Component;
import org.springframework.web.util.HtmlUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

public class aaa {
    public static void main(String args[]) {
        aaa ccccc=new aaa();
        try {
            BufferedReader bre = null;

            String file = "/home/cx/Downloads/vessel.bpmn20 (8).xml";


            String str="";
            String ssss="";//将文件中所以的字符都读出来
            bre = new BufferedReader(new FileReader(file));//此时获取到的bre就是整个文件的缓存流

            while ((str = bre.readLine())!= null) // 判断最后一行不存在，为空结束循环
            {
                //writer.write(ccccc.changetohtmlEscape(str));//原样输出读到的内容
                ssss+=ccccc.changetohtmlEscape(str);
            }
            System.out.println(ssss);
            bre.close();
            FileWriter fileWriter = new FileWriter(file);
		    /* 创建缓冲区 */
            BufferedWriter writer = new BufferedWriter(fileWriter);
            writer.write(ssss);
            writer.close();
            fileWriter.close();
            System.out.println("done");

        } catch (Exception e) {

            e.printStackTrace();
        }



    }
    public String changetohtmlEscape(String aa){
        String a= HtmlUtils.htmlUnescape(aa);
        return a;
    }
}
