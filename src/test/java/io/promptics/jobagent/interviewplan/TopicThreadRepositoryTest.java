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
                  "id": "662a2cf3b931226b6dcb4001",
                  "topicId": "662a2cf3b931226b6dcb3001",
                  "type": "core_details",
                  "status": "pending",
                  "duration": 300,
                  "contextAction": "ask",
                  "contextGoal": "extract_responsibilities"
                }
                """);
    }
}
