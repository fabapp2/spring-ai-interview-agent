package io.promptics.jobagent.interviewplan;

import io.promptics.jobagent.interviewplan.model.TopicThread;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TopicThreadRepository extends MongoRepository<TopicThread, String> {
}
