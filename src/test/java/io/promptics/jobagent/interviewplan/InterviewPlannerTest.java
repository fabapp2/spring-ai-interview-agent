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
        String interviewPlan = interviewPlanner.run(context);
        System.out.println(interviewPlan);
        InterviewPlan interviewPlan1 = objectMapper.readValue(interviewPlan, InterviewPlan.class);
        assertThat(interviewPlan1).isNotNull();
    }

    @Test
    @DisplayName("deserialize")
    void deserialize() throws JsonProcessingException {
        String json = """
                {
                    "career_data_id": "12345",
                    "topics": [
                        {
                            "id": "gap_current_status",
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
                                    "id": "gap_employment_2025",
                                    "type": "core_details",
                                    "focus": "Identify current employment status - CV only shows until 2024-12",
                                    "duration": 15,
                                    "actual_duration": 0,
                                    "status": "pending"
                                }
                            ]
                        },
                        {
                            "id": "gap_post_techgiant",
                            "type": "gap",
                            "reference": {
                                "section": "work",
                                "identifier": {
                                    "name": "TechGiant",
                                    "startDate": "2024-12"
                                }
                            },
                            "threads": [
                                {
                                    "id": "gap_recent_activities",
                                    "type": "core_details",
                                    "focus": "Understand activities and employment since TechGiant (2024-12 to present)",
                                    "duration": 20,
                                    "actual_duration": 0,
                                    "status": "pending"
                                },
                                {
                                    "id": "gap_transition_reason",
                                    "type": "transition",
                                    "focus": "Explore reasons for leaving TechGiant and career direction",
                                    "duration": 10,
                                    "actual_duration": 0,
                                    "status": "pending"
                                }
                            ]
                        },
                        {
                            "id": "gap_skills_update",
                            "type": "gap",
                            "reference": {
                                "section": "skills",
                                "identifier": {
                                    "name": "Programming Languages"
                                }
                            },
                            "threads": [
                                {
                                    "id": "gap_recent_skills",
                                    "type": "skill_application",
                                    "focus": "Update on new skills acquired since last CV update",
                                    "duration": 10,
                                    "actual_duration": 0,
                                    "status": "pending"
                                }
                            ]
                        },
                        {
                            "id": "gap_certificates_current",
                            "type": "gap",
                            "reference": {
                                "section": "certificates",
                                "identifier": {
                                    "name": "AWS Certified Solutions Architect"
                                }
                            },
                            "threads": [
                                {
                                    "id": "gap_cert_status",
                                    "type": "certification_details",
                                    "focus": "Verify current validity of certificates and any new ones obtained",
                                    "duration": 8,
                                    "actual_duration": 0,
                                    "status": "pending"
                                }
                            ]
                        },
                        {
                            "id": "gap_location_current",
                            "type": "gap",
                            "reference": {
                                "section": "basics",
                                "identifier": {
                                    "name": "Max Wurst"
                                }
                            },
                            "threads": [
                                {
                                    "id": "gap_current_location",
                                    "type": "core_details",
                                    "focus": "Verify current location and contact details",
                                    "duration": 5,
                                    "actual_duration": 0,
                                    "status": "pending"
                                }
                            ]
                        },
                        {
                            "id": "gap_techgiant_achievements",
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
                                    "id": "gap_final_achievements",
                                    "type": "achievements",
                                    "focus": "Document final achievements and impact at TechGiant",
                                    "duration": 15,
                                    "actual_duration": 0,
                                    "status": "pending"
                                },
                                {
                                    "id": "gap_project_completion",
                                    "type": "project_specifics",
                                    "focus": "Status of projects at time of departure",
                                    "duration": 10,
                                    "actual_duration": 0,
                                    "status": "pending"
                                }
                            ]
                        }
                    ]
                }
            """;
        InterviewPlan interviewPlan = new ObjectMapper().readValue(json, InterviewPlan.class);
    }
}