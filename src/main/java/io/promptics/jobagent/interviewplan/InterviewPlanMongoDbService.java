package io.promptics.jobagent.interviewplan;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class InterviewPlanMongoDbService {

    private final MongoTemplate mongoTemplate;

    public InterviewPlanMongoDbService(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    /**
     * Add a new message to the conversation of a thread.
     */
    public void addToThreadConversation(String threadId, ConversationEntry entry) {
        Thread thread = mongoTemplate.findById(threadId, Thread.class);
        thread.getConversation().add(entry);
        mongoTemplate.save(thread);
    }

    /**
     * Mark a {@link Thread} as completed and get the next {@link TopicAndThread} in return.
     */
    public Optional<TopicAndThread> completeThreadAndGetNext(TopicAndThread topicAndThread) {
        return null;
    }

    /**
     * Add a new {@link Thread} to current {@link Topic} to followup.
     */
    public void addThreadToCurrentTopic(Thread newThread, String relatedThreadId) {

    }

    public TopicAndThread findCurrentTopicAndThread(String planId) {
        Aggregation aggregation = Aggregation.newAggregation(
                // Match the plan
                Aggregation.match(Criteria.where("_id").is(planId)),

                // Unwind topics and threads
                Aggregation.unwind("topics"),
                Aggregation.unwind("topics.threads"),

                // Match in_progress or pending threads
                Aggregation.match(new Criteria().orOperator(
                        Criteria.where("topics.threads.status").is("in_progress"),
                        Criteria.where("topics.threads.status").is("pending")
                )),

                // Sort to get in_progress before pending
                Aggregation.sort(Sort.by("topics.threads.status")),

                // Take the first one
                Aggregation.limit(1),

                // Project to desired output format
                Aggregation.project()
                        .and("topics").as("topic")
                        .and("topics.threads").as("thread")
        );

        AggregationResults<TopicAndThread> results = mongoTemplate.aggregate(
                aggregation,
                "interviewPlans",  // collection name
                TopicAndThread.class
        );

        return results.getUniqueMappedResult();
    }
}
