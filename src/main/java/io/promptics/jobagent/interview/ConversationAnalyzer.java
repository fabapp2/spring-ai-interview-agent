package io.promptics.jobagent.interview;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.promptics.jobagent.interviewplan.TopicAndThread;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

@Component
public class ConversationAnalyzer {

    public static final String PROMPT_TEMPLATE = """
            Analyze the current thread and the last reply.
            Extract all information from the user reply.
            Decide if the current thread is complete or if new follow-up topics or threads should be created.
            
            ## Thread related conversation
            {threadConversation}
            
            ## Current topic and thread
            {topicAndThread}
            
            Update the interview plan:
            - Update the information in the current thread
            - Add new topics and threads when required.
            - Topics and threads are sorted by their priority, add new entries accordingly. Higher in the list means higher priority.
            - Update the state of the current topic and thread when required
                - When a current thread is finished, set the status to "complete"
                - If another thread exists in the topic with statzus "pending", set its status to "in_progress"
                - If no thread with status "pending" exists in the current topic, set the first thread in the next topic to "in_progress"
                - If no topic exists verify that all threads have status "complete", otherwise pick the topmost thread with status "pending" and set the state to "in_progress"
            
            The interview plan to update:
            
            {interviewPlan}
            
            Expected response:
            
            Return the updated interview plan as JSON.
            """;

    private final ThreadConversationRenderer conversationRenderer;
    private final TopicAndThreadRenderer topicAndThreadRenderer;
    private final ChatClient chatClient;

    public ConversationAnalyzer(ChatClient.Builder builder, ThreadConversationRenderer conversationRenderer, TopicAndThreadRenderer topicAndThreadRenderer) {
        chatClient = builder.defaultOptions(ChatOptions.builder().model("gpt-4").temperature(0.0).build()).build();
        this.conversationRenderer = conversationRenderer;
        this.topicAndThreadRenderer = topicAndThreadRenderer;
    }

    public String analyzeUserInput(TopicAndThread topicAndThread, ThreadConversation conversation, String input) {
        String prompt = renderPrompt(topicAndThread, conversation);
        return chatClient.prompt()
                .system(prompt)
                .user(input)
                .call()
                .content();
    }

    private String renderPrompt(TopicAndThread topicAndThread, ThreadConversation conversation) {
        String renderedTopicAndThread = topicAndThreadRenderer.renderTopicAndThread(topicAndThread.getTopic(), topicAndThread.getThread());
        String renderedConversation = conversationRenderer.renderConversation(conversation);

        String plan = "";
        try {
            plan = new String(new ClassPathResource("interview-plan.json").getInputStream().readAllBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return new PromptTemplate(PROMPT_TEMPLATE, Map.of(
                "threadConversation", renderedConversation,
                "topicAndThread", renderedTopicAndThread,
                "interviewPlan", plan
        )).render();
    }

}
