package io.promptics.jobagent.interviewplan;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.promptics.jobagent.InterviewContext;
import io.promptics.jobagent.careerdata.CareerDataRepository;
import io.promptics.jobagent.careerdata.model.CareerData;
import io.promptics.jobagent.utils.DateTimeProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@SpringBootTest
class InterviewPlannerTest {

    @Autowired
    private InterviewPlanner interviewPlanner;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    DateTimeProvider dateTimeProvider;

    @MockitoBean
    CareerDataRepository careerDataRepository;

    CareerData careerData;

    @BeforeEach
    void beforeEach() {
        try {
            File file = new ClassPathResource("career-data.json").getFile();
            careerData = objectMapper.readValue(file, CareerData.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("should create interview plan")
    void shouldCreateInterviewPlan() throws JsonProcessingException {
        given(dateTimeProvider.getDateTime()).willReturn("2025-01-13 12:33:45");
        given(careerDataRepository.findById("67e98007bd5c558ba6ad93d6")).willReturn(Optional.of(careerData));
        InterviewContext context = new InterviewContext("67e98007bd5c558ba6ad93d6", "222", "333", "Max");
        InterviewPlan interviewPlan = interviewPlanner.run(context);
        assertThat(interviewPlan.getTopics()).hasSizeGreaterThan(2);
    }

    @Test
//    @Disabled("Only to verify deserialization if it fails")
    @DisplayName("deserialize")
    void deserialize() throws JsonProcessingException {
        String json = """
                {
                       "$schema": "http://json-schema.org/draft-07/schema#",
                       "type": "object",
                       "description": "Structure for planning career data gathering interviews",
                       "properties": {
                         "career_data_id": {
                           "type": "string",
                           "description": "Foreign key reference to the id of the career data document"
                         },
                         "topics": [
                           {
                             "id": "gap_current_employment",
                             "type": "gap",
                             "reference": {
                               "section": "work",
                               "identifier": {
                                 "name": "TechGiant",
                                 "startDate": "2024-01"
                               }
                             },
                             "threads": [
                               {
                                 "id": "current_status",
                                 "type": "core_details",
                                 "focus": "Determine current employment status and activities since December 2024",
                                 "duration": 15,
                                 "status": "pending"
                               }
                             ]
                           },
                           {
                             "id": "gap_team_context",
                             "type": "gap",
                             "reference": {
                               "section": "work",
                               "identifier": {
                                 "name": "TechGiant",
                                 "startDate": "2024-01"
                               }
                             },
                             "threads": [
                               {
                                 "id": "team_details",
                                 "type": "team_context",
                                 "focus": "Determine team size, structure, and responsibilities",
                                 "duration": 10,
                                 "status": "pending"
                               }
                             ]
                           },
                           {
                             "id": "techgiant_achievements",
                             "type": "work_experience",
                             "reference": {
                               "section": "work",
                               "identifier": {
                                 "name": "TechGiant",
                                 "startDate": "2024-01"
                               }
                             },
                             "threads": [
                               {
                                 "id": "system_downtime_reduction",
                                 "type": "achievements",
                                 "focus": "Details of how system downtime was reduced by 30%",
                                 "duration": 15,
                                 "status": "pending"
                               },
                               {
                                 "id": "team_leadership",
                                 "type": "core_details",
                                 "focus": "Leadership style and team dynamics",
                                 "duration": 10,
                                 "status": "pending"
                               }
                             ]
                           },
                           {
                             "id": "skills_development",
                             "type": "skill_area",
                             "reference": {
                               "section": "skills",
                               "identifier": {
                                 "name": "Programming Languages"
                               }
                             },
                             "threads": [
                               {
                                 "id": "programming_languages_usage",
                                 "type": "skill_application",
                                 "focus": "Practical application of programming languages in recent roles",
                                 "duration": 15,
                                 "status": "pending",
                                 "related_threads": ["frameworks_usage"]
                               },
                               {
                                 "id": "frameworks_usage",
                                 "type": "project_specifics",
                                 "focus": "Specific frameworks used in projects and their impact",
                                 "duration": 10,
                                 "status": "pending"
                               }
                             ]
                           }
                         ]
                       },
                       "required": ["topics"]
                     }
            """;
        InterviewPlan interviewPlan = new ObjectMapper().readValue(json, InterviewPlan.class);
    }
}