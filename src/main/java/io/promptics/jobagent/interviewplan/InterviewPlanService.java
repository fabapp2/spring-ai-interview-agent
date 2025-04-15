package io.promptics.jobagent.interviewplan;

import io.promptics.jobagent.interview.ConversationEntry;
import io.promptics.jobagent.interview.ThreadConversation;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@Component
public class InterviewPlanService {

    private final MongoTemplate mongoTemplate;

    public InterviewPlanService(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    /**
     * Add a new message to the conversation of a thread.
     */
    public void addToThreadConversation(String threadId, ConversationEntry entry) {
        ThreadConversation conversation = mongoTemplate.findById(threadId, ThreadConversation.class);
        if(conversation == null) {
            conversation = new ThreadConversation();
            conversation.setThreadId(threadId);
        }
        conversation.getEntries().add(entry);
        mongoTemplate.save(conversation);
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

        MatchOperation byId = match(Criteria.where("_id").is(new ObjectId(planId)));
        UnwindOperation unwindTopics = unwind("topics", true);
        UnwindOperation unwindTopicsThreads = unwind("topics.threads", true);
        MatchOperation matchStatus = match(new Criteria().orOperator(
                Criteria.where("topics.threads.status").is("IN_PROGRESS"),
                Criteria.where("topics.threads.status").is("PENDING")
        ));
        SortOperation sort = sort(Sort.by("topics.threads.status"));
        LimitOperation limit = limit(1);
        GroupOperation groupOperation = group().first("$$ROOT").as("doc");
        ReplaceRootOperation replaceRoot = replaceRoot("doc");
        ProjectionOperation projectionOperation = project()
                .and("topics.identifier").as("topic.identifier")
                .and("topics.type").as("topic.type")
                .and("topics.reference").as("topic.reference")
                .and("topics.threads").as("thread");

        // debug
        // mongoTemplate.aggregate(Aggregation.newAggregation(byId, unwindTopics), "interview_plan", org.bson.Document.class);

        Aggregation aggregation = Aggregation.newAggregation(
                byId,
                unwindTopics,
                unwindTopicsThreads,
                matchStatus,
                sort,
                limit,
                groupOperation,
                replaceRoot,
                projectionOperation
        );

        return mongoTemplate.aggregate(
                aggregation,
                "interview_plan",
                TopicAndThread.class
        ).getUniqueMappedResult();
    }

    public InterviewPlan saveInterviewPlan(InterviewPlan plan) {
        return mongoTemplate.save(plan);
    }
}
