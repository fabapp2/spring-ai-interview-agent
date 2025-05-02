package io.promptics.jobagent.interview;

import io.promptics.jobagent.InterviewContext;
import io.promptics.jobagent.interviewplan.InterviewPlanner;
import io.promptics.jobagent.interviewplan.TopicAndThread;
import io.promptics.jobagent.interviewplan.model.Topic;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * The Interviewer agent conducts the interview.
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
 * The agent completed the last thread and can thus declare the interview as finished.
 * </p>
 */
@Component
public class Interviewer {

    private final ChatClient client;
    private final InterviewPromptBuilder promptBuilder;
    private final ConversationAnalyzer analyzer;
    private final InterviewPlanner interviewPlanner;

    public Interviewer(ChatClient.Builder builder, InterviewPromptBuilder promptBuilder, ConversationAnalyzer analyzer, InterviewPlanner interviewPlanner) {
        client = builder.defaultOptions(ChatOptions.builder().temperature(0.0).build()).build();
        this.promptBuilder = promptBuilder;
        this.analyzer = analyzer;
        this.interviewPlanner = interviewPlanner;
    }

    /**
     * Run the interview.
     *
     * @param context the InterviewContext
     * @param input   the
     */
    public String execute(InterviewContext context, String input) {
        // FIXME: move plan creation elsewhere
        List<Topic> plan = interviewPlanner.createPlan(context);
        TopicAndThread topicAndThread = getCurrentlyActiveThread(context);

        ThreadConversation conversation = addUserInputToConversation(input, topicAndThread);

        analyzer.analyzeUserInput(topicAndThread, conversation, input);

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
                .call()
                .content();

        addAssistantOutputToConversation(topicAndThread, output);

        return output;
    }

    private void addAssistantOutputToConversation(TopicAndThread topicAndThread, String output) {
        String role = "assistant";
        addToConversation(topicAndThread, output, role);
    }

    private ThreadConversation addUserInputToConversation(String input, TopicAndThread topicAndThread) {
        String role = "user";
        return addToConversation(topicAndThread, input, role);
    }

    private ThreadConversation addToConversation(TopicAndThread topicAndThread, String output, String role) {
        return interviewPlanner.addToThreadConversation(topicAndThread.getThread().getTopicId(), ConversationEntry.builder()
                .timestamp(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX").withZone(ZoneId.of("UTC")).format(Instant.now()))
                .role(role)
                .text(output)
                .build());
    }

    private TopicAndThread getCurrentlyActiveThread(InterviewContext context) {
        return interviewPlanner.findCurrentTopicAndThread(context.getCareerDataId());
    }

}
