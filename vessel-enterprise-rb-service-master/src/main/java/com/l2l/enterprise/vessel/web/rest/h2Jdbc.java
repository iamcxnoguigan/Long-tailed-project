package com.l2l.enterprise.vessel.web.rest;

import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
public class h2Jdbc {
    private static final String JDBC_URL = "jdbc:h2:file:./build/h2db/db/vessel";
   //连接数据库时使用的用户名
    private static final String USER = "vessel";
   //连接数据库时使用的密码
    private static final String PASSWORD = "";
//连接H2数据库时使用的驱动类，org.h2.Driver这个类是由H2数据库自己提供的，在H2数据库的jar包中可以找到
    private static final String DRIVER_CLASS="org.h2.Driver";

        public HashMap<String, List<String>> querytask() throws Exception {
                // 加载H2数据库驱动
                   Class.forName(DRIVER_CLASS);
                 // 根据连接URL，用户名，密码获取数据库连接
                 Connection conn = DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
                 Statement stmt = conn.createStatement();
//                 //如果存在USER_INFO表就先删除USER_INFO表
//                 stmt.execute("DROP TABLE IF EXISTS USER_INFO");
//                 //创建USER_INFO表
//                 stmt.execute("CREATE TABLE USER_INFO(id VARCHAR(36) PRIMARY KEY,name VARCHAR(100),sex VARCHAR(4))");
//                 //新增
//                 stmt.executeUpdate("INSERT INTO USER_INFO VALUES('" + UUID.randomUUID()+ "','大日如来','男')");
//                 stmt.executeUpdate("INSERT INTO USER_INFO VALUES('" + UUID.randomUUID()+ "','青龙','男')");
//                 stmt.executeUpdate("INSERT INTO USER_INFO VALUES('" + UUID.randomUUID()+ "','白虎','男')");
//                 stmt.executeUpdate("INSERT INTO USER_INFO VALUES('" + UUID.randomUUID()+ "','朱雀','女')");
//                 stmt.executeUpdate("INSERT INTO USER_INFO VALUES('" + UUID.randomUUID()+ "','玄武','男')");
//                 stmt.executeUpdate("INSERT INTO USER_INFO VALUES('" + UUID.randomUUID()+ "','苍狼','男')");
//                 //删除
//                 stmt.executeUpdate("DELETE FROM USER_INFO WHERE name='大日如来'");
//                 //修改
//                 stmt.executeUpdate("UPDATE USER_INFO SET name='孤傲苍狼' WHERE name='苍狼'");
                 //查询
                 ResultSet rs = stmt.executeQuery("SELECT * FROM ACT_RU_TASK ");
                 //遍历结果集
            HashMap<String,List<String>>  taskMap  = new HashMap<>();
                 while (rs.next()) {
                     List<String> pid_tname = new ArrayList<>();
                     pid_tname.add(rs.getString("PROC_INST_ID_"));
                     pid_tname.add(rs.getString("NAME_"));
                     taskMap.put(rs.getString("ID_"),pid_tname);
//                        String s = rs.getString("ID_") + "," + rs.getString("PROC_INST_ID_")+ "," + rs.getString("NAME_");
//                         System.out.println(rs.getString("ID_") + "," + rs.getString("PROC_INST_ID_")+ "," + rs.getString("NAME_"));
                     }
                return taskMap;
                 //释放资源
//                 stmt.close();
//                 //关闭连接
//                 conn.close();

    }
    public List<String> finTaskIf(String pid) throws Exception {
        HashMap<String, List<String>> taskMap = this.querytask();
        String taskId = "";
        String taskName = "";
        for(String key : taskMap.keySet()){
            if(taskMap.get(key).get(0).equals(pid)){
                taskId = key;
                taskName = taskMap.get(key).get(1);
                break;
            }
        }
        List<String> ans = new ArrayList<>();
        ans.add(taskId);
        ans.add(taskName);
        return ans;
    }
}
