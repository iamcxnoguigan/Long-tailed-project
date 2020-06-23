package com.l2l.enterprise.vessel.domain;

@SuppressWarnings("all")
public class VesselState {
    private double longitude;
    private double latitude;
    private double velocity;
    private  String timeStamp;

    public VesselState(double longitude, double latitude, double velocity, String timeStamp) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.velocity = velocity;
        this.timeStamp = timeStamp;
    }

    public VesselState() {
    }

    public VesselState deepCopy(){
        VesselState res = new VesselState();
        res.setTimeStamp(this.timeStamp);
        res.setLatitude(this.latitude);
        res.setLongitude(this.longitude);
        res.setVelocity(this.velocity);
        return res;
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
}
