package com.l2l.enterprise.vessel.web.rest;

import org.springframework.stereotype.Component;
import org.springframework.web.util.HtmlUtils;

import java.io.*;

@Component
public class BpmnAdd {
    public void changBpmn(String oldfile,String newfile) throws IOException {
        //待替换字符
        String aStr = "&lt;";
        //替换字符
        String bStr = "<";

        String cStr = "&gt;";
        String dStr = ">";
        String path = "/home/cx/Downloads/" + oldfile;
        String newPath ="/home/cx/Desktop/vessel-enterprise-rb-service/vessel-enterprise-rb-service/src/main/resources/processes/" + newfile;
        //读取文件
        File file = new File(path);
        File newFile = new File(newPath);

        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));

        //内存流
        CharArrayWriter caw = new CharArrayWriter();

        //替换
        String line = null;

        //以行为单位进行遍历
        while ((line = br.readLine()) != null) {
            //替换每一行中符合被替换字符条件的字符串
            line = line.replaceAll(aStr, bStr);
            line = line.replaceAll(cStr,dStr);
            //将该行写入内存
            caw.write(line);
            //添加换行符，并进入下次循环
            caw.append(System.getProperty("line.separator"));
        }
        //关闭输入流
        br.close();

        //将内存中的流写入源文件
        FileWriter fw = new FileWriter(newFile);
        caw.writeTo(fw);
        fw.close();

        }
}

