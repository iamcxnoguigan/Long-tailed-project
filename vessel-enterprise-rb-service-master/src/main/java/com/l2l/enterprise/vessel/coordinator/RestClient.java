package com.l2l.enterprise.vessel.coordinator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.l2l.enterprise.vessel.domain.Destination;
import com.l2l.enterprise.vessel.repository.CommonRepository;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Service
@SuppressWarnings("all")
public class RestClient {
    private static Logger logger = LoggerFactory.getLogger(RestClient.class);
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private CommonRepository commonRepository;

    private HttpHeaders getHeaders(){
        String plainCredentials="admin:test";
        String base64Credentials = new String(Base64.encodeBase64(plainCredentials.getBytes()));
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + base64Credentials);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        return headers;
    }

    public String  postDestinations(List<Destination> destinations , String vid){
        String url = commonRepository.getVdevContextPath()+"/vessel/"+vid+"/delay";
        ParameterizedTypeReference<List<Destination>> responseType = new ParameterizedTypeReference<List<Destination>>(){};
        HttpEntity requestEntity = new HttpEntity(destinations,getHeaders());
        ResponseEntity<List<Destination>> response = restTemplate.exchange(url , HttpMethod.POST , requestEntity , responseType);
        logger.debug("destinations is sent");
        return response.getBody().toString();
    }

    public String  updateStatus(String status){
        String url = commonRepository.getVdevContextPath()+"/status/"+status;
        HttpEntity requestEntity = new HttpEntity(status,getHeaders());
        ResponseEntity<String> response = restTemplate.exchange(url , HttpMethod.POST , requestEntity , String.class);
        logger.debug("status is sent");
        return response.getBody().toString();
    }

    public String checkDeiveryStatus(String pid){
        String url = commonRepository.getLvcContextPath()+"/vessel/"+commonRepository.getOrgId()+"/"+pid+"/delivery/status";
        HttpEntity requestEntity = new HttpEntity(getHeaders());
        ResponseEntity<String> status = restTemplate.exchange(url , HttpMethod.GET ,requestEntity , String.class);
        return status.getBody();
    }

    //delay -- Planning , missing --- Missing
    public String notifyMsg(String pid , String msgType ,  HashMap<String , Object> msgBody){
        String url = commonRepository.getLvcContextPath()+"/vessel/"+commonRepository.getOrgId()+"/"+pid+"/"+msgType;
        HttpEntity<?> requestEntity = new HttpEntity(msgBody, getHeaders());
        ResponseEntity<String> status = restTemplate.exchange(url , HttpMethod.POST ,requestEntity , String.class);
        return status.getBody();
    }

}
