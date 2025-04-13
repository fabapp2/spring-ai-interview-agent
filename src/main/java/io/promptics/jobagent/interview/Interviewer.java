package io.promptics.jobagent.interview;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
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
            
            
            {topic_thread}
          
            
            # Expected Output:
            Ask one question at a time.
            """;

    private final ChatClient client;

    public Interviewer(ChatClient.Builder builder) {
        client = builder.defaultOptions(ChatOptions.builder().temperature(0.0).build()).build();
    }

    public String run(String input) {
        memory.put("user", input);
        String topicThreadSummary = createTopicThreadSummary();
        String prompt = systemPrompt
                .replace("{memory}", renderMemory(memory))
                .replace("{topic_thread}", topicThreadSummary);
        String output = client.prompt()
                .system(prompt)
                .user(input)
                .call()
                .content();
        memory.put("assistant", output);
        return output;
    }

    private String createTopicThreadSummary() {
        return """
            # Current Topic: Employment Gap
            Type: gap
            Focus: Current employment status
            
            ## Reference
            - Company: TechGiant
            - Last Position: Senior Developer
            - Period: January 2024 - December 2024
            
            ## Active Thread
            ID: current_status
            Type: core_details
            Focus: Determine current employment status and activities since December 2024
            
            ## Relevant Context
            Last Known Position:
            - Company: TechGiant
            - Role: Senior Developer
            - Key Achievements:
              * Led a team of developers
              * Architected scalable solutions
              * Reduced system downtime by 30%
            
            Gap Period:
            - From: December 2024
            - To: Present (April 2024)
            - Duration: 4 months
            
            ## Previous Conversation
            {memory}
            
            ## Current Date
            2025-04-12
            """;
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
