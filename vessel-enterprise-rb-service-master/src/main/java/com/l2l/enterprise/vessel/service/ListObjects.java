package com.l2l.enterprise.vessel.service;

import java.util.List;

public class ListObjects {
    private List<TestListObject> testListObjects;
    public ListObjects(List<TestListObject> testListObjects){
        System.out.println("size : "+testListObjects.size());
        testListObjects.stream().forEach(var-> {
            System.out.println(var);
        });
    }
}
