package io.promptics.jobagent.interviewplan.agents;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.knuddels.jtokkit.Encodings;
import com.knuddels.jtokkit.api.Encoding;
import com.knuddels.jtokkit.api.EncodingRegistry;
import com.knuddels.jtokkit.api.EncodingType;
import com.knuddels.jtokkit.api.IntArrayList;
import io.promptics.jobagent.careerdata.model.Basics;
import io.promptics.jobagent.interviewplan.model.Thread;
import io.promptics.jobagent.interviewplan.model.ThreadTopic;
import org.intellij.lang.annotations.Language;
import org.junit.AssumptionViolatedException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class BasicsThreadsPlanningAgentTest {

    @Autowired
    BasicsThreadsPlanningAgent agent;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("system prompt should be cacheable")
    void systemPromptShouldBeCacheable() {
        EncodingRegistry registry = Encodings.newDefaultEncodingRegistry();
        Encoding enc = registry.getEncoding(EncodingType.CL100K_BASE);
        IntArrayList encoded = enc.encode(BasicsThreadsPlanningAgent.SYSTEM_PROMPT);
        assertThat(encoded.size()).isGreaterThan(1024); // cached when more than 1024 tokens
    }

    @Test
    @DisplayName("generate threads for topui")
    void generateThreadsForTopics() throws JsonProcessingException {
        Basics basicsSection = objectMapper.readValue(BASICS_JSON, Basics.class);

        List<ThreadTopic> topics = objectMapper.readValue(TOPICS_JSON, new TypeReference<>() {});
        List<Thread> threads = agent.planThreads(basicsSection, topics);

        assertThat(threads).hasSize(3);
        Thread thread1 = findThreadById(threads, "topic-1");
        assertThat(thread1.getTopicId()).isEqualTo("1133776655");
        Thread thread2 = findThreadById(threads, "topic-2");
        assertThat(thread2.getTopicId()).isEqualTo("1133776655");
        Thread thread3 = findThreadById(threads, "topic-3");
        assertThat(thread3.getTopicId()).isEqualTo("1111111111");
    }

    private Thread findThreadById(List<Thread> threads, String id) {
        return threads.stream()
                .filter(t -> t.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new AssumptionViolatedException("No thread found with id %d".formatted(id)));
    }

    @Language("json")
    public static final String BASICS_JSON = """
            {
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
            """;

    @Language("json")
    private static final String TOPICS_JSON = """
            [
               {
                 "id": "topic-1",
                 "type": "basics",
                 "reference": { "resumeItemId": "1133776655" },
                 "reason": "Candidate's professional summary is missing."
               },
               {
                 "id": "topic-2",
                 "type": "basics",
                 "reference": { "resumeItemId": "1133776655" },
                 "reason": "Candidate's city information is missing."
               },
               {
                 "id": "topic-3",
                 "type": "basics",
                 "reference": { "resumeItemId": "1111111111" },
                 "reason": "SomeCareerPlatform profile details are incomplete (username and URL missing)."
               }
            ]
            """;

}