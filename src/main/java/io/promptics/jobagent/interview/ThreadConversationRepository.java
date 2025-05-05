package io.promptics.jobagent.interview;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ThreadConversationRepository extends MongoRepository<ThreadConversation, String> {
    Optional<ThreadConversation> findThreadConversationByThreadId(String threadId);
}
