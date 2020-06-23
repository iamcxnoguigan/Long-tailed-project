package com.l2l.contextsharing.dmn;

import com.l2l.contextsharing.VesselManagerCoordinatorApp;
import org.camunda.bpm.dmn.engine.DmnDecision;
import org.camunda.bpm.dmn.engine.DmnDecisionRuleResult;
import org.camunda.bpm.dmn.engine.DmnDecisionTableResult;
import org.camunda.bpm.dmn.engine.DmnEngine;
import org.camunda.bpm.dmn.engine.test.DmnEngineRule;
import org.camunda.bpm.engine.variable.VariableMap;
import org.camunda.bpm.engine.variable.Variables;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = VesselManagerCoordinatorApp.class)
@ActiveProfiles("dev")
public class CamundaDMNTest {
    private static final Logger log = LoggerFactory.getLogger(CamundaDMNTest.class);

    @Rule
    public DmnEngineRule rule = new DmnEngineRule();

    @Test
    public void shouldEvaluateDecision(){
        //Get DMN engine
        DmnEngine dmnEngine = rule.getDmnEngine();
        //Parse decision
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("dmn/example.dmn");
        DmnDecision decision = dmnEngine.parseDecision("orderDecision" , inputStream);

        //Set input variables
        VariableMap variableMap = Variables.createVariables()
            .putValue("status" , "silver")
            .putValue("sum" , 9000);

        //Evaluate decision with id 'orderDecision" from file 'example.dmn'
        DmnDecisionTableResult results = dmnEngine.evaluateDecisionTable(decision , variableMap);

        //Check that one rule has matched
        assertThat(results).hasSize(1);

        DmnDecisionRuleResult result = results.getSingleResult();
        assertThat(result)
            .containsOnly(
                entry("result" , "notok") ,
                entry("reason" , "you took too much man, you took too much!")
            );
        // Change input variables
        variableMap.putValue("status" , "gold");

        //Evaluate decision again
        results = dmnEngine.evaluateDecisionTable(decision , variableMap);

        //Check new result
        assertThat(results).hasSize(1);

        result = results.getSingleResult();
        assertThat(result)
            .containsOnly(
                entry("result" , "ok"),
                entry("reason" , "you get anything you want")
            );
    }

}
