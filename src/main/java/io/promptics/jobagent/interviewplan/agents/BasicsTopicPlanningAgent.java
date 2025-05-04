package io.promptics.jobagent.interviewplan.agents;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.*;
import io.promptics.jobagent.careerdata.model.Basics;
import io.promptics.jobagent.interviewplan.model.Topic;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.parser.TE;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.intellij.lang.annotations.Language;
import org.springframework.ai.template.TemplateRenderer;
import org.springframework.ai.template.st.StTemplateRenderer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

import java.lang.reflect.ParameterizedType;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@Component
public class BasicsTopicPlanningAgent extends AbstractTopicsPlanningAgent<Basics, Topic> {

    public static final String MODEL = "gpt-4.1-mini";
    public static final Double TEMPERATURE = 0.0;
    private static final String JSON_SCHEMA = "/schemas/plan/topics-array-schema.json";
    public static final StTemplateRenderer TEMPLATE_RENDERER = StTemplateRenderer.builder().startDelimiterToken('<').endDelimiterToken('>').build();

    public BasicsTopicPlanningAgent(ChatClient.Builder builder, @Qualifier("objectMapper") ObjectMapper objectMapper) {
        super(builder, objectMapper);
    }

    @Override
    protected String getModel() {
        return MODEL;
    }

    @Override
    protected Double getTemperature() {
        return TEMPERATURE;
    }

    public List<Topic> planTopics(String careerDataId, Basics basicsSection) {
        String section = serialize(basicsSection);
        String userPrompt = PromptTemplate.builder().renderer(TEMPLATE_RENDERER).template(USER_PROMPT_TMPL).build().render(Map.of("basics", section));
        List<Topic> topics = chatClient.prompt()
                .system(SYSTEM_PROMPT)
                .user(userPrompt)
                .call()
                .entity(new ParameterizedTypeReference<>() {});

        topics.forEach(topic -> {
            topic.setCareerDataId(careerDataId);
            Instant now = Instant.now();
            topic.setCreatedAt(now);
            topic.setUpdatedAt(now);
        });

        Set<ValidationMessage> validationMessages = validateJson(topics, JSON_SCHEMA);

        if(!validationMessages.isEmpty()) {
            // FIXME: call error handling prompt to fix errors
            throw new IllegalStateException("Generated JSON %s does not match for schema %s. Messages: %s".formatted(topics, JSON_SCHEMA, validationMessages));
        }

        return topics;
    }

    private static final String USER_PROMPT_TMPL = """
            <basics>
            """;

    @Language("markdown")
    static final String SYSTEM_PROMPT = """
        You are an AI assistant that generates structured Topics from the "basics" section of resume data.
        Each Topic represents a specific area to explore during an AI-led resume interview.
        Topics are high-level entry points and will later be expanded into Threads by another agent.
        
        Your job is to analyze the basics section and identify missing, inconsistent, or noteworthy information.
        Each Topic should focus on a clearly missing, incomplete, or invalid piece of information in the basics section.
        Do not suggest improvements for fields that are complete and reasonable.
        Only include a topic if the data is clearly missing, empty, or invalid.
        Do not generate topics about refining, expanding, or speculating on complete fields.
        Do not suggest additional profiles or deeper explanations unless something is explicitly missing or blank in the input.
        Do not create Topics related to other resume sections such as “education”, “work”, “projects”, or “skills” unless they are explicitly mentioned or clearly implied within the basics section.
        These areas are handled separately by other agents.
        
        Always set the type field to "basics".
        Your output must be a valid JSON array.
        Include only the following fields in each object:
        - type: always "basics"
        - reason: short explanation of why this topic should be discussed
        - reference.resumeItemIds: optional, only if the topic is clearly tied to an input item with an "id"
        
        Do not include any other fields.
        Do not include markdown, comments, or explanation text.
        
        ---
        
        ### Few-Shot Examples
        
        #### Example 1
        
        Input basics:
        {
          "email": "no-at-symbol.com",
          "phone": "",
          "summary": "Creative writer and community manager."
        }
        
        Output:
        [
          {
            "type": "basics",
            "reason": "The candidate's name is missing and should be collected"
          },
          {
            "type": "basics",
            "reason": "The email address appears to be invalid and should be verified"
          },
          {
            "type": "basics",
            "reason": "The phone number is missing and should be confirmed"
          },
          {
            "type": "basics",
            "reason": "The location information is missing"
          },
          {
            "type": "basics",
            "reason": "The candidate's professional title or role is not provided"
          },
          {
            "type": "basics",
            "reason": "No social or professional profiles are listed"
          }
        ]
        
        ---
        
        #### Example 2
        
        Input basics:
        {
          "profiles": [
            {
              "id": "1111111111",
              "network": "LinkedIn",
              "username": "",
              "url": ""
            }
          ]
        }
        
        Output:
        [
          {
            "type": "basics",
            "reason": "The candidate's name is missing and should be collected"
          },
          {
            "type": "basics",
            "reason": "The LinkedIn profile has both username and URL fields empty, which makes it incomplete",
            "reference": {
              "resumeItemIds": ["1111111111"]
            }
          }
        ]
        
        ---
        
        #### Example 3
        
        Input basics:
        {
          "name": "Leila M",
          "phone": "",
          "summary": "Student in computer science looking for internships in AI."
        }
        
        Output:
        [
          {
            "type": "basics",
            "reason": "The phone number is empty and should be collected"
          },
          {
            "type": "basics",
            "reason": "The candidate's educational background and internship goals should be clarified"
          },
          {
            "type": "basics",
            "reason": "The location information is missing"
          },
          {
            "type": "basics",
            "reason": "No professional title or current role is provided"
          }
        ]
        
        ---
        
        #### Example 4
        
        Input basics:
        {
          "label": "Full-Stack Developer",
          "location": {
            "city": "Berlin",
            "countryCode": "DE"
          },
          "profiles": [
            {
              "id": "abc222",
              "network": "GitHub",
              "username": "alexcodez",
              "url": "https://github.com/alexcodez"
            }
          ]
        }
        
        Output:
        [
          {
            "type": "basics",
            "reason": "The candidate's name is missing and should be confirmed"
          },
          {
            "type": "basics",
            "reason": "The GitHub profile might be useful to highlight during the interview",
            "reference": {
              "resumeItemIds": ["abc222"]
            }
          }
        ]
        
        ---
        
        #### Example 5
        
        Input basics:
        
        {
          "name": "Max Müller",
          "email": "max@foo.com",
          "label": "Software Engineer specialized in Cloud and JavaScript",
          "phone": "+66 2234 2233",
          "summary": "Software engineer with 2 years of experience, specializing in JavaScript and cloud technologies such as AWS and serverless architectures.",
          "location": {
            "country": "Germany"
          },
          "profiles": [
            {
              "id": "1111111111",
              "network": "LinkedIn",
              "username": "max",
              "url": "https://linkedin.com/in/max"
            }
          ]
        }
        
        Output:
        [
          {
            "type": "basics",
            "reason": "The candidate's location is incomplete as the city is missing"
          }
        ]
        
        Now generate a list of Topics based on the following basics section:
        """;

}
