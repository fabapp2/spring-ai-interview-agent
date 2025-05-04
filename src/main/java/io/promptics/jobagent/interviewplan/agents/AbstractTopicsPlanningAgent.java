package io.promptics.jobagent.interviewplan.agents;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.promptics.jobagent.interviewplan.model.Topic;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;

import java.util.List;

@Slf4j
public abstract class AbstractTopicsPlanningAgent<S>  extends AbstractGeneralPlanningAgent<S, Topic> {

    protected AbstractTopicsPlanningAgent(ChatClient chatClient, ObjectMapper objectMapper) {
        super(chatClient, objectMapper);
    }

    protected AbstractTopicsPlanningAgent(ChatClient.Builder builder, ObjectMapper objectMapper) {
        super(builder, objectMapper);
    }

    public abstract List<Topic> planTopics(String careerDataId, S works);
}
