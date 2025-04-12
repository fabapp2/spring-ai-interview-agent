package io.promptics.jobagent.interviewplan;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.in;

@DataMongoTest
@Testcontainers
class InterviewPlanRepositoryTest {

    @Container
    @ServiceConnection
    final static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:7.0");

    @Autowired
    InterviewPlanRepository repository;

    @Test
    @DisplayName("crud")
    void crud() throws JsonProcessingException {
        String interviewPlanJson = """
                {
                    "topics": [
                        {
                            "id": "gap_current_role",
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
                                    "id": "gap_current_status",
                                    "type": "core_details",
                                    "focus": "Clarify current employment status - CV shows end date 2024-12 but that's in future",
                                    "duration": 5,
                                    "actual_duration": 0,
                                    "status": "pending"
                                }
                            ]
                        },
                        {
                            "id": "gap_minicorp_techgiant",
                            "type": "gap",
                            "reference": {
                                "section": "work",
                                "identifier": {
                                    "name": "MiniCorp",
                                    "startDate": "2022-06"
                                }
                            },
                            "threads": [
                                {
                                    "id": "gap_employment_break",
                                    "type": "core_details",
                                    "focus": "Understand period between MiniCorp (2022-06) and TechGiant (2024-01)",
                                    "duration": 10,
                                    "actual_duration": 0,
                                    "status": "pending"
                                }
                            ]
                        },
                        {
                            "id": "gap_startupx_minicorp",
                            "type": "gap",
                            "reference": {
                                "section": "work",
                                "identifier": {
                                    "name": "StartupX",
                                    "startDate": "2019-12"
                                }
                            },
                            "threads": [
                                {
                                    "id": "gap_transition",
                                    "type": "transition",
                                    "focus": "Clarify transition period between StartupX and MiniCorp",
                                    "duration": 5,
                                    "actual_duration": 0,
                                    "status": "pending"
                                }
                            ]
                        },
                        {
                            "id": "gap_tech_stack",
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
                                    "id": "gap_technologies",
                                    "type": "technical_depth",
                                    "focus": "Identify specific technologies and stack used in current role",
                                    "duration": 10,
                                    "actual_duration": 0,
                                    "status": "pending"
                                }
                            ]
                        },
                        {
                            "id": "gap_certificates_dates",
                            "type": "gap",
                            "reference": {
                                "section": "certificates",
                                "identifier": {
                                    "name": "AWS Certified Solutions Architect"
                                }
                            },
                            "threads": [
                                {
                                    "id": "gap_cert_details",
                                    "type": "certification_details",
                                    "focus": "Get missing dates and validity periods for certificates",
                                    "duration": 8,
                                    "actual_duration": 0,
                                    "status": "pending"
                                }
                            ]
                        },
                        {
                            "id": "gap_location",
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
                                    "focus": "Complete missing location information",
                                    "duration": 5,
                                    "actual_duration": 0,
                                    "status": "pending"
                                }
                            ]
                        },
                        {
                            "id": "gap_team_details",
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
                                    "id": "gap_team_size",
                                    "type": "team_context",
                                    "focus": "Determine team size and structure being led",
                                    "duration": 8,
                                    "actual_duration": 0,
                                    "status": "pending"
                                }
                            ]
                        },
                        {
                            "id": "gap_project_details",
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
                                    "id": "gap_projects",
                                    "type": "project_specifics",
                                    "focus": "Identify specific projects and their scope",
                                    "duration": 10,
                                    "actual_duration": 0,
                                    "status": "pending"
                                }
                            ]
                        }
                    ]
                }
                """;

        InterviewPlan interviewPlan = new ObjectMapper().readValue(interviewPlanJson, InterviewPlan.class);
        InterviewPlan saved = repository.save(interviewPlan);
        assertThat(saved).isNotNull();
    }
}