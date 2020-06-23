package com.l2l.enterprise.vessel.domain;

import org.junit.Test;

import static org.junit.Assert.*;

public class DestinationTest {
    @Test
    public void getName() throws Exception {
        Destination d = new Destination("name","estiAnchorTime","estiArrivalTime","estiDepartureTime");
        System.out.println(d.toString());
    }

}
