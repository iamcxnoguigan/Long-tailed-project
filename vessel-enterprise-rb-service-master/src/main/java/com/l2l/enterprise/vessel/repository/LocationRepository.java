package com.l2l.enterprise.vessel.repository;

import com.l2l.enterprise.vessel.domain.Location;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;


public class LocationRepository {
    private  final Logger logger = LoggerFactory.getLogger(this.getClass());

    private List<Location> locations = new ArrayList<Location>();

    public void save(Location location){
        locations.add(location);
    }

    public Location findByName(String name){
        for(Location location : locations){
            if(name.equals(location.getName())){
                return location;
            }
        }
        return null;
    }

    public List<Location> getLocations() {
        return locations;
    }

    public void setLocations(List<Location> locations) {
        this.locations = locations;
    }
}
