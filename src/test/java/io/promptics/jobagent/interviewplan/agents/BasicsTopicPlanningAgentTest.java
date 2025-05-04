package io.promptics.jobagent.interviewplan.agents;

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
    private Basics validBasics;

    @BeforeEach
    void beforeEach() {
        validBasics = ValidBasicsProvider.provideObject(objectMapper);
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
    void minimalBasicsValid() {
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

        // FIXME: Test flaky, sometimes "summary" is marked. Add example
        String careerDataId = "666666666";
        List<Topic> topics = agent.planTopics(careerDataId, summaryMissing);

        assertThat(topics).hasSize(1);
        assertThat(topics.get(0).getReason()).contains("country");
    }

}