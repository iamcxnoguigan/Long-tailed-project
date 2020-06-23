package com.l2l.enterprise.vessel.domain;



public class IoTSetting {
    private String id;
    private String defaultTopic;
    private String customTopic;

    public IoTSetting(String id, String defaultTopic, String customTopic) {
        this.id = id;
        this.defaultTopic = defaultTopic;
        this.customTopic = customTopic;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDefaultTopic() {
        return defaultTopic;
    }

    public void setDefaultTopic(String defaultTopic) {
        this.defaultTopic = defaultTopic;
    }

    public String getCustomTopic() {
        return customTopic;
    }

    public void setCustomTopic(String customTopic) {
        this.customTopic = customTopic;
    }
}
