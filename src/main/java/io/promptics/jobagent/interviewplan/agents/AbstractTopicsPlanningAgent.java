package io.promptics.jobagent.interviewplan.agents;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.promptics.jobagent.interviewplan.model.Topic;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;

import java.util.List;

@Slf4j
public abstract class AbstractTopicsPlanningAgent<S, T>  extends AbstractGeneralPlanningAgent<S, T> {


    protected AbstractTopicsPlanningAgent(ChatClient.Builder builder, ObjectMapper objectMapper) {
        super(builder, objectMapper);
    }

    public abstract List<Topic> planTopics(String careerDataId, S works);
}
