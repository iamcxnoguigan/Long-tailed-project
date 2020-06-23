package com.l2l.enterprise.vessel.service;

import com.l2l.enterprise.vessel.coordinator.RestClient;
import com.l2l.enterprise.vessel.domain.Application;
import com.l2l.enterprise.vessel.domain.Destination;
import com.l2l.enterprise.vessel.domain.VesselShadow;
import com.l2l.enterprise.vessel.repository.ApplicationRepository;
import com.l2l.enterprise.vessel.repository.ShadowRepository;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Service("dockingPostService")
public class DockingPostService implements ExecutionListener, Serializable {
    private static final long serialVersionUID = 4885656684805353238L;
    private static final Logger logger = LoggerFactory.getLogger(DockingPostService.class);

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private RestClient restClient;

    @Autowired
    private ShadowRepository shadowRepository;


    @Autowired
    private ApplicationRepository applicationRepository;


    @Override
    public void notify(DelegateExecution exec) {
        // TODO Auto-generated method stub
        logger.info("--DockingPostService--");
        String pid = exec.getProcessInstanceId();
        Map<String, Object> vars = exec.getVariables();
        String vid = vars.get("vid").toString();
        String applyId = vars.get("applyId").toString();
        logger.debug("applyId : " + applyId);
        VesselShadow vesselShadow = shadowRepository.findById(vid);
        logger.debug("status : " + vesselShadow.getStatus());

        Destination curPort = vesselShadow.getDestinations().get(vesselShadow.getStepIndex());
        Destination nextPort = null;
        String pname = curPort.getName();
        if (vesselShadow.getStepIndex() == vesselShadow.getDestinations().size() - 1) {
            logger.debug("Arrival at the last port ：　" + pname);
            runtimeService.setVariable(pid, "nextNav", false);
        } else {
            nextPort = vesselShadow.getDestinations().get(vesselShadow.getStepIndex() + 1);
        }
//        stompClient.sendCurrentPort("admin", "/topic/dock/end", curPort, vesselShadow.getStatus(), nextPort);
        logger.debug(curPort.getName());


        //TODO: if  process has applied for spare parts.
        if (!applyId.equals("NONE")) {
            Application application = applicationRepository.findById(applyId);
            String rend = application.getRendezvous();
            //TODO:  if  delivery is continuing , then  check the status of delivery.
            String applyStatus = application.getStatus();
            if (!(applyStatus.equals("Missing") || applyStatus.equals("Meeting"))) {//exclude the  end status.
                logger.debug("rend " + rend + " applyStatus : " + applyStatus);
                String deliveryStatus = restClient.checkDeiveryStatus(pid);
                logger.debug(deliveryStatus);
                switch (deliveryStatus) {
                    case "MISSING":
                        runtimeService.setVariable(pid, "nextNav", false);
                        application.setStatus("Missing");
                        //TODO: notify logistic of "Missing"
                        HashMap<String, Object> msgBody = new HashMap<String, Object>();
                        msgBody.put("eventType", "MISSING");
                        restClient.notifyMsg(pid, "Missing", msgBody);
//                        stompClient.sendMissingMsg("admin","/topic/missing" , pid , "MISSING");
//                        logger.debug("send \"MISSING\" message to monitor : ");
                        runtimeService.setVariable(pid, "processStatus", "Missing");
                        break;
                    case "NOT_MISSING":
                        if (rend.equals(pname)) { // check  whether the delivery is successful when departure.
                            application.setStatus("Meeting");
                            //TODO: notify logistic of "Meeting"
                            msgBody = new HashMap<String, Object>();
                            msgBody.put("eventType", "MEETING");
                            restClient.notifyMsg(pid, "Meeting", msgBody);
//                            stompClient.sendMeetMsg("admin", "/topic/meeting", pid, "MEET", application.getRendezvous());
                            logger.debug("send \"MEET\" message to monitor : ");
                            runtimeService.setVariable(pid, "processStatus", "Meeting");
                        } else {
                            runtimeService.setVariable(pid, "processStatus", "Voyaging");
                            logger.info("There still exists opportunity to meet!");
                        }
                        break;
                    case "MEETING":
                        runtimeService.setVariable(pid, "nextNav", false);
                        msgBody = new HashMap<String, Object>();
                        msgBody.put("eventType", "MEETING");
                        restClient.notifyMsg(pid, "Meeting", msgBody);
//                        stompClient.sendMeetMsg("admin", "/topic/meeting", pid, "MEET", application.getRendezvous());
                        logger.debug("MEETING");
                        runtimeService.setVariable(pid, "processStatus", "Meeting");
                        break;
                    case "NOT_PAIRED":
                        logger.debug("NOT_PAIRED");
                        runtimeService.setVariable(pid, "processStatus", "Voyaging");
                        break;
                    default:
                        runtimeService.setVariable(pid, "processStatus", "Voyaging");
                        break;
                }
            }
        } else {
            runtimeService.setVariable(pid, "processStatus", "Voyaging");

        }

        logger.debug("process status : "+runtimeService.getVariable(pid , "processStatus"));
//        stompClient.sendDockComplete("admin", "/topic/dock/complete" , pid , "DockComplete");
    }

}
