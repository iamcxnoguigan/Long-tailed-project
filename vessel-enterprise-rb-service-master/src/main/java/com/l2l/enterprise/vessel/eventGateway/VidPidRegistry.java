package com.l2l.enterprise.vessel.eventGateway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
public class VidPidRegistry {
    private static final Logger log = LoggerFactory.getLogger(VidPidRegistry.class);
    Map<String , String>   v2pMap;
    Map<String , String>   p2vMap;

    public VidPidRegistry() {
        this.p2vMap = Collections.synchronizedMap(new HashMap());
        this.v2pMap = Collections.synchronizedMap(new HashMap());
    }

    public String findRegisteredVidBypId(String pid){
        return  p2vMap.get(pid);
    }
    public String findRegisteredpidByvId(String vid){
        return  v2pMap.get(vid);
    }

    public Map<String, String> getV2pMap() {
        return v2pMap;
    }

    public void setV2pMap(Map<String, String> v2pMap) {
        this.v2pMap = v2pMap;
    }

    public Map<String, String> getP2vMap() {
        return p2vMap;
    }

    public void setP2vMap(Map<String, String> p2vMap) {
        this.p2vMap = p2vMap;
    }
}
