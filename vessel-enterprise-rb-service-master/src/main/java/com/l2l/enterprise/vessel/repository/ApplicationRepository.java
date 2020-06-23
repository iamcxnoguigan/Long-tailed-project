package com.l2l.enterprise.vessel.repository;


import com.l2l.enterprise.vessel.domain.Application;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class ApplicationRepository {
    private  static final Logger logger = LoggerFactory.getLogger(ApplicationRepository.class);

    private List<Application> applications = new ArrayList<Application>();

    public void save(Application application){
        applications.add(application);
    }

    public Application findById(String applyId){
        for(Application application : applications){
            if(applyId.equals(application.getId())){
                return application;
            }
        }
        return null;
    }

    public Application findByPid(String pid){
        for(Application application : applications){
            if(pid.equals(application.getVpid())){
                return application;
            }
        }
        return null;
    }
}
