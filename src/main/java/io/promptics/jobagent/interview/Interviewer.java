package io.promptics.jobagent.interview;

import io.promptics.jobagent.InterviewContext;
import io.promptics.jobagent.interviewplan.ConversationEntry;
import io.promptics.jobagent.interviewplan.InterviewPlanMongoTools;
import io.promptics.jobagent.interviewplan.InterviewPlanService;
import io.promptics.jobagent.interviewplan.TopicAndThread;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.stereotype.Component;

/**
 * The Interviewer agent conducts the interview.
 * 
 * It has access to the currently active Topic and Thread and decides about how to proceed.
 * The agent can:
 *
 * <p>
 * <b>Ask a new question in the current thread:</b>
 * The agent decides that more information is required to complete the current thread, 
 * decides which question to ask next and returns the question as response.
 * </p>
 * <p>
 * <b>Create a new, follow-up thread:</b>
 * The agent realizes that the user's reply requires a follow-up question and creates a new thread to follow-up upon.
 *  </p>
 *  <p>
 * <b>Complete the current thread:</b>
 * The agent can decide that the gathered information is enough to complete the current thread.
 * It will then either retrieve the next thread or
 *</p>
 * <p>
 * <b>finish the interview:</b>
 * The agent completed the last thread and can thus declare the imterview as finished.
 * </p>
 */
@Component
public class Interviewer {

    private static final String systemPrompt = """
            You are an experienced interviewer.
            You are conducting a user interview to gather career data from a user.
            
            The interview follows a given plan.
            The plan has broad topics and a set of threads per topic.
            Your task is to define questions with the goal to extract the targeted information per thread.
            You decide when the thread is completed.
            
            
            {current_thread}
          
            
            # Expected Output:
            Ask one question at a time.
            """;

    private final ChatClient client;
    private final InterviewPlanMongoTools mongoDbTools;
    private final InterviewPlanService interviewPlanService;

    /**
     * @param builder
     * @param mongoDbTools
     * @param interviewPlanService
     */
    public Interviewer(ChatClient.Builder builder, InterviewPlanMongoTools mongoDbTools, InterviewPlanService interviewPlanService) {
        client = builder.defaultOptions(ChatOptions.builder().temperature(0.0).build()).build();
        this.mongoDbTools = mongoDbTools;
        this.interviewPlanService = interviewPlanService;
    }

    /**
     * Run the interview.
     *
     * @param context the InterviewContext
     * @param input the
     */
    public String run(InterviewContext context, String input) {

        TopicAndThread topicAndThread = getCurrentlyActiveThread(context);

        String topicThreadSummary = topicAndThread.render();

        addUserInputToConversation(input, topicAndThread);

        String prompt = systemPrompt.replace("{current_thread}", topicThreadSummary);

        String output = client.prompt()
                .system(prompt)
                .user(input)
                .tools(mongoDbTools)
                .call()
                .content();

        addAssistantOutputToConversation(topicAndThread, output);

        return output;
    }

    private void addAssistantOutputToConversation(TopicAndThread topicAndThread, String output) {
        interviewPlanService.addToThreadConversation(topicAndThread.getThreadId(), ConversationEntry.builder()
                .role("assistant")
                .text(output)
                .build());
    }

    private void addUserInputToConversation(String input, TopicAndThread topicAndThread) {
        interviewPlanService.addToThreadConversation(topicAndThread.getThreadId(), ConversationEntry.builder()
                        .role("user")
                        .text(input)
                        .build());
    }

    private TopicAndThread getCurrentlyActiveThread(InterviewContext context) {
        return interviewPlanService.findCurrentTopicAndThread(context.getPlanId());
    }

}
