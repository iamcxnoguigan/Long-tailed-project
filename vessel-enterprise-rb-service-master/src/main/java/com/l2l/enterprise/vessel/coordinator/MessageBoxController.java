package com.l2l.enterprise.vessel.coordinator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.l2l.enterprise.vessel.domain.Destination;
import com.l2l.enterprise.vessel.domain.Location;
import com.l2l.enterprise.vessel.domain.VesselShadow;
import com.l2l.enterprise.vessel.repository.CommonRepository;
import com.l2l.enterprise.vessel.repository.LocationRepository;
import com.l2l.enterprise.vessel.repository.ShadowRepository;
import com.l2l.enterprise.vessel.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * business locgic for interaction between  Monitor and Vessel
 * @author bqzhu
 */
@RestController
@SuppressWarnings("all")
public class MessageBoxController  extends AbstractController{
    private static final Logger logger = LoggerFactory.getLogger(MessageBoxController.class);

    @Autowired
    private RestClient restClient;

    @Autowired
    private CommonRepository commonRepository;

    @Autowired
    private ShadowRepository shadowRepository;


    @Autowired
    private LocationRepository locationRepository;


    @RequestMapping(value = "/{pid}/delay", method = RequestMethod.POST , produces = "application/json")
    public ResponseEntity<String> delay(@PathVariable("pid") String pid, @RequestBody HashMap<String, Object> mp) throws JsonProcessingException {
        logger.info("--POST /{pid}/delay--"+pid+"--"+mp);
        int dx = Integer.parseInt(mp.get("dx").toString());
        int dy = Integer.parseInt(mp.get("dy").toString());
        //TODO:modify vesselShadow and sync shadow to device
        Map<String, Object> pvars = runtimeService.getVariables(pid);
        String vid = pvars.get("vid").toString();
        VesselShadow vesselShadow = shadowRepository.findById(vid);
        if (vesselShadow == null) {
            return new ResponseEntity<String>("{\"Tips\":\"There is no corresponding process instance for this vessel identifier!\"" , HttpStatus.OK);
        }
        List<Destination> destinations = vesselShadow.getDestinations();
        logger.info(destinations.toString());
        //TODO : find current port
        int curPortIndex = vesselShadow.getStepIndex();
        //TODO : find current time
        long zoomVal = commonRepository.getZoomInVal();
        long simuMs = DateUtil.str2date(vesselShadow.getStartTime()).getTime();
        long curMs = (new Date().getTime()-simuMs)*zoomVal+simuMs;
        //TODO : when status is "Voyaging" or "Anchoring"
        if(vesselShadow.getStatus().equals("Voyaging") || vesselShadow.getStatus().equals("Anchoring")) {
            logger.debug("when status is \"Voyaging\" or \"Anchoring\"");
            long gapMs = -1;
            for (int i = 0; i < destinations.size(); i++) {
                Destination d = destinations.get(i);
                String newEstiAnchoringTime = null;
                String newEstiArrivalTime = null;
                String newEstiDepartureTime = null;

                if (i == curPortIndex) {
                    newEstiAnchoringTime = d.getEstiAnchorTime();//EstiAnchoringTime 不变
                    newEstiArrivalTime = DateUtil.date2str(DateUtil.
                            transForDate(DateUtil.str2date(d.getEstiArrivalTime()).getTime() + dx * 60 * 60 * 1000));

                    //TODO:EstiArrivalTime + delay > currentTime --> New EstiArrivalTime must be later than the current time.
                    if(DateUtil.str2date(newEstiArrivalTime).getTime() < curMs) {
                        logger.debug("New estimate arrival time is illegal ,  required to be later than current time");
                        return new ResponseEntity<String>("{\"Tips\":\"New estimate arrival time is illegal ,  required to be later than current time.\"" , HttpStatus.OK);
                    }
                    gapMs = (dx+dy) * 60 * 60 * 1000;
                    newEstiDepartureTime = DateUtil.date2str(DateUtil.
                            transForDate(DateUtil.str2date(d.getEstiDepartureTime()).getTime() + gapMs));
                    //TODO:NewEstiDepartureTime > NewEstiArrivalTime --> NewEstiDepartureTime must be later than the NewEstiArrivalTime.
                    if(DateUtil.TimeMinus(newEstiDepartureTime , newEstiArrivalTime) < 0) {
                        logger.debug("New estimate departure time is illegal ,  required to be later than estimate arrival time");
                        return new ResponseEntity<String>("{\"Tips\":\"New estimate departure time is illegal ,  required to be later than estimate arrival time.\"" , HttpStatus.OK);
                    }
                }

                if (i > curPortIndex) {
                    newEstiAnchoringTime = DateUtil.date2str(DateUtil
                            .transForDate(DateUtil.str2date(d.getEstiAnchorTime()).getTime() + gapMs));
                    newEstiArrivalTime = DateUtil.date2str(DateUtil
                            .transForDate(DateUtil.str2date(d.getEstiArrivalTime()).getTime() + gapMs));
                    newEstiDepartureTime = DateUtil.date2str(DateUtil
                            .transForDate(DateUtil.str2date(d.getEstiDepartureTime()).getTime() + gapMs));
                }
                //TODO: update destination of shadow
                d.setEstiAnchorTime(newEstiAnchoringTime);
                d.setEstiArrivalTime(newEstiArrivalTime);
                d.setEstiDepartureTime(newEstiDepartureTime);
            }

            //TODO : send destinations to vessel device.
            logger.debug("send destinations to vessel device.");
            restClient.postDestinations(destinations , vid);

            //TODO: notify logistic of "Planning"
            HashMap<String , Object> msgBody = new HashMap<String , Object>();
            msgBody.put("eventType" , "DELAY");
            msgBody.put("phase" , vesselShadow.getStatus());
            msgBody.put("dx" , dx);
            msgBody.put("dy" , dy);
            restClient.notifyMsg(pid , "Planning" , msgBody);

            String anchoringMsg = "";
            String dockingMsg = "";
            String msg ="The ship will";
            if(dx != 0){
                if(dx > 0){
                    anchoringMsg = " arrive the port port after a delay of "+ dy +" hours";
                }else{
                    anchoringMsg = " arrive the port " + (-dy) + " hours in advance";
                }

                msg += anchoringMsg;
            }
            if(dy != 0){
                if(!msg.equals("The ship will")){
                    msg+= " and";
                }
                if(dy > 0){
                    dockingMsg = " leave the port port after a delay of "+ dy +" hours";
                }else{
                    dockingMsg = " leave the port " + (-dy) + " hours in advance";
                }
                msg+=dockingMsg;
            }

            if(!msg.equals("The ship will")){
//                stompClient.sendDelayMsg("admin" , "/topic/vessel/delay" , pid , msg);
                logger.debug(msg);
            }
            //TODO : when status is "Docking"
        } else if(vesselShadow.getStatus().equals("Docking")) {
            logger.debug("when status is \"Docking\"");
            //TODO: can not set dy
            if(dx != 0){
                dx = 0;
            }
            long gapMs = 0;
            for(int i = 0 ; i < destinations.size();i++){
                String newEstiAnchoringTime = null;
                String newEstiArrivalTime = null;
                String newEstiDepartureTime = null;
                Destination d = destinations.get(i);
                if(i == curPortIndex ){
                    newEstiAnchoringTime = d.getEstiAnchorTime();
                    newEstiArrivalTime = d.getEstiArrivalTime();
                    newEstiDepartureTime = DateUtil.date2str(DateUtil.
                            transForDate(DateUtil.str2date(d.getEstiDepartureTime()).getTime() + dy * 60 * 60 * 1000));
                    gapMs = dy * 60 * 60 * 1000;
                    //TODO: determine if the departure time is later than cur Ms
                    if(DateUtil.str2date(newEstiDepartureTime).getTime() < curMs) {
                        logger.debug("New estimate departure time is illegal : required to be later than current time.");
                        return new ResponseEntity<String>("{\"Tips\":\"New estimate departure time is illegal : required to be later than current time.\"" , HttpStatus.OK);
                    }

                }

                if(i > curPortIndex) {
                    newEstiAnchoringTime = DateUtil.date2str(DateUtil
                            .transForDate(DateUtil.str2date(d.getEstiAnchorTime()).getTime() + gapMs));
                    newEstiArrivalTime = DateUtil.date2str(DateUtil
                            .transForDate(DateUtil.str2date(d.getEstiArrivalTime()).getTime() + gapMs));
                    newEstiDepartureTime = DateUtil.date2str(DateUtil
                            .transForDate(DateUtil.str2date(d.getEstiDepartureTime()).getTime() + gapMs));
                }

                //TODO: update destination of shadow
                d.setEstiAnchorTime(newEstiAnchoringTime);
                d.setEstiArrivalTime(newEstiArrivalTime);
                d.setEstiDepartureTime(newEstiDepartureTime);
            }
            //TODO : send destinations to vessel device.
            logger.info("send destinations to vessel device.");
            restClient.postDestinations(destinations , vid);

            //TODO: notify logistic of "Planning"
            HashMap<String , Object> msgBody = new HashMap<String , Object>();
            msgBody.put("eventType" , "DELAY");
            msgBody.put("phase" , "Docking");
            msgBody.put("dy" , dy);
            restClient.notifyMsg(pid , "Planning" , msgBody);
            String msg = "";
            if(dy != 0){
                if(dy > 0){
                    msg = "The ship will leave the port port after a delay of "+ dy +" hours";
                }else{
                    msg = "The ship will leave the port " + (-dy) + " hours in advance";
                }
//                stompClient.sendDelayMsg("admin" , "/topic/vessel/delay" , pid , msg);
                logger.debug(msg);
            }
        } else{
            logger.debug("The current situation is not considered!(the current status of the vessel is ignored)");
            return new ResponseEntity<String>("{\"Tips\":\"The current situation is not considered!(the current status of the vessel is ignored\")", HttpStatus.OK);
        }

        // delay/postpone event to coordinator
        return new ResponseEntity<String>("{\"Tips\":\"ok\"}" , HttpStatus.OK);
    }

    @RequestMapping(value = "/{pid}/destinations/remain", method = RequestMethod.GET)
    public ResponseEntity<List<Destination>> findRemainingDestinations(@PathVariable String pid) {
        VesselShadow result = null;
        Map<String, Object> pvars = runtimeService.getVariables(pid);
        String vid = pvars.get("vid").toString();
        VesselShadow vesselShadow = shadowRepository.findById(vid);
        List<Destination> destinations = vesselShadow.getRemainingDestinations();
        result = shadowRepository.findById(vid);
        return new ResponseEntity<List<Destination>>(destinations, HttpStatus.OK);
    }
    @RequestMapping(value = "/{pid}/currentTime", method = RequestMethod.GET)
    public ResponseEntity<Long> getCurrentMs(@PathVariable String pid) {
        VesselShadow result = null;
        Map<String, Object> pvars = runtimeService.getVariables(pid);
        String vid = pvars.get("vid").toString();
        VesselShadow vesselShadow = shadowRepository.findById(vid);
        String simuStartTime = vesselShadow.getStartTime();
        Date curDate = new Date();
        long simuStartMs = DateUtil.str2date(simuStartTime).getTime();
        simuStartMs += (curDate.getTime() - simuStartMs)*commonRepository.getZoomInVal();
        return new ResponseEntity<Long>(simuStartMs, HttpStatus.OK);
    }

    @RequestMapping(value = "/location", method = RequestMethod.GET)
    public ResponseEntity<Location> getLocationByName(@RequestParam(value = "name") String name){
//        logger.debug("--/location--"+name);
        Location location = locationRepository.findByName(name.trim());
        return new ResponseEntity<Location>(location , HttpStatus.OK);
    }


}

