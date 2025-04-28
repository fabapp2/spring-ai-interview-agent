package io.promptics.jobagent.interviewplan;


import io.promptics.jobagent.interviewplan.model.ThreadTopic;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TopicRepository extends MongoRepository<ThreadTopic, String> {
}
