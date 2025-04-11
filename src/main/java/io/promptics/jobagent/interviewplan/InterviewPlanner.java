package io.promptics.jobagent.interviewplan;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.promptics.jobagent.InterviewContext;
import io.promptics.jobagent.careerdata.CareerDataRepository;
import io.promptics.jobagent.careerdata.model.CareerData;
import io.promptics.jobagent.utils.DateTimeProvider;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Map;
import java.util.Optional;

public class InterviewPlanner {
    
    private static String systemPrompt = """
            You are an interview planning agent. 
            Create a structured career interview plan that first validates all existing data before exploring deeper insights. 
            After validation, organize by specific career experiences as topics, with multiple investigation threads for each topic.
                        
            TIME LEFT: {time_left} minutes
            TIME PLANNED: {time_planned} minutes
            CURRENT DATE TIME: {datetime}
                        
            CAREER DATA TO WORK WITH:
            {career_data}
                        
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
            {few_shot}
                        
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
    private final InterviewContext context;
    private final ObjectMapper objectMapper;
    private final DateTimeProvider datetimeProvider;


    public InterviewPlanner(ChatClient.Builder chatClientBuilder, ChatMemory chatMemory, CareerDataRepository careerDataRepository, InterviewContext context, ObjectMapper objectMapper, DateTimeProvider datetimeProvider) {
        this.chatMemory = chatMemory;
        this.client = chatClientBuilder.build();
        this.careerDataRepository = careerDataRepository;
        this.context = context;
        this.objectMapper = objectMapper;
        this.datetimeProvider = datetimeProvider;
    }

    public InterviewPlan run() {
        PromptTemplate prompt = new PromptTemplate(systemPrompt);
        String jsonSchema = getJsonSchema();
        String careerData = getCareerData();
        String renderedPrompt = prompt.render(Map.of(
            "json_schema", jsonSchema,
            "few_shot", "",
            "career_data", careerData,
            "datetime", datetimeProvider.getDateTime(),
            "time_planned", "60",
            "time_left", "55"
        ));

        return client.prompt()
                .system(systemPrompt)
                .advisors(
                        new PromptChatMemoryAdvisor(chatMemory),
                        new SimpleLoggerAdvisor()
                )
                .call()
                .responseEntity(InterviewPlan.class)
                .getEntity();
    }

    private String getCareerData() {
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
