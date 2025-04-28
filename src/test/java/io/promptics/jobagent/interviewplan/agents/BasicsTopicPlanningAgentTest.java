package io.promptics.jobagent.interviewplan.agents;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.knuddels.jtokkit.Encodings;
import com.knuddels.jtokkit.api.*;
import io.promptics.jobagent.careerdata.model.Basics;
import io.promptics.jobagent.interviewplan.model.Topic;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class BasicsTopicPlanningAgentTest {

    @Autowired
    BasicsTopicPlanningAgent agent;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("system prompt should be cacheable")
    void systemPromptShouldBeCacheable() {
        EncodingRegistry registry = Encodings.newDefaultEncodingRegistry();
        Encoding enc = registry.getEncoding(EncodingType.CL100K_BASE);
        IntArrayList encoded = enc.encode(BasicsTopicPlanningAgent.SYSTEM_PROMPT);
        assertThat(encoded.size()).isGreaterThan(1024); // cached when more than 1024 tokens
    }

    @Test
    @DisplayName("plan")
    void plan() throws JsonProcessingException {
        Basics basicsSection = objectMapper.readValue("""
                {x
                  "name": "Max Müller",
                  "email": "max@foo.com",
                  "summary": "",
                  "id": "1133776655",
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
        assertThat(topics).isNotEmpty();
        List<Topic> generalBasicsTopics = topics.stream().filter(t -> t.getReference().getResumeItemId().equals("1133776655")).toList();
        List<Topic> profilesBasicsTopics = topics.stream().filter(t -> t.getReference().getResumeItemId().equals("1111111111")).toList();

        assertThat(generalBasicsTopics).hasSize(2);
        assertThat(generalBasicsTopics).extracting(Topic::getReason)
                .contains(
                        "Candidate's city information is missing.",
                        "Candidate's professional summary is missing."
                );
        assertThat(profilesBasicsTopics).hasSize(1);
        assertThat(profilesBasicsTopics.get(0).getReason()).contains("username", "URL");
    }

}