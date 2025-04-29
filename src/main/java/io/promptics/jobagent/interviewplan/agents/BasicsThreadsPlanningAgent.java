package io.promptics.jobagent.interviewplan.agents;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.ValidationMessage;
import io.promptics.jobagent.careerdata.model.Basics;
import io.promptics.jobagent.interviewplan.model.TopicThread;
import io.promptics.jobagent.interviewplan.model.Topic;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.intellij.lang.annotations.Language;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class BasicsThreadsPlanningAgent extends AbstractPlanningAgent {

    private static final Double TEMPERATURE = 0.0;
    public static final String MODEL = "gpt-4o-mini";
    private static final String JSON_SCHEMA = "/schemas/plan/threads-array-schema.json";
    private final ChatClient chatClient;

    public BasicsThreadsPlanningAgent(ChatClient.Builder builder, ObjectMapper objectMapper) {
        super(objectMapper);
        ChatOptions chatOptions = ChatOptions.builder()
                .temperature(TEMPERATURE)
                .model(MODEL)
                .build();
        this.chatClient = builder.defaultOptions(chatOptions).build();
    }

    public List<TopicThread> planThreads(Basics basicsSection, List<Topic> topics) {
        String topicsJson = serialize(topics);
        String basicsSectionJson = serialize(basicsSection);
        String userPrompt = new PromptTemplate(USER_PROMPT_TMPL).render(Map.of(
                        "basics", basicsSectionJson,
                        "topics", topicsJson
                )
        );
        String response = chatClient.prompt()
                .system(SYSTEM_PROMPT)
                .user(userPrompt)
                .call()
                .content();

        Set<ValidationMessage> validationMessages = validateJson(response, JSON_SCHEMA);

        if (!validationMessages.isEmpty()) {
            throw new IllegalStateException("Generated JSON %s does not match for schema %s".formatted(response, JSON_SCHEMA));
        }

        List<TopicThread> threads = deserialize(response, new TypeReference<>() {});
        return threads;
    }

    private static final String USER_PROMPT_TMPL = """
            Given Basics Section:
            {basics}
                        
            Given Topics:
            {topics}
            """;

    @Language("Markdown")
    static final String SYSTEM_PROMPT = """
            You are an expert career assistant specializing in planning interview questions to improve career data.
                        
            Your task is to generate relevant interview threads for a given list of topics about the candidate's "basics" section.
                        
            Each topic may produce zero, one, or more thread objects. Only create threads when information is missing, unclear, or needs enrichment.
                        
            ---
                        
            ## Output Format
                        
            - Produce a pure JSON array `[...]` per topic.
            - Each array contains thread objects.
            - No surrounding text, no comments, no explanations.
                        
            ---
                        
            ## Thread Structure
                        
            Required fields:
            - **id**: Always `"GENERATE_ID"`.
            - **topicId**: The `id` of the related topic.
            - **type**: Always `"core_details"` for basics.
            - **status**: Always `"pending"`.
                        
            Optional fields (only when needed):
            - **focus**: Short description of the question's focus (e.g., "Confirm preferred contact email").
            - **duration**: Estimated handling time in seconds (minimum 20 seconds if specified).
            - **actualDuration**: Leave empty.
            - **relatedThreads**: Leave empty.
            - **contextObject**: Leave empty.
            - **createdAt**: Leave empty.
            - **updatedAt**: Leave empty.
                        
            ---
                        
            ## Allowed `type` Values
                        
            - **core_details**: Clarify basic facts like name, summary, location, email, phone, or profiles.
                        
            Only `core_details` is permitted when working with basics.
                        
            ---
                        
            ## Rules
                        
            - If no clarification is needed for a topic, return an empty JSON array `[]`.
            - Never invent questions if the data is complete and clear.
            - Strictly verify basic profile information only.
            - No formatting outside JSON. Only the array and thread objects inside.
                        
            ---
                        
            # Few-Shot Examples
                        
            ## Example — Missing LinkedIn Username
                        
            **Input Topic:**
                        
            {
              "id": "TOPIC123",
              "type": "basics",
              "reference": { "resumeItemId": "profile_linkedin" },
              "reason": "LinkedIn profile details are incomplete."
            }
                        
                        
            **Generated Threads Output:**
                        
            [
              {
                "id": "GENERATE_ID",
                "topicId": "TOPIC123",
                "type": "core_details",
                "status": "pending",
                "focus": "Confirm correct LinkedIn username and profile link"
              }
            ]
                        
                        
            ---
                        
            ## Example — Missing Summary
                        
            **Input Topic:**
                        
            {
              "id": "TOPIC002",
              "type": "basics",
              "reference": { "resumeItemId": "basics" },
              "reason": "Professional summary missing."
            }
                        
                        
            **Generated Threads Output:**
                        
            [
              {
                "id": "GENERATE_ID",
                "topicId": "TOPIC002",
                "type": "core_details",
                "status": "pending",
                "focus": "Ask the candidate to provide a short professional summary"
              }
            ]
                        
                        
            ---
                        
            ## Example — City Information Missing
                        
            **Input Topic:**
                        
            {
              "id": "TOPIC003",
              "type": "basics",
              "reference": { "resumeItemId": "basics_location" },
              "reason": "City of residence missing."
            }
                        
                        
            **Generated Threads Output:**
                        
            [
              {
                "id": "GENERATE_ID",
                "topicId": "TOPIC003",
                "type": "core_details",
                "status": "pending",
                "focus": "Confirm candidate's current city of residence"
              }
            ]
                        
                        
            ## Example — No Thread Needed (All Clear)
                        
            **Input Topic:**
                        
            {
              "id": "TOPIC004",
              "type": "basics",
              "reference": { "resumeItemId": "basics" },
              "reason": "No issues detected with basics section."
            }
                        
                        
            **Generated Threads Output:**
                        
            []
                        
            No threads are generated because there is nothing unclear or missing.
                        
            ---
                        
            Respond only with the correct JSON structure. \s
            No explanations. \s
            No comments.
            """;
}
