package io.promptics.jobagent.interviewplan.agents;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.knuddels.jtokkit.Encodings;
import com.knuddels.jtokkit.api.Encoding;
import com.knuddels.jtokkit.api.EncodingRegistry;
import com.knuddels.jtokkit.api.EncodingType;
import com.knuddels.jtokkit.api.IntArrayList;
import io.promptics.jobagent.careerdata.model.Basics;
import io.promptics.jobagent.interviewplan.model.Topic;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.ExpectedToFail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class BasicsTopicPlanningAgentTest {

    public static final String VALID_JSON = """
            {
              "name": "Max Müller",
              "email": "max@foo.com",
              "summary": "Software engineer with 2 years of experience, specializing in JavaScript and cloud technologies such as AWS and serverless architectures.",
              "id": "1133776655",
              "label": "Software Engineer specialized in Cloud and Javascript",
              "phone": "+66 2234 2233",
              "location": {
                "countryCode": "DE",
                "city": "Berlin"
              },
              "profiles": [
                {
                  "id": "1111111111",
                  "network": "Linkedin",
                  "username": "Max",
                  "url": "https://linkedin.com/in/max"
                }
              ]
            }
            """;
    @Autowired
    BasicsTopicPlanningAgent agent;

    @Autowired
    ObjectMapper objectMapper;
    private Basics validBasics;

    @BeforeEach
    void beforeEach() throws JsonProcessingException {
        validBasics = objectMapper.readValue(VALID_JSON, Basics.class);
    }

    @Test
    @DisplayName("system prompt should be cacheable")
    void systemPromptShouldBeCacheable() {
        EncodingRegistry registry = Encodings.newDefaultEncodingRegistry();
        Encoding enc = registry.getEncoding(EncodingType.CL100K_BASE);
        IntArrayList encoded = enc.encode(BasicsTopicPlanningAgent.SYSTEM_PROMPT);
        assertThat(encoded.size()).isGreaterThan(1024); // cached when more than 1024 tokens
    }

    @Test
    @DisplayName("minimal basics valid")
    void minimalBasicsValid() throws JsonProcessingException {
        String careerDataId = "666666666";
        List<Topic> topics = agent.planTopics(careerDataId, validBasics);
        assertThat(topics).hasSize(0);
    }

    @Test
    @DisplayName("no city")
    void noCity() {
        Basics basicsNoCity = validBasics;
        basicsNoCity.getLocation().setCity(null);
        String careerDataId = "666666666";
        List<Topic> topics = agent.planTopics(careerDataId, basicsNoCity);
        assertThat(topics).hasSize(1);
        assertThat(topics.get(0).getReason()).contains("city");
    }
    
    @Test
    @DisplayName("professional summary and country missing")
    void plan() {
        Basics summaryMissing = validBasics;
        summaryMissing.setSummary(null);
        summaryMissing.getLocation().setCountryCode(null);

        String careerDataId = "666666666";
        List<Topic> topics = agent.planTopics(careerDataId, summaryMissing);

        assertThat(topics).hasSize(1);
        assertThat(topics.get(0).getReason()).contains("country");
    }

}