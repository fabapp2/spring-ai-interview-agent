package io.promptics.jobagent.interviewplan.agents;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.networknt.schema.*;
import io.promptics.jobagent.utils.JsonValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.template.st.StTemplateRenderer;

import java.util.List;
import java.util.Set;

@Slf4j
public abstract class AbstractGeneralPlanningAgent<S, T> {

    private final ObjectMapper objectMapper;
    protected ChatClient chatClient;

    public static final StTemplateRenderer TEMPLATE_RENDERER = StTemplateRenderer.builder()
            .startDelimiterToken('<')
            .endDelimiterToken('>')
            .build();

    protected AbstractGeneralPlanningAgent(ChatClient chatClient, ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.chatClient = chatClient;
    }

    protected AbstractGeneralPlanningAgent(ChatClient.Builder builder, ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        ChatOptions chatOptions = ChatOptions.builder()
                .temperature(getTemperature())
                .model(getModel())
                .build();
        this.chatClient = builder
                .defaultTemplateRenderer(
                        TEMPLATE_RENDERER
                ).defaultOptions(chatOptions).build();
    }

    protected abstract String getModel();

    protected abstract Double getTemperature();

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

    protected <S> S deserialize(String response, TypeReference<S> typeReference) {
        try {
            return objectMapper.readValue(response, typeReference);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    protected Set<ValidationMessage> validateJson(List<T> response, String jsonSchema) {
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
