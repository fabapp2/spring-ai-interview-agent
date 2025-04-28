package io.promptics.jobagent.interviewplan.agents;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.*;
import io.promptics.jobagent.careerdata.model.Basics;
import io.promptics.jobagent.interviewplan.model.Topic;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.intellij.lang.annotations.Language;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@Component
public class BasicsTopicPlanningAgent extends AbstractPlanningAgent {

    public static final String MODEL = "gpt-4o-mini";
    public static final Double TEMPERATURE = 0.0;
    private static final String JSON_SCHEMA = "/schemas/plan/topics-array-schema.json";
    private final ChatClient chatClient;

    public BasicsTopicPlanningAgent(ChatClient.Builder builder, ObjectMapper objectMapper) {
        super(objectMapper);
        ChatOptions chatOptions = ChatOptions.builder()
                .model(MODEL)
                .temperature(TEMPERATURE)
                .build();

        chatClient = builder.defaultOptions(chatOptions).build();
    }

    public List<Topic> planTopics(Basics basicsSection) {
        String section = serialize(basicsSection);

        String response = chatClient.prompt()
                .system(SYSTEM_PROMPT)
                .user(new PromptTemplate(USER_PROMPT_TMPL).render(Map.of("basics", section)))
                .call()
                .content();

        Set<ValidationMessage> validationMessages = validateJson(response, JSON_SCHEMA);

        if(!validationMessages.isEmpty()) {
            // FIXME: call error handling prompt to fix errors
            throw new IllegalStateException("Generated JSON %s does not macth for schema %s".formatted(response, JSON_SCHEMA));
        }

        try {
            List<Topic> topics = deserialize(response, Topic.class);
            return topics;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private static final String USER_PROMPT_TMPL = """
            Given Basics:
            
            {basics}
            """;

    @Language("markdown")
    static final String SYSTEM_PROMPT = """
            You are an expert career assistant. Your task is to analyze the "basics" section of a candidate's career profile and generate a meaningful list of interview topics.
             Each topic must focus on clarifying, enriching, or verifying important information based on the provided Basics data.
             
            You must produce a pure JSON array ([...]) containing one or more topic objects.
            Each topic must exactly conform to the following structure:
            
            ## Required Fields for Each Topic:
                        
            - id - Always set to "GENERATE_ID". Do not invent real IDs. (The backend will replace it.)
            - type - A string defining the topic type. Use one of the allowed values listed below.
            
            Allowed Values for the type field (with explanations)
                        
                - basics - For gaps or uncertainties directly in the basics section (e.g., missing summary, unclear location, incomplete profiles).
                - work - Topics about missing or unclear work experience entries (e.g., missing company name, unclear responsibilities).
                - volunteer - Topics about missing or unclear volunteer work entries.
                - education - Topics about missing or unclear education details.
                - projects - Topics about missing or unclear major projects.
                - awards - Topics about missing or unclear award details.
                - certificates - Topics about missing or unclear certificates.
                - publications - Topics about missing or unclear publications.
                - skills - Topics about missing or unclear skills.
                - languages - Topics about missing or unclear language proficiency.
                - interests - Topics about missing or unclear personal interests.
                - references - Topics about missing or unclear professional references.
                - gap - Topics identifying missing time periods between resume activities.
                - career_transition - Topics about major career changes (e.g., industry switch).
                - freelance_period - Topics about periods of freelance/self-employment.
                - timeline_arc - Topics that span multiple years or activities in a major way.
                - role - Topics about specific roles or responsibilities that are unclear.
                - narrative - Topics for general career storytelling or motivation aspects.
            
            Always select the most specific and meaningful type based on what needs clarification.
            
            ### Optional Fields
            Include only when relevant:
                        
            - reference - An object describing the element linked to the topic. Use to specify which exact element in the provided Basics data the topic refers to. Linkage rules:
            - resumeItemId (string) - ID of a specific resume item (e.g., profile entry id inside basics.profiles) that this topic is about. Use the ID given in the input Basics section.
            - spans (array of strings) - Identifiers for time spans related to this topic, used for timeline or gap-related topics.
            - startDate (string, format YYYY or YYYY-MM) - Optional start date when the topic refers to a time period.
            - endDate (string, format YYYY or YYYY-MM) - Optional end date when the topic refers to a time period.
            - resumeItemBeforeId (string) - ID of a resume item before a detected gap.
            - resumeItemAfterId (string) - ID of a resume item after a detected gap.
            + gapType - Required if the topic type is "gap". Allowed values:
            - time - For normal missing periods between activities.
            - timeline_boundary - For gaps at the start or end of the resume timeline.
                        
            - reason - Short human-readable explanation for why the topic is needed.
                        
            - reasonMeta - Machine-readable structured metadata. Allowed fields:
                        
            - detectedBy (string)
                        
            - gapDurationInMonths (integer)
            - missingSectionCandidates (array of strings)
            - patternType (string)
            - confidenceScore (number)
            - relatedResumeItems (array of strings)
            - timestamp (ISO 8601 string)
            - priorityScore - Integer between 0 and 100.
            - priority - Priority level as "low", "medium", or "high".
            - createdAt - Timestamp (ISO 8601 format). Leave empty.
            - updatedAt - Timestamp (ISO 8601 format). Leave empty.
                        
            ## General Rules
                        
            Do not generate any fields other than those listed above.
            Omit optional fields if not applicable.
            Always output a JSON array ([...]), not a single object ({}).
            Do not embed or reference "threads" inside topics.
            Do not add explanations outside the JSON.
            Special Clarifications About Reference Usage
            Always use resumeItemId if the topic clearly refers to a specific item (e.g., a missing LinkedIn profile inside basics.profiles).
            If the topic is about basics overall (e.g., missing city in location), set resumeItemId to "basics".
            Use spans, startDate, endDate, resumeItemBeforeId, and resumeItemAfterId only for time-related or gap-related topics.
            Never add properties to reference that are not listed.
            
            Respond only with the correct JSON structure, no explanations, no comments, no additional markup.
             
             ## Examples
             
             ### Example 1 — Imperfect Basics Input (Missing LinkedIn Profile, Missing Summary)
             
             Career Basics Input:       
             {
               "name": "Anna Müller",
               "email": "anna@example.com",
               "summary": "",
               "location": {
                 "city": "",
                 "countryCode": "DE"
               },
               "profiles": [
                 {
                   "id": "1111111111",
                   "network": "LinkedIn",
                   "username": "",
                   "url": ""
                 }
               ],
               "id": "1122334455"
             }
             
             Expected Topics Output:
             
             [
               {
                 "id": "GENERATE_ID",
                 "type": "basics",
                 "reference": { "resumeItemId": "1122334455" },
                 "reason": "Candidate's professional summary is missing."
               },
               {
                 "id": "GENERATE_ID",
                 "type": "basics",
                 "reference": { "resumeItemId": "1122334455" },
                 "reason": "Candidate's city information is missing."
               },
               {
                 "id": "GENERATE_ID",
                 "type": "basics",
                 "reference": { "resumeItemId": "1111111111" },
                 "reason": "LinkedIn profile details are incomplete (username and URL missing)."
               }
             ]
             
             ### Example 2 — Well-Filled Basics Input (Missing Relocation Information)
             
             Career Basics Input:
             {
               "name": "Liam Smith",
               "email": "liam@example.com",
               "summary": "Experienced DevOps Engineer.",
               "location": {
                 "city": "Hamburg",
                 "countryCode": "DE"
               },
               "id": "1122334455"
             }
             
             Expected Topics Output:
             [
               {
                 "id": "GENERATE_ID",
                 "type": "basics",
                 "reference": { "resumeItemId": "1122334455" },
                 "reason": "Relocation preferences are not specified."
               }
             ]
            """;
}
