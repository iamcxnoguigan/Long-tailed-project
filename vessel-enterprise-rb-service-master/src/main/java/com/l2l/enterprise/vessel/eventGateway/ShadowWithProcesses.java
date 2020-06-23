package com.l2l.enterprise.vessel.eventGateway;

import java.util.ArrayList;
import java.util.List;

public class ShadowWithProcesses {
     private String vid;
     private List<String> pids = new ArrayList<String>();

     public void save(String pid){//temporaly support one shadow to be subscribed by one process.
         // pids.add(pid);
         if(pids.size() == 1){
             pids.set(0, pid);
         }else{
             pids.add(pid);
         }
     }

     public String findByPid(String pid){
         for(String s : pids){
             if(s.equals(pid)){
                 return vid;
             }
         }
         return null;
     }

     public  String getPid(){
         return pids.get(0);
     }

    public String getVid() {
        return vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }

    public List<String> getPids() {
        return pids;
    }

    public void setPids(List<String> pids) {
        this.pids = pids;
    }
}
