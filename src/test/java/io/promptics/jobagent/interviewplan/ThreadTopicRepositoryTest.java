package io.promptics.jobagent.interviewplan;

import io.promptics.jobagent.interviewplan.model.ThreadTopic;
import org.intellij.lang.annotations.Language;
import org.json.JSONException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.MongoRepository;


public class ThreadTopicRepositoryTest extends RepositoryTest<ThreadTopic, String, TopicRepository> {

    @Autowired
    TopicRepository topicRepository;

    protected ThreadTopicRepositoryTest() {
        super(ThreadTopic.class);
    }

    @Override
    protected MongoRepository getRepository() {
        return topicRepository;
    }

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

    @Language("json")
    private static final String JSON_1 = """
            {
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
