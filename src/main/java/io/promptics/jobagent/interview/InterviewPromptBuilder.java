package io.promptics.jobagent.interview;

import io.promptics.jobagent.interviewplan.Thread;
import io.promptics.jobagent.interviewplan.Topic;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class InterviewPromptBuilder {
    private static final String PROMPT_TEMPLATE = """
        # Interview Context
        
        {topicSection}
        
        {threadSection}
        
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
        Based on this context, particularly focusing on ${threadFocus}, please ask your next interview question.
        Remember to consider the ${topicType} context and maintain a professional, understanding tone.
        """;

    private static final String TOPIC_SECTION_TEMPLATE = """
        # Current Topic: {topicName}
        
        Type: ${typeDescription}
        {typeDescription}
        
        ## Reference Information
        - Section: {section}
        - Organization: {organization}
        - Start Date: {startDate}
        """;

    private static final String THREAD_SECTION_TEMPLATE = """
        # Current Thread: ${threadName}
        
        ${typeDescription}
        
        ## Focus Area
        ${focus}
        
        ## Thread Parameters
        - Allocated Time: ${duration} minutes
        - Current Status: ${status}
        """;

    private final TopicTypeDescriptionMapper topicMapper;
    private final ThreadTypeDescriptionMapper threadMapper;

    public InterviewPromptBuilder(
            TopicTypeDescriptionMapper topicMapper,
            ThreadTypeDescriptionMapper threadMapper) {
        this.topicMapper = topicMapper;
        this.threadMapper = threadMapper;
    }

    public String buildPrompt(Topic topic, Thread thread, ThreadConversation conversation, String additionalContext) {
        String topicSection = buildTopicSection(topic);
        String threadSection = buildThreadSection(thread);

        Map<String, Object> variables = new HashMap<>();
        variables.put("topicSection", topicSection);
        variables.put("threadSection", threadSection);
        variables.put("previousContext", additionalContext);
        variables.put("threadName", thread.getIdentifier());
        variables.put("threadFocus", thread.getFocus());
        variables.put("topicType", topic.getType().value());
        variables.put("organization", topic.getReference().getIdentifier().getName());
        variables.put("conversation", renderConversation(conversation));

        return new PromptTemplate(PROMPT_TEMPLATE, variables).render();
    }

    private String renderConversation(ThreadConversation conversation) {
        return conversation.getEntries().stream()
                .map(e -> e.getRole() + ": " + e.getText() + "\n")
                .collect(Collectors.joining("\n"));
    }

    private String buildTopicSection(Topic topic) {
        String typeDescription = topicMapper.mapType(topic.getType().value());

        Map<String, Object> variables = new HashMap<>();
        variables.put("topicName", topic.getIdentifier());
        variables.put("typeDescription", typeDescription);
        variables.put("section", topic.getReference().getSection());
        variables.put("organization", topic.getReference().getIdentifier().getName());
        variables.put("startDate", topic.getReference().getIdentifier().getStartDate());

        return new PromptTemplate(TOPIC_SECTION_TEMPLATE, variables).render();
    }

    private String buildThreadSection(Thread thread) {
        String typeDescription = threadMapper.mapType(thread.getType().value());

        Map<String, Object> variables = new HashMap<>();
        variables.put("threadName", thread.getIdentifier());
        variables.put("typeDescription", typeDescription);
        variables.put("focus", thread.getFocus());
        variables.put("duration", String.valueOf(thread.getDuration()));
        variables.put("status", thread.getStatus());

        return new PromptTemplate(THREAD_SECTION_TEMPLATE, variables).render();
    }
}
