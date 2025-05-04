package io.promptics.jobagent.interviewplan;

import io.promptics.jobagent.interviewplan.model.Topic;
import org.intellij.lang.annotations.Language;
import org.json.JSONException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.MongoRepository;


public class TopicRepositoryTest extends RepositoryTest<Topic, String, TopicRepository> {

    @Autowired
    TopicRepository topicRepository;

    protected TopicRepositoryTest() {
        super(Topic.class);
    }

    @Override
    protected MongoRepository getRepository() {
        return topicRepository;
    }

    @Test
    @DisplayName("json1")
    void persistJson1() throws JSONException {
        assertPersisted("""
            {
              "careerDataId": "1234",
              "type": "basics",
              "reference": {
                "resumeItemIds": ["basics123"]
              },
              "reason": "Update basic personal details",
              "priorityScore": 90,
              "createdAt": "2025-04-25T08:00:00Z",
              "updatedAt": "2025-04-25T08:10:00Z"
            }
            """);
    }

    @Test
    @DisplayName("json2")
    void persistJson2() throws JSONException {
        assertPersisted("""
              {
                "careerDataId": "1234",
                "type": "basics",
                "reason": "Update basic personal details",
                "priorityScore": 90,
                "createdAt": "2025-04-25T08:00:00Z",
                "updatedAt": "2025-04-25T08:10:00Z"
              }
            """);
    }

    @Test
    @DisplayName("json3")
    void persistJson3() throws JSONException {
        assertPersisted("""
             {
                "careerDataId": "1234",
                "type": "basics",
                "reason": "Update basic personal details",
                "priorityScore": 54,
                "createdAt": "2025-04-25T08:00:00Z",
                "updatedAt": "2025-04-25T08:10:00Z",
                "reference": {
                  "resumeItemIds": ["after123"]
                }
              }
            """);
    }

    @Test
    @DisplayName("json4")
    void persistJson4() throws JSONException {
        assertPersisted("""
            {
                "careerDataId": "1234",
                "type": "basics",
                "reason": "Update basic personal details",
                "priorityScore": 90,
                "createdAt": "2025-04-25T08:00:00Z",
                "updatedAt": "2025-04-25T08:10:00Z",
                "reference": {
                  "resumeItemIds": ["before123"]
                }
              }
            """);
    }
}
