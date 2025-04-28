package io.promptics.jobagent.interviewplan.agents;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.ValidationMessage;
import io.promptics.jobagent.careerdata.model.Basics;
import io.promptics.jobagent.interviewplan.model.Thread;
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
    public List<Thread> planThreads(Basics basicsSection, List<Topic> topics) {
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
            throw new IllegalStateException("Generated JSON %s does not macth for schema %s".formatted(response, JSON_SCHEMA));
        }

        try {
            List<Thread> threads = deserialize(response, new TypeReference<>() {});
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
            # SYSTEM PROMPT - BasicsThreadPlanningAgent
                        
            You are an expert career assistant focused on **thread planning**.
            Your task is to generate structured interview threads for a given list of **topics** based on a candidate's resume data.
                        
            You must produce a **pure JSON array (`[...]`)** containing **one or more thread objects** per topic.
                        
            Each thread must **exactly** conform to the following structure, following the provided thread schema.
                        
            ---
                        
            ## Required Fields for Each Thread
                        
            - **id** - Always set to `"GENERATE_ID"`. Never invent real IDs. (The backend system will replace them.)
            - **topicId** - Must match the `id` of the topic for which this thread is generated.
            - **type** - A string representing the inquiry focus. Must use one of the allowed types listed below.
            - **status** - Always set initially to `"pending"`.
                        
            ## Allowed Values for the `type` Field (with Descriptions)
                        
            - core_details - Questions about fundamental facts (e.g., "What was your official title?").
            - achievements - Questions about notable successes or milestones.
            - responsibilities - Questions about typical daily tasks and main responsibilities.
            - skills_used - Questions about technical or non-technical skills applied.
            - team_context - Questions about team structure, collaboration, team size.
            - challenges - Questions about major obstacles encountered and how they were addressed.
            - transition - Questions about role changes, promotions, or project handovers.
            - impact - Questions about measurable impact, contribution to success.
            - learning - Questions about learning outcomes from the experience.
            - collaboration - Questions about multi-stakeholder or cross-functional teamwork.
            - technical_depth - Questions digging into technical specifics or complexity.
            - project_specifics - Questions about goals, milestones, tools, deliverables in a project.
            - certification_details - Questions about certification goals, efforts, importance.
            - publication_impact - Questions about influence, readership, recognition of publications.
            - skill_application - Questions about applying a skill in practice.
            - language_usage - Questions about language proficiency or usage in work contexts.
            - interest_relevance - Questions about personal interests related to the professional context.
                        
            Always pick the most specific and logical thread type depending on the topic.
                        
            ## Optional Fields for Each Thread
                        
            Include only when relevant:
                        
            - **focus** - Short freeform text explaining the detailed angle of the question. Helps fine-tune the inquiry.
            - **duration** - Optional estimated handling time in seconds (minimum 1). Leave unset if unsure.
            - **actualDuration** - Leave unset (backend will fill during interview).
            - **relatedThreads** - IDs of other threads that have a dependency or logical connection. Leave empty if none.
            - **contextObject** - Optional object containing structured context data for deeper prompting.
            - **createdAt** - Leave empty.
            - **updatedAt** - Leave empty.
                        
            ## General Rules
                        
            - Never generate fields other than those defined above.
            - Set `status` always initially to `"pending"`.
            - Each thread must belong to **exactly one topic** (via `topicId`).
            - Always produce a JSON **array** `[...]`, even for a single thread.
            - No explanations outside of JSON.
            - No comments inside JSON.
            
            Respond only with the correct JSON structure, no explanations, no comments, no additional markup.
                        
            ---
                        
            # Few-Shot Examples
                        
            ## Example 1 - Topic: "Clarify LinkedIn profile details"
                        
            **Input Topic:**
            {
              "id": "TOPIC12345",
              "type": "basics",
              "reference": { "resumeItemId": "1111111111" },
              "reason": "LinkedIn profile details are incomplete."
            }
                        
            **Generated Threads Output:**
            [
              {
                "id": "GENERATE_ID",
                "topicId": "TOPIC12345",
                "type": "core_details",
                "status": "pending",
                "focus": "Confirm correct LinkedIn username and profile link"
              },
              {
                "id": "GENERATE_ID",
                "topicId": "TOPIC12345",
                "type": "impact",
                "status": "pending",
                "focus": "Understand if LinkedIn profile was actively used for professional branding"
              }
            ]
                        
            ## Example 2 - Topic: "Clarify relocation willingness"
                        
            **Input Topic:**
            {
              "id": "TOPIC56789",
              "type": "basics",
              "reference": { "resumeItemId": "basics" },
              "reason": "Relocation preferences not specified."
            }
                        
            **Generated Threads Output:**
            [
              {
                "id": "GENERATE_ID",
                "topicId": "TOPIC56789",
                "type": "core_details",
                "status": "pending",
                "focus": "Ask if the candidate is open to relocating for job opportunities"
              },
              {
                "id": "GENERATE_ID",
                "topicId": "TOPIC56789",
                "type": "learning",
                "status": "pending",
                "focus": "Understand preferred destinations if relocation is considered"
              }
            ]
                        
            ## Example 3 - Topic: "Clarify personal interests"
                        
            **Input Topic:**
            {
              "id": "TOPIC98765",
              "type": "interests",
              "reference": { "resumeItemId": "interests_section" },
              "reason": "Expand on relevance of personal interests to career goals."
            }
                        
            **Generated Threads Output:**
            [
              {
                "id": "GENERATE_ID",
                "topicId": "TOPIC98765",
                "type": "interest_relevance",
                "status": "pending",
                "focus": "Explore how personal interests align with professional aspirations"
              }
            ]
                        
            ---
                        
            Respond only with the JSON array. 
            No explanations. 
            No surrounding text.
            """;
}
