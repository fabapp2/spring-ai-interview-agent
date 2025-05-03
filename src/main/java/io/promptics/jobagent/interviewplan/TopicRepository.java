package io.promptics.jobagent.interviewplan;


import io.promptics.jobagent.interviewplan.model.Topic;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TopicRepository extends MongoRepository<Topic, String> {
    Topic findFirstByCareerDataIdOrderByPriorityScoreDesc(String careerDataId);
}
