package io.promptics.jobagent.interviewplan;

import io.promptics.jobagent.interview.ConversationEntry;
import io.promptics.jobagent.interview.ThreadConversation;
import io.promptics.jobagent.interviewplan.model.Topic;
import io.promptics.jobagent.interviewplan.model.TopicThread;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@Component
@RequiredArgsConstructor
@Deprecated // use INterviewPlanner
public class InterviewPlanService {

    private final MongoTemplate mongoTemplate;
    private final TopicRepository topicRepository;
    private final TopicThreadRepository threadRepository;
    private final TopicThreadRepository topicThreadRepository;

    /**
     * Add a new message to the conversation of a thread.
     */
    public ThreadConversation addToThreadConversation(String threadId, ConversationEntry entry) {
        ThreadConversation conversation = mongoTemplate.findById(threadId, ThreadConversation.class);
        if(conversation == null) {
            conversation = new ThreadConversation();
            conversation.setThreadId(threadId);
        }
        conversation.getEntries().add(entry);
        return mongoTemplate.save(conversation);
    }

    /**
     * Find the next active topic and thread.
     */
    public TopicAndThread findCurrentTopicAndThread(String careerDataId) {
        // FIXME: get the next topic with highest priority
        Topic curTopic = topicRepository.findAll().get(0);
        List<TopicThread> curThread = topicThreadRepository.findByTopicId(curTopic.getId());
        return new TopicAndThread(curTopic, curThread.get(0));
    }

    public void saveTopics(List<Topic> topics) {
        topicRepository.saveAll(topics);
    }

    public void saveThreads(List<TopicThread> threads) {
        threadRepository.saveAll(threads);
    }
}
