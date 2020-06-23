package com.l2l.enterprise.vessel.domain;

import java.util.ArrayList;
import java.util.List;

public class VesselShadow {
    private String id; // vid
    private double longitude;
    private double latitude;
    private double velocity;
    private  String timeStamp;
    private List<Destination> destinations;
    private String status;
    private String startTime;
    private int stepIndex;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getVelocity() {
        return velocity;
    }

    public void setVelocity(double velocity) {
        this.velocity = velocity;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public List<Destination> getDestinations() {
        return destinations;
    }

    public void setDestinations(List<Destination> destinations) {
        this.destinations = destinations;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public int getStepIndex() {
        return stepIndex;
    }

    public void setStepIndex(int stepIndex) {
        this.stepIndex = stepIndex;
    }

    public List<Destination> getRemainingDestinations(){
        List<Destination> dlist = new ArrayList<Destination>();
        for(int i = 0 ; i < destinations.size(); i++){
            if(i >= stepIndex){
                Destination d = destinations.get(i);
//                if(i == stepIndex) {
//                    if ("Anchoring".equals(status) || "Docking".equals(status)) {
//                        dlist.add(destinations.get(stepIndex));
//                    }
//                }
                dlist.add(d);
            }
        }

        return dlist;
    }

    public void updateState(double longitude , double latitude , double velocity , String timeStamp ){
        this.longitude = longitude;
        this.latitude =latitude;
        this.velocity = velocity;
        this.timeStamp = timeStamp;
    }



}
