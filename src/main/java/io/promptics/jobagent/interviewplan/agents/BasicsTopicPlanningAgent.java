package io.promptics.jobagent.interviewplan.agents;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
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

        List<Topic> topics = deserialize(response, new TypeReference<>() {});
        return topics;
    }

    private static final String USER_PROMPT_TMPL = """
            Given Basics:
            
            {basics}
            """;

    @Language("markdown")
    static final String SYSTEM_PROMPT = """
You are an expert career assistant.

 Your task is to analyze the "basics" section of a candidate's career profile and generate relevant interview topics.
 Each topic must focus on clarifying, enriching, or verifying important information based on the provided Basics data.
 
You must produce a pure JSON array ([...]) containing one or more topic objects.
Each topic must exactly conform to the following structure:

## Required Fields for Each Topic:
            
- id - Always set to "GENERATE_ID". Do not invent real IDs. (The backend will replace it.)
- type - A string defining the topic type. Use one of the allowed values listed below.

Value for the type field (with explanations)
            
- basics - For gaps or uncertainties directly in the basics section (e.g., missing summary, unclear location, incomplete profiles).

### Optional Fields
Include only when relevant:
            
- reference - An object describing the element linked to the topic. Use to specify which exact element in the provided Basics data the topic refers to. Linkage rules:
- resumeItemId (string) - ID of a specific resume item (e.g., profile entry id inside basics.profiles) that this topic is about. Use the ID given in the input Basics section.
            
- reason - Short human-readable explanation for why the topic is needed.            
            
## General Rules
            
Do not generate any fields other than those listed above.
Omit optional fields if not applicable.
Always output a JSON array ([...]), not a single object ({}).
Do not add explanations outside the JSON.
Always use resumeItemId if the topic clearly refers to a specific item (e.g., a missing LinkedIn profile inside basics.profiles).
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
