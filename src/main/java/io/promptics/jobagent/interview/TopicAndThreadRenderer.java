package io.promptics.jobagent.interview;

import io.promptics.jobagent.interviewplan.Thread;
import io.promptics.jobagent.interviewplan.Topic;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class TopicAndThreadRenderer {

    private static final String PROMPT_TEMPLATE = """
        {topicSection}
        
        {threadSection}
        """;

    private static final String TOPIC_SECTION_TEMPLATE = """
        # Current Topic: {topicName}
        
        {topicTypeDescription
        }
        
        ## Reference Information
        - Section: {section}
        - Organization: {organization}
        - Start Date: {startDate}
        """;

    private static final String THREAD_SECTION_TEMPLATE = """
        # Current Thread: {threadName}
        
        {threadTypeDescription}
        
        ## Focus Area
        {focus}
        
        ## Thread Parameters
        - Allocated Time: {duration} minutes
        - Current Status: {status}
        """;

    private final TopicTypeDescriptionMapper topicMapper;
    private final ThreadTypeDescriptionMapper threadMapper;

    public String renderTopicAndThread(Topic topic, Thread thread) {

        String topicSection = buildTopicSection(topic);
        String threadSection = buildThreadSection(thread);

        Map<String, Object> variables = new HashMap<>();
        variables.put("topicSection", topicSection);
        variables.put("threadSection", threadSection);
        variables.put("threadName", thread.getIdentifier());
        variables.put("threadFocus", thread.getFocus());
        variables.put("topicType", topic.getType().value());

        variables.put("organization", topic.getReference().getIdentifier().getName());

        return new PromptTemplate(PROMPT_TEMPLATE, variables).render();
    }

    private String buildTopicSection(Topic topic) {
        String typeDescription = topicMapper.mapType(topic.getType().value());

        Map<String, Object> variables = new HashMap<>();
        variables.put("topicName", topic.getIdentifier());
        variables.put("topicTypeName", topic.getType().value());
        variables.put("topicTypeDescription", typeDescription);
        variables.put("section", topic.getReference().getSection());
        variables.put("organization", topic.getReference().getIdentifier().getName());
        variables.put("startDate", topic.getReference().getIdentifier().getStartDate());

        return new PromptTemplate(TOPIC_SECTION_TEMPLATE, variables).render();
    }

    private String buildThreadSection(Thread thread) {
        String typeDescription = threadMapper.mapType(thread.getType().value());

        Map<String, Object> variables = new HashMap<>();
        variables.put("threadName", thread.getIdentifier());
        variables.put("threadTypeName", thread.getType().value());
        variables.put("threadTypeDescription", typeDescription);
        variables.put("focus", thread.getFocus());
        variables.put("duration", String.valueOf(thread.getDuration()));
        variables.put("status", thread.getStatus());

        return new PromptTemplate(THREAD_SECTION_TEMPLATE, variables).render();
    }

}