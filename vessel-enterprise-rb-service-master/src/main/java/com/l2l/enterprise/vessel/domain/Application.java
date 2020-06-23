package com.l2l.enterprise.vessel.domain;

import java.util.List;


public class Application {
    private String id;
    private String vOrgId;
    private String vpid;
    private String vid;
    private String mOrgId;
    private String mpid;
    private String spName;
    private int spNumber;
    private String rendezvous;
    private List<String> destinations;
    private String status;
    private String timeStamp;

    public Application() {
    }

    public Application(String id, String vOrgId, String vpid, String vid, String spName, int spNumber, List<String> destinations, String timeStamp) {
        this.id = id;
        this.vOrgId = vOrgId;
        this.vpid = vpid;
        this.vid = vid;
        this.spName = spName;
        this.spNumber = spNumber;
        this.destinations = destinations;
        this.timeStamp = timeStamp;
    }

    public Application(String id, String vOrgId, String vpid, String vid, String mOrgId, String mpid, String spName, int spNumber, String rendezvous, List<String> destinations, String status, String timeStamp) {
        this.id = id;
        this.vOrgId = vOrgId;
        this.vpid = vpid;
        this.vid = vid;
        this.mOrgId = mOrgId;
        this.mpid = mpid;
        this.spName = spName;
        this.spNumber = spNumber;
        this.rendezvous = rendezvous;
        this.destinations = destinations;
        this.status = status;
        this.timeStamp = timeStamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getvOrgId() {
        return vOrgId;
    }

    public void setvOrgId(String vOrgId) {
        this.vOrgId = vOrgId;
    }

    public String getVpid() {
        return vpid;
    }

    public void setVpid(String vpid) {
        this.vpid = vpid;
    }

    public String getVid() {
        return vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }

    public String getmOrgId() {
        return mOrgId;
    }

    public void setmOrgId(String mOrgId) {
        this.mOrgId = mOrgId;
    }

    public String getMpid() {
        return mpid;
    }

    public void setMpid(String mpid) {
        this.mpid = mpid;
    }

    public String getSpName() {
        return spName;
    }

    public void setSpName(String spName) {
        this.spName = spName;
    }

    public int getSpNumber() {
        return spNumber;
    }

    public void setSpNumber(int spNumber) {
        this.spNumber = spNumber;
    }

    public String getRendezvous() {
        return rendezvous;
    }

    public void setRendezvous(String rendezvous) {
        this.rendezvous = rendezvous;
    }

    public List<String> getDestinations() {
        return destinations;
    }

    public void setDestinations(List<String> destinations) {
        this.destinations = destinations;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }
}
