package com.l2l.enterprise.vessel.config;

import com.l2l.enterprise.vessel.domain.Location;
import com.l2l.enterprise.vessel.repository.CommonRepository;
import com.l2l.enterprise.vessel.repository.LocationRepository;
import com.l2l.enterprise.vessel.util.CsvUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import javax.inject.Inject;
import java.io.IOException;
import java.util.List;

@Configuration
@SuppressWarnings("all")
public class ReposConfiguration {
    private  final static Logger logger = LoggerFactory.getLogger(ReposConfiguration.class);
    @Inject
    private Environment environment;
//    @Bean
    public CommonRepository commonRepository(){
        CommonRepository commonRepository = new CommonRepository(environment);
        return  commonRepository;
    }

    @Bean
    public LocationRepository locationRepository() throws IOException {
        String  fileName = environment.getProperty("locations.fileName");
        logger.debug("--"+fileName+"--");
        String path ="/home/cx/Desktop/vessel-enterprise-rb-service/vessel-enterprise-rb-service/build/resources/main" +"/data/"+fileName;
        List<Location> locations = CsvUtil.readLocations(path);
        logger.debug("locations : "+locations.toString());
        LocationRepository locationRepository = new LocationRepository();
        locationRepository.setLocations(locations);
        return locationRepository;
    }
}
