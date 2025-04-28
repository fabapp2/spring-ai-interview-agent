package io.promptics.jobagent.interviewplan.agents;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.promptics.jobagent.careerdata.model.Basics;
import io.promptics.jobagent.interviewplan.model.Topic;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BasicsTopicPlanningAgentTest {

    @Autowired
    BasicsTopicPlanningAgent agent;
    @Autowired
    private BasicsTopicPlanningAgent basicsTopicPlanningAgent;
    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("plan")
    void plan() throws JsonProcessingException {
        Basics basicsSection = objectMapper.readValue("""
                {
                  "name": "Max Müller",
                  "email": "max@foo.com",
                  "summary": "",
                  "location": {
                    "city": "",
                    "countryCode": "DE"
                  },
                  "profiles": [
                    {
                      "id": "1111111111",
                      "network": "SomeCareerPlatform",
                      "username": "",
                      "url": ""
                    }
                  ]
                }
                """, Basics.class);

        List<Topic> topics = agent.planTopics(basicsSection);
    }

}