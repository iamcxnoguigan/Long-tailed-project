package com.l2l.enterprise.vessel.repository;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

/**
 * vessel data cache for eventGateway shadow and related process data by vId
 * @author bqzhu
 */
@Service
public class CommonRepository {
    //TODO:the following properties should be configed in xxx.properties
    private  String orgId;
    private  String host;
    private  String port;
    private  String projectId;

    private  String vmcContextPath;
    private  String lvcContextPath;
    private  String vdevContextPath;

    private int zoomInVal = 1000;
    private int defaultDelayHour = 6;

    public CommonRepository(Environment environment){
        //TODO:register organization in VMC and LVC
        orgId = environment.getRequiredProperty("org.id");
        host = environment.getRequiredProperty("org.host");
        port = environment.getRequiredProperty("org.port");
        projectId = environment.getRequiredProperty("org.projectId");
        vmcContextPath = environment.getRequiredProperty("org.vmcContextPath");
        lvcContextPath = environment.getRequiredProperty("org.lvcContextPath");
        vdevContextPath = environment.getRequiredProperty("org.vdevContextPath");
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getVmcContextPath() {
        return vmcContextPath;
    }

    public void setVmcContextPath(String vmcContextPath) {
        this.vmcContextPath = vmcContextPath;
    }

    public String getLvcContextPath() {
        return lvcContextPath;
    }

    public void setLvcContextPath(String lvcContextPath) {
        this.lvcContextPath = lvcContextPath;
    }

    public String getVdevContextPath() {
        return vdevContextPath;
    }

    public void setVdevContextPath(String vdevContextPath) {
        this.vdevContextPath = vdevContextPath;
    }

    public int getZoomInVal() {
        return zoomInVal;
    }

    public void setZoomInVal(int zoomInVal) {
        this.zoomInVal = zoomInVal;
    }

    public int getDefaultDelayHour() {
        return defaultDelayHour;
    }

    public void setDefaultDelayHour(int defaultDelayHour) {
        this.defaultDelayHour = defaultDelayHour;
    }
}
