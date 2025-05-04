package io.promptics.jobagent.interviewplan.agents;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.networknt.schema.*;
import io.promptics.jobagent.interviewplan.model.TopicThread;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.util.List;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractGeneralPlanningAgent<S> {

    private final ObjectMapper objectMapper;

    protected String serialize(Object basicsSection) {
        ObjectWriter objectWriter = objectMapper.writer();
        if (log.isDebugEnabled()) {
            objectWriter = objectMapper.writerWithDefaultPrettyPrinter();
        }
        try {
            return objectWriter.writeValueAsString(basicsSection);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
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
        try {
            ClassPathResource resource = new ClassPathResource(jsonSchema);
            JsonSchemaFactory schemaFactory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V7);
            JsonSchema schema = schemaFactory.getSchema(resource.getURI());
            Set<ValidationMessage> messages = schema.validate(json, InputFormat.JSON);
            return messages;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
