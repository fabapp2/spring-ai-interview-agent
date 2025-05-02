package io.promptics.jobagent.interview;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface ThreadConversationRepository extends MongoRepository<ThreadConversation, String> {
}
