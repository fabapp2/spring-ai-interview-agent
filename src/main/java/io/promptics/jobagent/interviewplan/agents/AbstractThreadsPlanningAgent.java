package io.promptics.jobagent.interviewplan.agents;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.ValidationMessage;
import io.promptics.jobagent.interviewplan.model.Topic;
import io.promptics.jobagent.interviewplan.model.TopicThread;
import lombok.extern.slf4j.Slf4j;
import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.NotNull;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.template.st.StTemplateRenderer;
import org.springframework.core.ParameterizedTypeReference;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
public abstract class AbstractThreadsPlanningAgent<S> extends AbstractGeneralPlanningAgent<S, TopicThread> {

    private static final String USER_PROMPT_TMPL = """
            Given Section:
            <section>
                        
            Given Topics:
            <topics>
            """;

    public AbstractThreadsPlanningAgent(ChatClient.Builder builder, ObjectMapper objectMapper) {
        super(builder, objectMapper);
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


    protected @NotNull String getUserPromptTemplate() {
        return USER_PROMPT_TMPL;
    }

    private String getJsonSchema() {
        return "/schemas/plan/threads-array-schema.json";
    }

    protected List<TopicThread> promptLlm(S sectionData, List<Topic> topics) {
        String basicsSectionText = serialize(sectionData, true);
        String topicsJson = serialize(topics, true);

        String userPrompt = PromptTemplate.builder().template(getUserPromptTemplate()).renderer(TEMPLATE_RENDERER).build().render(Map.of(
                        "section", basicsSectionText,
                        "topics", topicsJson
                )
        );
        List<TopicThread> response = chatClient.prompt()
                .system(getSystemPrompt())
                .user(userPrompt)
                .call()
                .entity(new ParameterizedTypeReference<>() {});

        return response;
    }
    protected abstract @Language("markdown") @NotNull String getSystemPrompt();

}
