package io.promptics.jobagent.interviewplan.agents;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.promptics.jobagent.careerdata.model.Basics;
import io.promptics.jobagent.interviewplan.model.TopicThread;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.intellij.lang.annotations.Language;
import org.springframework.ai.template.st.StTemplateRenderer;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class BasicsThreadsPlanningAgent extends AbstractThreadsPlanningAgent<Basics> {

    private static final Double TEMPERATURE = 0.0;
    public static final String MODEL = "gpt-4.1-mini";
    private final ChatClient chatClient;

    public BasicsThreadsPlanningAgent(ChatClient.Builder builder, ObjectMapper objectMapper) {
        super(objectMapper);
        ChatOptions chatOptions = ChatOptions.builder()
                .temperature(TEMPERATURE)
                .model(MODEL)
                .build();
        this.chatClient = builder
                .defaultTemplateRenderer(
                        StTemplateRenderer.builder()
                                .startDelimiterToken('<')
                                .endDelimiterToken('>')
                                .build()
                ).defaultOptions(chatOptions).build();
    }

    @Override
    protected List<TopicThread> promptLlm(String basicsSectionJson, String topicsJson) {
        String userPrompt = new PromptTemplate(USER_PROMPT_TMPL).render(Map.of(
                        "basics", basicsSectionJson,
                        "topics", topicsJson
                )
        );
        List<TopicThread> response = chatClient.prompt()
                .system(SYSTEM_PROMPT)
                .user(userPrompt)
                .call()
                .entity(new ParameterizedTypeReference<List<TopicThread>>() {});

        return response;
    }

    private static final String USER_PROMPT_TMPL = """
            Given Basics Section:
            <basics>
                        
            Given Topics:
            <topics>
            """;

    @Language("markdown")
    static final String SYSTEM_PROMPT = """
            You are an AI assistant that generates interview threads for improving missing or incomplete resume data.
            You receive a list of Topics related to the "basics" section of a resume.
            Each Topic describes a specific gap or issue in the data.
            
            Only create a thread if information is clearly missing, incomplete, or needs clarification.
            Do not generate threads if the data is already present and valid.
            Do not speculate, improve wording, or suggest extra content.
            Do not include threads for other sections like work, education, or skills.
            Only respond to what the Topic identifies.
            
            Always set:
            - topicId: the Topic's id
            - focus: "core_details"
            - status: "pending"
            - focusReason: a short, actionable description of what to clarify
            
            Do not set:
            - id
            - careerDataId
            - createdAt
            - updatedAt
            - any other fields not listed
            
            Output only a valid JSON array.
            Do not return any text, comments, or formatting.
            
            ---
            
            Example 1
            Input:
            {
              "id": "TOPIC001",
              "type": "basics",
              "reason": "The phone number is missing"
            }
            
            Output:
            [
              {
                "topicId": "TOPIC001",
                "focus": "core_details",
                "status": "pending",
                "focusReason": "Request the candidate’s preferred phone number"
              }
            ]
            
            ---
            
            Example 2
            Input:
            {
              "id": "TOPIC002",
              "type": "basics",
              "reason": "The LinkedIn profile has no username or URL",
              "reference": { "resumeItemIds": ["1111111111"] }
            }
            
            Output:
            [
              {
                "topicId": "TOPIC002",
                "focus": "core_details",
                "status": "pending",
                "focusReason": "Confirm LinkedIn username and profile link"
              }
            ]
            
            ---
            
            Example 3
            Input:
            {
              "id": "TOPIC003",
              "type": "basics",
              "reason": "The city of residence is missing"
            }
            
            Output:
            [
              {
                "topicId": "TOPIC003",
                "focus": "core_details",
                "status": "pending",
                "focusReason": "Ask the candidate to provide their current city of residence"
              }
            ]
            
            ---
            
            Example 4
            Input:
            {
              "id": "TOPIC004",
              "type": "basics",
              "reason": "No issue detected"
            }
            
            Output:
            []
            """;
}
