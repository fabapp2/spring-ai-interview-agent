package io.promptics.jobagent.interview;

import io.promptics.jobagent.interviewplan.TopicAndThread;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.stereotype.Component;

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
            """;

    private final ThreadConversationRenderer conversationRenderer;
    private final TopicAndThreadRenderer topicAndThreadRenderer;
    private final ChatClient chatClient;

    public ConversationAnalyzer(ChatClient.Builder builder, ThreadConversationRenderer conversationRenderer, TopicAndThreadRenderer topicAndThreadRenderer) {
        chatClient = builder.defaultOptions(ChatOptions.builder().temperature(0.0).build()).build();
        this.conversationRenderer = conversationRenderer;
        this.topicAndThreadRenderer = topicAndThreadRenderer;
    }

    public String analyzeUserInput(TopicAndThread topicAndThread, ThreadConversation conversation, String input) {
        String prompt = renderPrompt(topicAndThread, conversation);
        System.out.println(prompt);
        return chatClient.prompt()
                .system(prompt)
                .user(input)
                .call()
                .content();
    }

    private String renderPrompt(TopicAndThread topicAndThread, ThreadConversation conversation) {
        String renderedTopicAndThread = topicAndThreadRenderer.renderTopicAndThread(topicAndThread.getTopic(), topicAndThread.getThread());
        String renderedConversation = conversationRenderer.renderConversation(conversation);
        return new PromptTemplate(PROMPT_TEMPLATE, Map.of(
                "threadConversation", renderedConversation,
                "topicAndThread", renderedTopicAndThread
        )).render();
    }

}
