package io.promptics.jobagent.interviewplan;

import org.bson.types.ObjectId;
import org.springframework.boot.autoconfigure.context.PropertyPlaceholderAutoConfiguration;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@Component
public class InterviewPlanService {

    private final MongoTemplate mongoTemplate;
    private final PropertyPlaceholderAutoConfiguration propertyPlaceholderAutoConfiguration;

    public InterviewPlanService(MongoTemplate mongoTemplate, PropertyPlaceholderAutoConfiguration propertyPlaceholderAutoConfiguration) {
        this.mongoTemplate = mongoTemplate;
        this.propertyPlaceholderAutoConfiguration = propertyPlaceholderAutoConfiguration;
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

    /**
     * Find the next active topic and thread.
     */
    public TopicAndThread findCurrentTopicAndThread(String planId) {
        Aggregation aggregation2 = Aggregation.newAggregation(
                match(Criteria.where("_id").is(new ObjectId(planId))),
                unwind("topics", true),
                unwind("topics.threads", true),
                match(new Criteria().orOperator(
                        Criteria.where("topics.threads.status").is("IN_PROGRESS"),
                        Criteria.where("topics.threads.status").is("PENDING")
                )),
                sort(Sort.by("topics.threads.status")),
                limit(1),
                group().first("$$ROOT").as("doc"),
                replaceRoot("doc"),
                project()
                        .and("topics._id").as("topic.id")
                        .and("topics.type").as("topic.type")
                        .and("topics.reference").as("topic.reference")
                        .and("topics.threads").as("thread")
        );

        return mongoTemplate.aggregate(
                aggregation2,
                "interview_plan",
                TopicAndThread.class
        ).getUniqueMappedResult();
    }

    public InterviewPlan saveInterviewPlan(InterviewPlan plan) {
        return mongoTemplate.save(plan);
    }
}
