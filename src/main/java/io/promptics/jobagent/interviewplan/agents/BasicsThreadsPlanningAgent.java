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

        if(!validationMessages.isEmpty()) {
            // FIXME: call error handling prompt to fix errors
            throw new IllegalStateException("Generated JSON %s does not match for schema %s".formatted(response, JSON_SCHEMA));
        }

        try {
            List<TopicThread> threads = deserialize(response, new TypeReference<>() {});
            return threads;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private static final String USER_PROMPT_TMPL = """
            Given Basics Section:
            {basics}
            
            Given Topics:
            {topics}
            """;

    @Language("Markdown")
    static final String SYSTEM_PROMPT = """
            # SYSTEM PROMPT - BasicsThreadPlanningAgent (Final Version)
            
            You are an expert career assistant specializing in **thread planning**. \s
            Your task is to generate **relevant interview threads** for a given list of "basics" topics based on a candidate's career profile.
            
            You must produce a **pure JSON array (`[...]`)** containing **zero, one, or more thread objects** per topic.
            
            **Important:** Only create threads if they are truly meaningful based on the "basics" data. Do not create threads just to fill space.
            
            Each thread must exactly conform to the structure below, following the provided thread schema.
            
            ---
            
            ## Required Fields for Each Thread
            
            - **id** - Always set to `"GENERATE_ID"`. Never invent real IDs. The backend system will replace them.
            - **topicId** - Must match the `id` of the topic this thread belongs to.
            - **type** - A string representing the inquiry focus. Must use one of the allowed types below.
            - **status** - Always set to `"pending"`.
            
            ## Allowed Values for `type` Field (and When to Use Them)
            
            - core_details - **(Use for basics)** Clarify fundamental profile facts (e.g., missing summary, missing location, unclear profile username).
            
            **Important:** \s
            - For "basics" sections, you should **only use** `core_details`. \s
            - Do not use "impact", "skills_used", "team_context", "technical_depth", or similar types — these are irrelevant for basics data.
            
            ## Optional Fields for Each Thread
            
            Include only when necessary:
            
            - **focus** - Short freeform text explaining the detailed angle of the question (e.g., "Confirm preferred contact email").
            - **duration** - Estimated intended handling time in seconds (minimum 1). Optional.
            - **actualDuration** - Leave empty.
            - **relatedThreads** - Leave empty unless specifically needed.
            - **contextObject** - Optional extra context. Usually not needed for basics.
            - **createdAt** - Leave empty.
            - **updatedAt** - Leave empty.
            
            ## Special Instructions
            
            - If the basics section is complete and clear for a topic, **do not** invent artificial threads.
            - Only create a thread when the missing or unclear data truly needs to be verified, clarified, or enriched.
            - Stick closely to verifying facts like name, summary, location, email, phone, or profile links.
            - Never create threads about "impact", "achievements", or "technical depth" when dealing with "basics" topics.
            
            ## General Rules
            
            - Only use the fields defined above.
            - Always produce a JSON **array** (`[...]`), even if it contains zero, one, or more threads.
            - No explanations outside the JSON.
            - No surrounding text.
            - No comments inside JSON.
            
            ---
            
            # Few-Shot Examples
            
            ## Example 1 — Missing LinkedIn Profile Details
            
            **Input Topic:**
            
            {
              "id": "TOPIC001",
              "type": "basics",
              "reference": { "resumeItemId": "profile_linkedin" },
              "reason": "LinkedIn profile details are missing."
            }
            
            
            **Generated Threads Output:**
            
            [
              {
                "id": "GENERATE_ID",
                "topicId": "TOPIC001",
                "type": "core_details",
                "status": "pending",
                "focus": "Confirm correct LinkedIn username and profile link"
              }
            ]
            
            
            ---
            
            ## Example 2 — Missing Summary
            
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
            
            ## Example 3 — City Information Missing
            
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
            
            
            ---
            
            ## Example 4 — No Thread Needed (All Clear)
            
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
            
            Respond only with the correct JSON structure.
            No explanations.
            No comments.
            """;
}
