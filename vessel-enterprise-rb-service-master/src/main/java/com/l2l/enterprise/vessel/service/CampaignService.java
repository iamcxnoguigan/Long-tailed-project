package com.l2l.enterprise.vessel.service;

import com.l2l.enterprise.vessel.domain.Tweet;
import org.activiti.engine.RuntimeService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
public class CampaignService {

    private final String currentTopic;
    private final RuntimeService runtimeService;

    public CampaignService(@Value("${campaign.topic}") String currentTopic,
                           RuntimeService runtimeService) {
        this.currentTopic = currentTopic;
        this.runtimeService = runtimeService;
    }

    public String getCurrentTopic() {
        return currentTopic;
    }

    public void processTweet(Tweet tweet) {
        Map<String, Object> vars = new HashMap<>();
        vars.put("text",
                 tweet.getText());
        vars.put("author",
                 tweet.getAuthor());
        vars.put("lang",
                 tweet.getLang());
        vars.put("timestamp",
                 tweet.getTimestamp());
        vars.put("campaign",
                 currentTopic);
        runtimeService.startProcessInstanceByKey("launchCampaign",
                                                 currentTopic,
                                                 //BusinessKey
                                                 vars);
    }

    public void rewardTopUsers() {
        Map<String, Object> vars = new HashMap<>();
        vars.put("campaign",
                 currentTopic);
        vars.put("nroTopAuthors",
                 3);
        vars.put("top",
                 new ArrayList<>());
        runtimeService.startProcessInstanceByKey("tweet-prize",
                                                 vars);
    }
}
