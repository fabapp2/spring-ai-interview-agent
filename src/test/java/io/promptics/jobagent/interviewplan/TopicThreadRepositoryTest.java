package io.promptics.jobagent.interviewplan;

import io.promptics.jobagent.interviewplan.model.TopicThread;
import org.json.JSONException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.MongoRepository;

public class TopicThreadRepositoryTest extends RepositoryTest<TopicThread, String, TopicThreadRepository> {

    @Autowired
    TopicThreadRepository threadRepository;

    public TopicThreadRepositoryTest() {
        super(TopicThread.class);
    }

    @Override
    protected MongoRepository<TopicThread, String> getRepository() {
        return threadRepository;
    }

    @Test
    @DisplayName("persist minimal")
    void persist() throws JSONException {
        assertPersisted(
                """
                {
                  "careerDataId": "1234",
                  "id": "662a2cf3b931226b6dcb4001",
                  "topicId": "662a2cf3b931226b6dcb3001",
                  "status": "pending",
                  "focus": "team_context",
                  "createdAt": "2025-04-25T08:00:00Z",
                  "updatedAt": "2025-04-25T08:10:00Z"
                }
                """);
    }
}
