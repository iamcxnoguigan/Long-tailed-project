package com.l2l.enterprise.vessel.domain;

import java.io.Serializable;
import java.util.List;

public class StopOrderMsg extends MsgImpl implements Serializable {
    private List<String> vids;
    public StopOrderMsg() {
        super();
    }

    public StopOrderMsg(List<String> pids, String connectorType, String topic, String scenario,List<String> vids) {
        super(pids, connectorType, topic, scenario);
        this.vids = vids;
    }

    public List<String> getVids() {
        return vids;
    }

    public void setVids(List<String> vids) {
        this.vids = vids;
    }
}
