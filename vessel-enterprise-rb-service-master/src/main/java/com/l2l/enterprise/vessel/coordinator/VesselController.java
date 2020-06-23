package com.l2l.enterprise.vessel.coordinator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.l2l.enterprise.vessel.domain.Application;
import com.l2l.enterprise.vessel.domain.Destination;
import com.l2l.enterprise.vessel.domain.VesselShadow;
import com.l2l.enterprise.vessel.repository.ApplicationRepository;
import com.l2l.enterprise.vessel.repository.CommonRepository;
import com.l2l.enterprise.vessel.repository.ShadowRepository;
import com.l2l.enterprise.vessel.util.DateUtil;
import com.l2l.enterprise.vessel.util.MapUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * business locgic for interaction between  Monitor and Vessel
 * @author bqzhu
 */
@RestController
public class VesselController extends AbstractController {

    private static  Logger logger = LoggerFactory.getLogger(VesselController.class);


    @Autowired
    private CommonRepository commonRepository;

    @Autowired
    private ShadowRepository shadowRepository;

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private RestTemplate restTemplate;

    @RequestMapping("/hello")
    String hello(){
        logger.info("hello , girls");
        return "hello , girls";
    }
    /**
     * start a process instance by process name.
     * @param mp
     * @param processName
     * @return
     */
//    @RequestMapping(value = "/process-instances/{processName}", method = RequestMethod.POST)
//    public ProcessInstanceRepresentation StartProcessInstanceByName(@RequestBody Map<String, Object> mp , @PathVariable("processName") String processName) {
//        logger.info("--POST /process-instances/"+processName+"--");
//        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionName(processName).latestVersion().singleResult();
//        logger.info(processDefinition.toString());
//        Map<String , Object> vars = new HashMap<String , Object>();
//        String sailor = mp.get("sailor").toString();
//        String vid = mp.get("vid").toString();
//        int defaultDelayHour = Integer.parseInt(mp.get("defaultDelayHour").toString());
//        int zoomInVal = Integer.parseInt(mp.get("zoomInVal").toString());
//        String orgId = commonRepository.getOrgId();
//        vars.put("orgId" , orgId);
//        vars.put("vid" , vid);
//        vars.put("sailor" , sailor);
//
//        commonRepository.setDefaultDelayHour(defaultDelayHour);
//        commonRepository.setZoomInVal(zoomInVal);
//        //TODO: create shadow for process instance.
//
//        ProcessInstance processInstance = runtimeService.startProcessInstanceById(processDefinition.getId() , vars);
//
//        HistoricProcessInstance historicProcess = historyService.createHistoricProcessInstanceQuery().processInstanceId
//                (processInstance.getId()).singleResult();
//
//        User user = null;
//        if (historicProcess.getStartUserId() != null) {
//            UserCache.CachedUser cachedUser = userCache.getUser(historicProcess.getStartUserId());
//            if (cachedUser != null && cachedUser.getUser() != null) {
//                user = cachedUser.getUser();
//            }
//        }
//        return new ProcessInstanceRepresentation(historicProcess, processDefinition, ((ProcessDefinitionEntity)
//                processDefinition).isGraphicalNotationDefined(), user);
//    }


    /******************************REST API for operations on vessel cache **************************/

    /**
     * Custom GET/PUT Variables
     */
    @RequestMapping(value = "/vessel/shadow/{pid}", method = RequestMethod.GET)
    public ResponseEntity<VesselShadow> queryVesselShadow(@PathVariable String pid) {
        VesselShadow result = null;
        Map<String , Object> vars = runtimeService.getVariables(pid);
        String vid = vars.get("vid").toString();
        result = shadowRepository.findById(vid);

        return new ResponseEntity<VesselShadow>(result, HttpStatus.OK);
    }

    @RequestMapping(value = "/vessel/shadow/{pid}", method = RequestMethod.PUT)
    public ResponseEntity<VesselShadow> updateVesselShadow(@PathVariable String pid , @RequestBody VesselShadow vesselShadow) {
        Map<String , Object> vars = runtimeService.getVariables(pid);
        String vId = vars.get("vid").toString();
        shadowRepository.update(vesselShadow);
        return new ResponseEntity<VesselShadow>(vesselShadow, HttpStatus.OK);
    }


    @RequestMapping(value = "/{pid}/apply", method = RequestMethod.POST)
    public ResponseEntity<Application> apply(@PathVariable String pid , @RequestBody HashMap<String, Object> payload) throws JsonProcessingException {
        logger.info("---GET /{pid}/apply---"+payload.toString());
        VesselShadow result = null;
        //TODO:Forward request to vmc
        Map<String, Object> pvars = runtimeService.getVariables(pid);
        String vid = pvars.get("vid").toString();
        String vOrgId = pvars.get("orgId").toString();
        VesselShadow vesselShadow = shadowRepository.findById(vid);
        String spName = payload.get("spName").toString();
        int spNumber= Integer.parseInt(payload.get("spNumber").toString());

        //TODO:Map Destination List  to String List.
        List<Destination> destinations = vesselShadow.getRemainingDestinations();
        List<String> candidateDestinations = MapUtil.Destinations2Dnames(destinations);

        long startMs = DateUtil.str2date(vesselShadow.getStartTime()).getTime();
        long nowMs= new Date().getTime();
        String timeStamp = DateUtil.date2str(DateUtil.transForDate(nowMs + (nowMs-startMs)*commonRepository.getZoomInVal()));
        Application application = new Application(null , vOrgId ,pid , vid , spName , spNumber , candidateDestinations , timeStamp);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        HttpEntity<Application> requestEntity = new HttpEntity<Application>(application, headers);
        String url = commonRepository.getVmcContextPath()+"/vessel/"+vOrgId+"/"+pid+"/apply";
        ResponseEntity<Application> response = restTemplate.postForEntity(url ,requestEntity , Application.class);
        //TODO: 保存ａpplication
        Application activatedApplication = response.getBody();
        activatedApplication.setStatus("Activated");
        activatedApplication.setRendezvous("NONE");
        applicationRepository.save(activatedApplication);
        runtimeService.setVariable(pid , "applyId" , activatedApplication.getId());
        logger.info(activatedApplication.toString());
        return new ResponseEntity<Application>(application, HttpStatus.OK);
    }

    @RequestMapping(value = "/{pid}/ADTask/info" , method = RequestMethod.GET)
    HashMap<String , Object> getADTaskInfo(@PathVariable String pid){
        logger.debug("--GET /{pid}/ADTask/info--"+pid);
        HashMap<String,Object>  info  = new HashMap<String,Object>();
        Map<String, Object> pvars = runtimeService.getVariables(pid);
        String vid = pvars.get("vid").toString();
        VesselShadow vesselShadow = shadowRepository.findById(vid);
        List<Destination> dests = vesselShadow.getDestinations();
        Destination curDest = dests.get(vesselShadow.getStepIndex());
        String status = vesselShadow.getStatus();
        logger.debug(status);
        //TODO:port name
        info.put("pname" , curDest.getName());
        //TODO: status
        info.put("status" , status);

        //TODO: current time
        String startTime = vesselShadow.getStartTime();
        Date curDate = new Date();
        long startMs = DateUtil.str2date(startTime).getTime();
        String currentTime = DateUtil.date2str(DateUtil.transForDate( startMs + (curDate.getTime() - startMs)*commonRepository.getZoomInVal()));
        info.put("curTime" , currentTime);
        if(status.equals("Anchoring")){
            //TODO:  elapse time of arrival
            double arrivalElapseHour = DateUtil.TimeMinus(curDest.getEstiArrivalTime() , currentTime)*1.0/(1000*60*60);
            DecimalFormat df = new DecimalFormat("0.###");
            String arrivalElapseTime = df.format(arrivalElapseHour);
            info.put("arrivalElapseTime" , arrivalElapseTime);
            //TODO: anchoring time
            info.put("anchoringTime" , curDest.getEstiAnchorTime());
            //TODO: estimate arrival time
            info.put("arrivalTime" , curDest.getEstiArrivalTime());
        }else if(status.equals("Docking")){
            //TODO:  elapse time of despature
            logger.debug(curDest.toString()+"---"+currentTime);
            double departureElapseHour = DateUtil.TimeMinus(curDest.getEstiDepartureTime(), currentTime)*1.0/(1000*60*60);
            DecimalFormat df = new DecimalFormat("0.###");
            String departureElapseTime = df.format(departureElapseHour);
            info.put("departureElapseTime" , departureElapseTime);
            //TODO: arrival time
            info.put("arrivalTime" ,curDest.getEstiArrivalTime());
            //TODO: departure time
            info.put("departureTime" , curDest.getEstiDepartureTime());
        }else{
            logger.debug("Error status."+status);
        }
        return  info;
    }

//    @RequestMapping(value = "/{pid}/shadow/status/{status}" , method = RequestMethod.POST , produces = "application/json")
//    public ResponseEntity<String> updateStatus(@PathVariable("pid") String pid , @PathVariable("status") String status){
//        logger.debug("--status--"+status);
//        VesselShadow vesselShadow = shadowRepository.findByPid(pid);
//        vesselShadow.setStatus(status);
//        return new ResponseEntity<String>(status , HttpStatus.OK);
//    }
    @RequestMapping(value = "/{pid}/rendezvous/{rendezvous}" , method = RequestMethod.POST , produces = "application/json")
    public ResponseEntity<String> updateRendezvous(@PathVariable("pid") String pid , @PathVariable("rendezvous") String rendezvous){
        logger.debug("--POST /{pid}/shadow/rendezvous/{rendezvous}--"+rendezvous);
        Application application = applicationRepository.findByPid(pid);
        application.setRendezvous(rendezvous);
        return new ResponseEntity<String>(rendezvous , HttpStatus.OK);
    }

    @RequestMapping(value = "/{pid}/msgType/{MsgType}" , method = RequestMethod.POST , produces = "application/json")
    public ResponseEntity<String> sendMsg(@PathVariable("pid") String pid , @PathVariable("MsgType") String msgType){
        logger.debug("--POST /{pid}/{MsgType}--"+msgType);
        runtimeService.messageEventReceived(msgType , pid);
        return new ResponseEntity<String>(msgType , HttpStatus.OK);
    }
    @RequestMapping(value = "/{pid}/application" , method = RequestMethod.GET , produces = "application/json")
    public ResponseEntity<Application> getApplication(@PathVariable("pid") String pid ){
         String applyId =  runtimeService.getVariable(pid , "applyId").toString();
        Application application = null;
        if (applyId.equals("NONE")) {
            application = new Application();
            application.setId("NONE");
        }else{
           application =   applicationRepository.findByPid(pid);
        }
        return  new ResponseEntity<Application>(application ,  HttpStatus.OK);
    }
    @RequestMapping(value = "/{pid}/currentPort" , method = RequestMethod.GET , produces = "application/json")
    public ResponseEntity<Destination> getCurrentPort(@PathVariable("pid") String pid ){
        String vid = runtimeService.getVariable(pid , "vid").toString();
        VesselShadow vesselShadow = shadowRepository.findById(vid);
        Destination curPort = vesselShadow.getDestinations().get(vesselShadow.getStepIndex());
        return  new ResponseEntity<Destination>(curPort ,  HttpStatus.OK);
    }

    @RequestMapping(value = "/{pid}/process/status" , method = RequestMethod.GET , produces = "application/json")
    public ResponseEntity<String> getProcessStatus(@PathVariable("pid") String pid ){
        String processStatus = runtimeService.getVariable(pid , "processStatus").toString();
        logger.debug("/{pid}/process/status--"+processStatus);
        String payload= "{\"processStatus\":\""+processStatus+"\"}";
        return  new ResponseEntity<String>(payload ,  HttpStatus.OK);
    }
}
