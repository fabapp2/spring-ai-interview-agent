package io.promptics.jobagent.interview;

import io.promptics.jobagent.InterviewContext;
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

    private final ChatClient client;
    private final InterviewPlanMongoTools mongoDbTools;
    private final InterviewPlanService interviewPlanService;
    private final InterviewPromptBuilder promptBuilder;
    /**
     * @param builder
     * @param mongoDbTools
     * @param interviewPlanService
     */
    public Interviewer(ChatClient.Builder builder, InterviewPlanMongoTools mongoDbTools, InterviewPlanService interviewPlanService, InterviewPromptBuilder promptBuilder) {
        client = builder.defaultOptions(ChatOptions.builder().temperature(0.0).build()).build();
        this.mongoDbTools = mongoDbTools;
        this.interviewPlanService = interviewPlanService;
        this.promptBuilder = promptBuilder;
    }

    /**
     * Run the interview.
     *
     * @param context the InterviewContext
     * @param input the
     */
    public String execute(InterviewContext context, String input) {

        TopicAndThread topicAndThread = getCurrentlyActiveThread(context);

        ThreadConversation conversation = addUserInputToConversation(input, topicAndThread);

        String additionalContext = "";

        String prompt = promptBuilder.buildPrompt(
                topicAndThread.getTopic(),
                topicAndThread.getThread(),
                conversation,
                additionalContext
        );

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
        interviewPlanService.addToThreadConversation(topicAndThread.getThread().getIdentifier(), ConversationEntry.builder()
                .role("assistant")
                .text(output)
                .build());
    }

    private ThreadConversation addUserInputToConversation(String input, TopicAndThread topicAndThread) {
        return interviewPlanService.addToThreadConversation(topicAndThread.getThread().getIdentifier(), ConversationEntry.builder()
                .role("user")
                .text(input)
                .build());
    }

    private TopicAndThread getCurrentlyActiveThread(InterviewContext context) {
        return interviewPlanService.findCurrentTopicAndThread(context.getPlanId());
    }

}
