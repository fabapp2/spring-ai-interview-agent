package io.promptics.jobagent.interviewplan.agents;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.networknt.schema.*;
import io.promptics.jobagent.interviewplan.model.Topic;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.util.List;
import java.util.Set;

@Slf4j
public abstract class AbstractTopicsPlanningAgent<S>  extends AbstractGeneralPlanningAgent<S> {


    protected AbstractTopicsPlanningAgent(ObjectMapper objectMapper) {
        super(objectMapper);
    }

    public abstract List<Topic> planTopics(String careerDataId, S works);
}
