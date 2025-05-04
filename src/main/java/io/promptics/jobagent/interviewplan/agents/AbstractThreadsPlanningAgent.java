package io.promptics.jobagent.interviewplan.agents;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.ValidationMessage;
import io.promptics.jobagent.careerdata.model.Basics;
import io.promptics.jobagent.interviewplan.model.Topic;
import io.promptics.jobagent.interviewplan.model.TopicThread;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.util.List;
import java.util.Set;

@Slf4j
public abstract class AbstractThreadsPlanningAgent<S> extends AbstractGeneralPlanningAgent<TopicThread> {

    public AbstractThreadsPlanningAgent(ObjectMapper objectMapper) {
        super(objectMapper);
    }

    public List<TopicThread> planThreads(String careerDataId, S sectionData, List<Topic> topics) {
        List<TopicThread> response = promptLlm(sectionData, topics);
        response.forEach(thread -> {
            Instant now = Instant.now();
            thread.setCreatedAt(now);
            thread.setUpdatedAt(now);
            thread.setCareerDataId(careerDataId);
        });

        Set<ValidationMessage> validationMessages = validateJson(response, getJsonSchema());

        if (!validationMessages.isEmpty()) {
            throw new IllegalStateException("Generated JSON %s does not match for schema %s. Messages: %s".formatted(response, getJsonSchema(), validationMessages));
        }

        return response;
    }

    private String getJsonSchema() {
        return "/schemas/plan/threads-array-schema.json";
    }

    protected abstract List<TopicThread> promptLlm(S basicsSectionJson, List<Topic> topicsJson);
}
