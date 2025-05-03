package io.promptics.jobagent.interviewplan.agents;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.networknt.schema.*;
import io.promptics.jobagent.careerdata.model.Basics;
import io.promptics.jobagent.interviewplan.model.Topic;
import io.promptics.jobagent.interviewplan.model.TopicThread;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.util.List;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractThreadsPlanningAgent<S> {

    private final ObjectMapper objectMapper;

    public abstract List<TopicThread> planThreads(S sectionData, List<Topic> topics);

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

    public List<TopicThread> planThreads(Basics sectionData, List<Topic> topics) {
        String topicsJson = serialize(topics);
        String basicsSectionJson = serialize(sectionData);
        String response = promptLlm(basicsSectionJson, topicsJson);

        Set<ValidationMessage> validationMessages = validateJson(response, getJsonSchema());

        if (!validationMessages.isEmpty()) {
            throw new IllegalStateException("Generated JSON %s does not match for schema %s".formatted(response, getJsonSchema()));
        }

        List<TopicThread> threads = deserialize(response, new TypeReference<>() {});
        return threads;
    }

    private String getJsonSchema() {
        return "/schemas/plan/threads-array-schema.json";
    }

    @Nullable
    protected abstract String promptLlm(String basicsSectionJson, String topicsJson);
}
