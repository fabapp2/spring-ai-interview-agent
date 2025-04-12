package io.promptics.jobagent.interviewplan;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.promptics.jobagent.InterviewContext;
import io.promptics.jobagent.careerdata.CareerDataRepository;
import io.promptics.jobagent.careerdata.model.CareerData;
import io.promptics.jobagent.utils.DateTimeProvider;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Map;
import java.util.Optional;

@Component
public class InterviewPlanner {
    
    private static String systemPrompt = """
            You are an interview planning agent.
            Create a structured career interview plan that first validates all existing data before exploring deeper insights. 
            After validation, organize by specific career experiences as topics, with multiple investigation threads for each topic.
                        
            TIME LEFT: {time_left} minutes
            TIME PLANNED: {time_planned} minutes
            CURRENT DATE TIME: {datetime}
                        
            GUIDELINES:
                        
            DETAILED EXPLORATION:
            - Identify distinct topics from career experiences (specific roles, education, projects)
            - For each topic, plan multiple investigation threads:
              * Core experience details
              * Key achievements and impacts
              * Skills and growth
              * Team and organizational context
              * Challenges and learnings
              * Transition context (entry/exit)
                        
            - Prioritize topics and threads based on:
              * Information gaps
              * Impact and significance
              * Recent relevance
              * Career progression story
                        
            - Ensure threads maintain:
              * Clear focus and purpose
              * Relationships to other threads
              * Context preservation
              * Completion criteria
                        
            - Enable dynamic thread creation based on discoveries
                        
            EXAMPLES:
            
            ## Fill Gaps
            **1. Recent work experience:**
            Given that today is: 2025-01-03 and this is the last known work experience:
            ```json
            {{
                "name": "TechGiant",
                "position": "Senior Developer",
                "startDate": "2024-01",
                "endDate": "2024-12"
            }}
                        
            **Expected Interview Plan:**
            ```json
            {{
              "sections": [
                {{
                  "type": "fill_gap",
                  "duration": 1,
                  "actual_duration": 0,
                  "focus": "Unexplained gap between Dec 2024 and today",
                  "status": "pending",
                  "cv_reference": {{
                    "section": "work",
                    "identifier": {{
                        "name": "TechGiant",
                        "startDate": "2024-01"
                    }},
                    "fields": ["startDate", "endDate"]
                  }}
                }}
              ]
            }}
            ```
                        
            **2. A gap in the work experience:**
            ```json
            {{
              "work": [
                {{ "name": "Company A", "startDate": "2018-01", "endDate": "2020-06" }},
                {{ "name": "Company B", "startDate": "2021-01" }}
              ]
            }}
            ```
                        
            **Expected Interview Plan:**
            ```json
            {{
              "sections": [
                {{
                  "type": "fill_gap",
                  "duration": 2,
                  "actual_duration": 0,
                  "focus": "Unexplained gap between June 2020 and January 2021",
                  "status": "pending",
                  "cv_reference": {{
                    "section": "work",
                    "identifier": "all",
                    "fields": ["startDate", "endDate"]
                  }}
                }}
              ]
            }}
            ```
                        
            ---
                        
            ## Validate Information
                        
            **CV Input:**
            ```json
            {{
              "basics": {{
                "name": "Sara Becker",
                "email": "sara@example.com",
                "location": {{ "city": "Berlin" }}
              }}
            }}
            ```
                        
            **Expected Interview Plan:**
            ```json
            {{
              "sections": [
                {{
                  "type": "validate",
                  "duration": 5,
                  "actual_duration": 0,
                  "focus": "Confirm personal contact information and location",
                  "status": "pending",
                  "cv_reference": {{
                    "section": "basics",
                    "identifier": "all",
                    "fields": ["name", "email", "location"]
                  }}
                }}
              ]
            }}
            ```
                        
            ---
                        
            ## Deep Dive
                        
            **CV Input:**
            ```json
            {{
              "work": [
                {{
                  "name": "TechCorp",
                  "position": "Product Manager",
                  "startDate": "2022-03",
                  "summary": "Managed 3 product lines"
                }}
              ]
            }}
            ```
                        
            **Expected Interview Plan:**
            ```json
            {{
              "sections": [
                {{
                  "type": "deep_dive",
                  "duration": 5,
                  "actual_duration": 0,
                  "focus": "Explore responsibilities and product strategy at TechCorp",
                  "status": "pending",
                  "cv_reference": {{
                    "section": "work",
                    "identifier": {{ "name": "TechCorp", "startDate": "2022-03" }},
                    "fields": ["summary", "highlights"]
                  }}
                }}
              ]
            }}
            ```
                        
            ---
                        
            ## Behavioral
                        
            **CV Input:**
            ```json
            {{
              "projects": [
                {{
                  "name": "Client Onboarding Redesign",
                  "roles": ["Team Lead"]
                }}
              ]
            }}
            ```
                        
            **Expected Interview Plan:**
            ```json
            {{
              "sections": [
                {{
                  "type": "behavioral",
                  "duration": 5,
                  "actual_duration": 0,
                  "focus": "Leadership experience during 'Client Onboarding Redesign' project",
                  "status": "pending",
                  "cv_reference": {{
                    "section": "projects",
                    "identifier": {{ "name": "Client Onboarding Redesign" }},
                    "fields": ["roles", "highlights"]
                  }}
                }}
              ]
            }}
            ```
                        
            ---
                        
            ## 🧠 Technical (type: `technical`)
                        
            **CV Input:**
            ```json
            {{
              "skills": [
                {{
                  "name": "Machine Learning",
                  "level": "Advanced",
                  "keywords": ["TensorFlow", "Python"]
                }}
              ]
            }}
            ```
                        
            **Expected Interview Plan:**
            ```json
            {{
              "sections": [
                {{
                  "type": "technical",
                  "duration": 15,
                  "actual_duration": 0,
                  "focus": "Test proficiency in Machine Learning, specifically TensorFlow",
                  "status": "pending",
                  "cv_reference": {{
                    "section": "skills",
                    "identifier": {{ "name": "Machine Learning" }},
                    "fields": ["keywords"]
                  }}
                }}
              ]
            }}
            ```
                        
            ---
                        
            ## 🔄 Transition (type: `transition`)
                        
            **CV Input:**
            ```json
            {{
              "work": [
                {{
                  "name": "DesignStudio",
                  "position": "Graphic Designer",
                  "endDate": "2020-08"
                }},
                {{
                  "name": "CodeWorks",
                  "position": "Frontend Developer",
                  "startDate": "2021-01"
                }}
              ]
            }}
            ```
                        
            **Expected Interview Plan:**
            ```json
            {{
              "sections": [
                {{
                  "type": "transition",
                  "duration": 10,
                  "actual_duration": 0,
                  "focus": "Discuss career change from Graphic Designer to Frontend Developer",
                  "status": "pending",
                  "cv_reference": {{
                    "section": "work",
                    "identifier": "all",
                    "fields": ["position", "startDate", "endDate"]
                  }}
                }}
              ]
            }}
            ```
                        
            EXPECTED OUTPUT:
            Create a structured plan that:
            1. Groups by specific career experience topics
            2. Defines multiple investigation threads per topic
            3. Allocates time based on significance and data completeness
            4. Maintains thread relationships and context
                        
            Return only the JSON strictly following the provided JSON schema and no additional text.
            Expected JSON schema:
            {json_schema}
            """;

    private final CareerDataRepository careerDataRepository;
    private final ChatMemory chatMemory;
    private final ChatClient client;
    private final ObjectMapper objectMapper;
    private final DateTimeProvider datetimeProvider;

    public InterviewPlanner(ChatClient.Builder chatClientBuilder, ChatMemory chatMemory, CareerDataRepository careerDataRepository, ObjectMapper objectMapper, DateTimeProvider datetimeProvider) {
        this.chatMemory = chatMemory;
        this.client = chatClientBuilder.defaultOptions(ChatOptions.builder().temperature(0.0).build()).build();
        this.careerDataRepository = careerDataRepository;
        this.objectMapper = objectMapper;
        this.datetimeProvider = datetimeProvider;
    }

    public String run(InterviewContext context) {
        String jsonSchema = getJsonSchema();
        String careerData = getCareerData(context);
        PromptTemplate prompt = new PromptTemplate(systemPrompt);
        String renderedPrompt = prompt.render(Map.of(
            "json_schema", jsonSchema,
            "datetime", datetimeProvider.getDateTime(),
            "time_planned", "60",
            "time_left", "55"
        ));

        String userPrompt = new PromptTemplate("CAREER DATA TO WORK WITH:\n{career_data}").render(Map.of("career_data", careerData));

        return client.prompt()
                .system(renderedPrompt)
                .user(userPrompt)
//                .advisors(
//                        new SimpleLoggerAdvisor()
//                )
                .call()
                .content();
//                .responseEntity(InterviewPlan.class)
//                .getEntity();
    }

    private String getCareerData(InterviewContext context) {
        String careerDataId = context.getCareerDataId();
        Optional<CareerData> careerDataOpt = careerDataRepository.findById(careerDataId);
        CareerData careerData = careerDataOpt.orElseThrow(() -> new IllegalStateException("No career data found for id from context: %s".formatted(careerDataId)));
        try {
            String asString = objectMapper.writeValueAsString(careerData);
            return asString;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private String getJsonSchema() {
        try {
            ClassPathResource resource = new ClassPathResource("interview-plan-schema.json");
            return Files.readString(resource.getFile().toPath(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
