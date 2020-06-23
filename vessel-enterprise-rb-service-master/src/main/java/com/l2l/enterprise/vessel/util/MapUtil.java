package com.l2l.enterprise.vessel.util;



import com.l2l.enterprise.vessel.domain.Destination;

import java.util.ArrayList;
import java.util.List;

public class MapUtil {
    public static List<String> Destinations2Dnames(List<Destination> dests){
        List<String> dNames = new ArrayList<String>();
        for(Destination destination : dests){
                dNames.add(destination.getName());
        }
        return dNames;
    }
}
