package io.promptics.jobagent.interview;

import io.promptics.jobagent.InterviewContext;
import io.promptics.jobagent.interviewplan.ConversationEntry;
import io.promptics.jobagent.interviewplan.InterviewPlanMongoDbService;
import io.promptics.jobagent.interviewplan.InterviewPlanMongoDbTools;
import io.promptics.jobagent.interviewplan.TopicAndThread;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class Interviewer {

    private Map<String, String> memory = new HashMap<>();

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
    private final InterviewPlanMongoDbTools mongoDbTools;
    private final InterviewPlanMongoDbService mongoDbService;

    public Interviewer(ChatClient.Builder builder, InterviewPlanMongoDbTools mongoDbTools, InterviewPlanMongoDbService mongoDbService) {
        client = builder.defaultOptions(ChatOptions.builder().temperature(0.0).build()).build();
        this.mongoDbTools = mongoDbTools;
        this.mongoDbService = mongoDbService;
    }

    public String run(InterviewContext context, String input) {

        TopicAndThread topicAndThread = mongoDbService.findCurrentTopicAndThread(context.getCareerDataId());

        mongoDbService.addToThreadConversation(topicAndThread.getThreadId(), ConversationEntry.builder()
                        .role("user")
                        .text(input)
                        .build());

        String topicThreadSummary = topicAndThread.render();

        String prompt = systemPrompt
                .replace("{memory}", renderMemory(memory))
                .replace("{current_thread}", topicThreadSummary);

        String output = client.prompt()
                .system(prompt)
                .user(input)
                .tools(mongoDbTools)
                .call()
                .content();
        memory.put("assistant", output);
        return output;
    }

    private String renderMemory(Map<String, String> memory) {
        return memory.entrySet().stream()
                .map(e -> e.getKey() + ": " + e.getValue())
                .collect(Collectors.joining("\n\n"));
    }

    public Map<String, String> getMemory() {
        return memory;
    }
}
