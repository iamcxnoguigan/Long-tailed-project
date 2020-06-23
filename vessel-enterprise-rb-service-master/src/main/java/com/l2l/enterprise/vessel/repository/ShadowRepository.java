package com.l2l.enterprise.vessel.repository;

import com.l2l.enterprise.vessel.domain.VesselShadow;
import com.l2l.enterprise.vessel.eventGateway.ShadowWithProcesses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class ShadowRepository {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    List<ShadowWithProcesses> shadowProcessRegistry = new ArrayList<ShadowWithProcesses>();
    List<VesselShadow> vesselShadows = new ArrayList<VesselShadow>();

    public void save(VesselShadow vs){
        vesselShadows.add(vs);
    }

    public VesselShadow findById(String id){
        for(VesselShadow vesselShadow : vesselShadows){
            if(id.equals(vesselShadow.getId())){
                return vesselShadow;
            }
        }
        return null;
    }

    public ShadowWithProcesses findRegisteredProcessesById(String vid){
        for(ShadowWithProcesses spr : shadowProcessRegistry){
            if(spr.getVid().equals(vid)){
                return spr;
            }
        }
        return null;
    }

    public ShadowWithProcesses findRegisteredVidBypId(String pId){
        for(ShadowWithProcesses spr : shadowProcessRegistry){
            if(spr.getPid().equals(pId)){
                return spr;
            }
        }
        return null;
    }

    public  VesselShadow update(VesselShadow newVs){
        String vid = newVs.getId();
        VesselShadow oldVs = findById(vid);
        oldVs = newVs;
        return findById(vid);
    }

    public  void  saveRegistry(String vid , String pid){
        ShadowWithProcesses spr = findRegisteredProcessesById(vid);
        if(spr == null){
            ShadowWithProcesses e = new ShadowWithProcesses();
            e.setVid(vid);
            e.save(pid);
            shadowProcessRegistry.add(e);
        }else{
            spr.save(pid);
        }
    }


    public List<ShadowWithProcesses> getShadowProcessRegistry() {
        return shadowProcessRegistry;
    }

    public void setShadowProcessRegistry(List<ShadowWithProcesses> shadowProcessRegistry) {
        this.shadowProcessRegistry = shadowProcessRegistry;
    }

    public List<VesselShadow> getVesselShadows() {
        return vesselShadows;
    }

    public void setVesselShadows(List<VesselShadow> vesselShadows) {
        this.vesselShadows = vesselShadows;
    }
}
