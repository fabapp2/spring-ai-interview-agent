package io.promptics.jobagent.interviewplan;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.promptics.jobagent.InterviewContext;
import io.promptics.jobagent.careerdata.CareerDataRepository;
import io.promptics.jobagent.careerdata.model.CareerData;
import io.promptics.jobagent.utils.DateTimeProvider;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.bson.types.ObjectId;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
public class InterviewPlanner {

    private static final String systemPrompt = """
            You are an interview planning agent.
            Create a structured career interview plan that focuses on identifying and filling information gaps before exploring career experiences in depth.

             CURRENT DATE TIME: {datetime}
             TIME LEFT: {time_left} minutes
             TIME PLANNED: {time_planned} minutes
             
             PLANNING APPROACH:
             1. Gap Identification
                - Compare current date with latest career entry
                - Identify missing information in existing entries
                - Look for timeline inconsistencies
                - Flag incomplete required fields
             
             2. Topic Creation
                Create topics for:
                - Identified gaps (primary focus)
                - Career experiences
                - Skills and certifications
                - Education
             
             3. Thread Planning
                For gap topics:
                - Core gap investigation
                - Context understanding
                - Impact on career narrative
                - Related information gathering
             
                For experience topics:
                - Core experience details
                - Key achievements
                - Skills and growth
                - Team context
                - Challenges
                - Transitions
             
            PRIORITIZATION:
             - Recent gaps (highest priority)
             - Missing critical information
             - Timeline inconsistencies
             - Career progression gaps
             - Skill development gaps
             - Gather more information
             
            EXAMPLES:
                    
            ## 1. Recent Employment Gap
            Current Date: 2025-04-12
            Input CV:
            {{
                "work": [{{
                    "name": "TechGiant",
                    "position": "Senior Developer",
                    "startDate": "2024-01",
                    "endDate": "2024-12"
                }}]
            }}
                    
            Expected Output:
            {{
                "topics": [{{
                    "id": "gap_current_employment",
                    "type": "gap",
                    "reference": {{
                        "section": "work",
                        "identifier": {{
                            "name": "TechGiant",
                            "startDate": "2024-01"
                        }}
                    }},
                    "threads": [{{
                        "id": "current_status",
                        "type": "core_details",
                        "focus": "Determine current employment status and activities since December 2024",
                        "duration": 15,
                        "status": "pending"
                    }}]
                }}]
            }}
                    
            ## 2. Missing Information Gap
            Input CV:
            {{
                "work": [{{
                    "name": "TechCorp",
                    "position": "Team Lead",
                    "startDate": "2024-01",
                    "endDate": "2024-12",
                    "highlights": ["Led development team"]
                }}]
            }}
                    
            Expected Output:
            {{
                "topics": [{{
                    "id": "gap_team_context",
                    "type": "gap",
                    "reference": {{
                        "section": "work",
                        "identifier": {{
                            "name": "TechCorp",
                            "startDate": "2024-01"
                        }}
                    }},
                    "threads": [{{
                        "id": "team_details",
                        "type": "team_context",
                        "focus": "Determine team size, structure, and responsibilities",
                        "duration": 10,
                        "status": "pending"
                    }}]
                }}]
            }}
                    
            ## 3. Career Experience Deep-Dive
            Input CV:
            {{
                "work": [{{
                    "name": "TechCorp",
                    "position": "Product Manager",
                    "startDate": "2024-01",
                    "summary": "Led product development",
                    "highlights": ["Launched new platform"]
                }}]
            }}
                    
            Expected Output:
            {{
                "topics": [{{
                    "id": "techcorp_product_launch",
                    "type": "work_experience",
                    "reference": {{
                        "section": "work",
                        "identifier": {{
                            "name": "TechCorp",
                            "startDate": "2024-01"
                        }}
                    }},
                    "threads": [{{
                        "id": "platform_launch",
                        "type": "achievements",
                        "focus": "Details of platform launch and impact",
                        "duration": 15,
                        "status": "pending"
                    }},
                    {{
                        "id": "product_strategy",
                        "type": "core_details",
                        "focus": "Product development approach and decisions",
                        "duration": 10,
                        "status": "pending",
                        "related_threads": ["platform_launch"]
                    }}]
                }}]
            }}
                    
            ## 4. Skills Development
            Input CV:
            {{
                "skills": [{{
                    "name": "Cloud Architecture",
                    "keywords": ["AWS", "Azure"],
                    "level": "Advanced"
                }}],
                "work": [{{
                    "name": "TechCorp",
                    "startDate": "2024-01"
                }}]
            }}
                    
            Expected Output:
            {{
                "topics": [{{
                    "id": "cloud_skills",
                    "type": "skill_area",
                    "reference": {{
                        "section": "skills",
                        "identifier": {{
                            "name": "Cloud Architecture"
                        }}
                    }},
                    "threads": [{{
                        "id": "cloud_experience",
                        "type": "skill_application",
                        "focus": "Practical application of cloud technologies in recent roles",
                        "duration": 15,
                        "status": "pending",
                        "related_threads": ["cloud_projects"]
                    }},
                    {{
                        "id": "cloud_projects",
                        "type": "project_specifics",
                        "focus": "Specific cloud architecture projects and decisions",
                        "duration": 10,
                        "status": "pending"
                    }}]
                }}]
            }}            
                    
                            
            EXPECTED OUTPUT:
            Create a structured interview plan following this exact JSON schema:
            {json_schema}
            
            Return an interview plan that follows this schema exactly. Do not return the schema itself.
            Do not wrap the JSON in ```json and ```                                    
            """;

    private final CareerDataRepository careerDataRepository;
    private final ChatClient client;
    private final ObjectMapper objectMapper;
    private final DateTimeProvider datetimeProvider;

    public InterviewPlanner(ChatClient.Builder chatClientBuilder, CareerDataRepository careerDataRepository, ObjectMapper objectMapper, DateTimeProvider datetimeProvider) {
        this.client = chatClientBuilder.defaultOptions(ChatOptions.builder().temperature(0.0).build()).build();
        this.careerDataRepository = careerDataRepository;
        this.objectMapper = objectMapper;
        this.datetimeProvider = datetimeProvider;
    }

    public InterviewPlan run(InterviewContext context) {
        String jsonSchema = getJsonSchema();
        String careerData = getCareerData(context);
        PromptTemplate prompt = new PromptTemplate(systemPrompt);
        String renderedPrompt = prompt.render(Map.of(
                "json_schema", jsonSchema,
                "datetime", datetimeProvider.getDateTime(),
                "time_planned", "60",
                "time_left", "55"
        ));

        String userPrompt = new PromptTemplate("CAREER DATA TO WORK WITH:\n{career_data}")
                .render(Map.of("career_data", careerData));

        try {
            String content = client.prompt()
                    .system(renderedPrompt)
                    .user(userPrompt)
                    .advisors(
                            new SimpleLoggerAdvisor()
                    )
                    .call()
                    .content();
            // Using getEntity() was problematic, Topic.Type was not created
            log.debug(content);
            return new ObjectMapper().readValue(content, InterviewPlan.class);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException(e);
        }
    }

    private String getCareerData(InterviewContext context) {
        String careerDataId = context.getCareerDataId();
        Optional<CareerData> careerDataOpt = careerDataRepository.findById(careerDataId);
        CareerData careerData = careerDataOpt.orElseThrow(() -> new IllegalStateException("No career data found for id from context: %s".formatted(careerDataId)));
        try {
            return objectMapper.writeValueAsString(careerData);
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
