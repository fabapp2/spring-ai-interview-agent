package io.promptics.jobagent.interviewplan;

import io.promptics.jobagent.interviewplan.model.TopicThread;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TopicThreadRepository extends MongoRepository<TopicThread, String> {
    List<TopicThread>  findByTopicId(String topicId);
}
