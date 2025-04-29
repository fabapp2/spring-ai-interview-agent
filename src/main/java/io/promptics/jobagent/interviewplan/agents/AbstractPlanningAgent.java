package io.promptics.jobagent.interviewplan.agents;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.networknt.schema.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.util.List;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
public class AbstractPlanningAgent {

    private final ObjectMapper objectMapper;

    protected static Set<ValidationMessage> validateJson(String json, String jsonSchema) {
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

    protected <T> List<T> deserialize(String response, TypeReference<List<T>> typeReference) {
        try {
            return objectMapper.readValue(response, typeReference);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
