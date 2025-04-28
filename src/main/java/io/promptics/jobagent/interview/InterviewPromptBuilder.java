package io.promptics.jobagent.interview;

import io.promptics.jobagent.interviewplan.model.Topic;
import io.promptics.jobagent.interviewplan.model.TopicThread;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class InterviewPromptBuilder {
    private static final String PROMPT_TEMPLATE = """
        # Interview Context
        
        {topicAndThread}
        
        ## Interviewer Guidelines
        As an interviewer, focus on:
        1. Understanding the specific ${topicType} context
        2. Gathering concrete details about ${threadFocus}
        3. Exploring the timeline and progression
        4. Understanding decisions and outcomes
        5. Identifying key learnings and future outlook
        
        Your questions should:
        - Be specific and focused on the current context
        - Follow up on important details
        - Maintain professional and empathetic tone
        - Seek concrete examples and timelines
        - Allow for detailed explanations
        
        ## Conversation in thread
        {conversation}
        
        ## Additional Context
        {previousContext}
        
        ## Next Question
        Based on this context, particularly focusing on {threadFocus}, please ask your next interview question.
        Remember to consider the {topicType} context and maintain a professional, understanding tone.
        
        """;

    private final ThreadConversationRenderer conversationRenderer;
    private final TopicAndThreadRenderer topicAndThreadRenderer;

    public InterviewPromptBuilder(ThreadConversationRenderer conversationRenderer, TopicAndThreadRenderer topicAndThreadRenderer) {
        this.conversationRenderer = conversationRenderer;
        this.topicAndThreadRenderer = topicAndThreadRenderer;
    }

    public String buildPrompt(Topic topic, TopicThread thread, ThreadConversation conversation, String additionalContext) {

        String topicAndThread = topicAndThreadRenderer.renderTopicAndThread(topic, thread);
        String renderedConversation = conversationRenderer.renderConversation(conversation);

        Map<String, Object> variables = new HashMap<>();
        variables.put("previousContext", additionalContext);
        variables.put("conversation", renderedConversation);
        variables.put("topicAndThread", topicAndThread);
        variables.put("threadFocus", thread.getFocus());
        variables.put("topicType", topic.getType().value());

        return new PromptTemplate(PROMPT_TEMPLATE, variables).render();
    }


}
