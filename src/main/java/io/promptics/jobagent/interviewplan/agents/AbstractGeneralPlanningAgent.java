package io.promptics.jobagent.interviewplan.agents;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.networknt.schema.*;
import io.promptics.jobagent.utils.JsonValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractGeneralPlanningAgent<S> {

    private final ObjectMapper objectMapper;

    protected String serialize(Object basicsSection, boolean prettyPrint) {
        ObjectWriter objectWriter = objectMapper.writer();
        if(prettyPrint) {
            objectWriter = objectMapper.writerWithDefaultPrettyPrinter();
        }
        try {
            return objectWriter.writeValueAsString(basicsSection);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    protected String serialize(Object basicsSection) {
        if (log.isDebugEnabled()) {
            return serialize(basicsSection, true);
        } else {
            return serialize(basicsSection, false);
        }
    }

    protected <S> List<S> deserialize(String response, TypeReference<List<S>> typeReference) {
        try {
            return objectMapper.readValue(response, typeReference);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    protected Set<ValidationMessage> validateJson(List<S> response, String jsonSchema) {
        try {
            String json = objectMapper.writeValueAsString(response);
            return validateJson(json, jsonSchema);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    protected Set<ValidationMessage> validateJson(String json, String jsonSchema) {
        return JsonValidator.validateJson(json, jsonSchema);
    }
}
