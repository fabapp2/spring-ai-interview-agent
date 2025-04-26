package io.promptics.jobagent.interviewplan;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface ThreadRepository extends MongoRepository<Thread, String> {
}
