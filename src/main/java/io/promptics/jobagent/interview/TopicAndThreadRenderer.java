package io.promptics.jobagent.interview;

import io.promptics.jobagent.interviewplan.model.Topic;
import io.promptics.jobagent.interviewplan.model.TopicThread;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Render current TopicAndThread asx textual representation to be used in prompts.
 */
@Component
@RequiredArgsConstructor
public class TopicAndThreadRenderer {

    private final ThreadTypeDescriptionMapper descriptionMapper;

    private static final String PROMPT_TEMPLATE = """
        The current topic covers "{topicType}" in the "{section}" of career data with id "{resumeItemId}".
        The current thread in this topic is: "{threadFocus}" and handles "{threadType}".
        
        {typeDescription}
        """;

    public String renderTopicAndThread(Topic topic, TopicThread thread) {

        String typeDescription = descriptionMapper.mapType(thread.getType().value());

        Map<String, Object> variables = new HashMap<>();
        variables.put("section", topic.getType().value());
        variables.put("topicType", topic.getReason());
        variables.put("threadType", thread.getType().value());
        variables.put("resumeItemId", topic.getReference().getResumeItemId());
        variables.put("threadFocus", thread.getFocus());
        variables.put("typeDescription", typeDescription);

        return new PromptTemplate(PROMPT_TEMPLATE, variables).render();
    }


}