package io.promptics.jobagent.interviewplan;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InterviewPlanRepository extends MongoRepository<InterviewPlan, String> {

}
