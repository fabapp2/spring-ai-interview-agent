package io.promptics.jobagent.interviewplan;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.promptics.jobagent.CareerDataInterviewConfig;
import io.promptics.jobagent.MongoDbConfig;
import io.promptics.jobagent.interviewplan.model.Topic;
import org.intellij.lang.annotations.Language;
import org.json.JSONException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
@Testcontainers
@Import(CareerDataInterviewConfig.class)
public class TopicRepositoryTest {


    @Container
    @ServiceConnection
    final static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:%s".formatted(MongoDbConfig.MONGODB_VERSION));

    @Autowired
    TopicRepository topicRepository;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("json1")
    void persistJson1() throws JSONException {
        String given = JSON_1;
        assertPersisted(given);
    }

    @Test
    @DisplayName("json2")
    void persistJson2() throws JSONException {
        String given = JSON_2;
        assertPersisted(given);
    }

    @Test
    @DisplayName("json3")
    void persistJson3() throws JSONException {
        String given = JSON_3;
        assertPersisted(given);
    }

    @Test
    @DisplayName("json4")
    void persistJson4() throws JSONException {
        String given = JSON_4;
        assertPersisted(given);
    }

    private void assertPersisted(String given) throws JSONException {
        Topic topic = deserialize(given, Topic.class);
        Topic saved = topicRepository.save(topic);
        assertThat(saved.getId()).isNotNull();
        String json = serialize(saved);
        JSONAssert.assertEquals(given, json, JSONCompareMode.STRICT);
    }

    private String serialize(Topic saved) {
        try {
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(saved);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private Topic deserialize(String json, Class<Topic> topicClass) {
        try {
            return objectMapper.readValue(json, topicClass);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Language("json")
    private static final String JSON_1 = """
            {
              "id": "662a2cf3b931226b6dcb3001",
              "type": "basics",
              "reference": {
                "resumeItemId": "basics123"
              },
              "gapType": "time",
              "reason": "Update basic personal details",
              "reasonMeta": {
                "detectedFields": [
                  "name",
                  "email"
                ],
                "triggeredBy": "manual_review"
              },
              "priorityScore": 90,
              "priority": "high",
              "createdAt": "2025-04-25T08:00:00Z",
              "updatedAt": "2025-04-25T08:10:00Z"
            }
            """;

    @Language("json")
    private static final String JSON_2 = """
              {
                "id": "662a2cf3b931226b6dcb3001",
                "type": "basics",
                "reason": "Update basic personal details",
                "reasonMeta": {
                  "detectedFields": [
                    "name",
                    "email"
                  ],
                  "triggeredBy": "manual_review"
                },
                "priorityScore": 90,
                "priority": "high",
                "createdAt": "2025-04-25T08:00:00Z",
                "updatedAt": "2025-04-25T08:10:00Z",
                "gapType": "time"
              }
            """;

    @Language("json")
    private static final String JSON_3 = """
             {
                "id": "662a2cf3b931226b6dcb3001",
                "type": "basics",
                "reason": "Update basic personal details",
                "reasonMeta": {
                  "detectedFields": [
                    "name",
                    "email"
                  ],
                  "triggeredBy": "manual_review"
                },
                "priorityScore": 90,
                "priority": "high",
                "createdAt": "2025-04-25T08:00:00Z",
                "updatedAt": "2025-04-25T08:10:00Z",
                "reference": {
                  "resumeItemAfterId": "after123"
                }
              }
            """;

    @Language("json")
    public static final String JSON_4 = """
            {
                "id": "662a2cf3b931226b6dcb3001",
                "type": "basics",
                "reason": "Update basic personal details",
                "reasonMeta": {
                  "detectedFields": [
                    "name",
                    "email"
                  ],
                  "triggeredBy": "manual_review"
                },
                "priorityScore": 90,
                "priority": "high",
                "createdAt": "2025-04-25T08:00:00Z",
                "updatedAt": "2025-04-25T08:10:00Z",
                "reference": {
                  "resumeItemBeforeId": "before123",
                  "resumeItemAfterId": "after123"
                }
              }
            """;
}
