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
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.in;
import static org.junit.jupiter.api.Assertions.*;
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
              "career_data_id": "67e98007bd5c558ba6ad93d6",
              "topics": [
                {
                  "id": "1",
                  "type": "role",
                  "reference": {
                    "section": "work",
                    "identifier": {
                      "name": "TechGiant",
                      "startDate": "2024-01"
                    }
                  },
                  "threads": [
                    {
                      "id": "1.1",
                      "type": "validation",
                      "focus": "Confirm current role and responsibilities at TechGiant",
                      "duration": 300,
                      "actual_duration": 0,
                      "status": "pending",
                      "related_threads": [],
                      "fields": ["position", "highlights"]
                    },
                    {
                      "id": "1.2",
                      "type": "core_details",
                      "focus": "Explore specific responsibilities and projects at TechGiant",
                      "duration": 600,
                      "actual_duration": 0,
                      "status": "pending",
                      "related_threads": [],
                      "fields": ["highlights"]
                    },
                    {
                      "id": "1.3",
                      "type": "achievements",
                      "focus": "Discuss key achievements and their impacts at TechGiant",
                      "duration": 600,
                      "actual_duration": 0,
                      "status": "pending",
                      "related_threads": [],
                      "fields": ["highlights"]
                    },
                    {
                      "id": "1.4",
                      "type": "team_context",
                      "focus": "Understand team dynamics and organizational context at TechGiant",
                      "duration": 300,
                      "actual_duration": 0,
                      "status": "pending",
                      "related_threads": [],
                      "fields": ["highlights"]
                    },
                    {
                      "id": "1.5",
                      "type": "challenges",
                      "focus": "Identify challenges faced and learnings from experiences at TechGiant",
                      "duration": 600,
                      "actual_duration": 0,
                      "status": "pending",
                      "related_threads": [],
                      "fields": ["highlights"]
                    }
                  ]
                },
                {
                  "id": "2",
                  "type": "role",
                  "reference": {
                    "section": "work",
                    "identifier": {
                      "name": "MiniCorp",
                      "startDate": "2020-01"
                    }
                  },
                  "threads": [
                    {
                      "id": "2.1",
                      "type": "core_details",
                      "focus": "Explore specific responsibilities and projects at MiniCorp",
                      "duration": 600,
                      "actual_duration": 0,
                      "status": "pending",
                      "related_threads": [],
                      "fields": ["highlights"]
                    },
                    {
                      "id": "2.2",
                      "type": "achievements",
                      "focus": "Discuss key achievements and their impacts at MiniCorp",
                      "duration": 600,
                      "actual_duration": 0,
                      "status": "pending",
                      "related_threads": [],
                      "fields": ["highlights"]
                    },
                    {
                      "id": "2.3",
                      "type": "skills",
                      "focus": "Identify skills developed during the time at MiniCorp",
                      "duration": 300,
                      "actual_duration": 0,
                      "status": "pending",
                      "related_threads": [],
                      "fields": ["highlights"]
                    },
                    {
                      "id": "2.4",
                      "type": "challenges",
                      "focus": "Identify challenges faced and learnings from experiences at MiniCorp",
                      "duration": 600,
                      "actual_duration": 0,
                      "status": "pending",
                      "related_threads": [],
                      "fields": ["highlights"]
                    }
                  ]
                },
                {
                  "id": "3",
                  "type": "role",
                  "reference": {
                    "section": "work",
                    "identifier": {
                      "name": "StartupX",
                      "startDate": "2017-07"
                    }
                  },
                  "threads": [
                    {
                      "id": "3.1",
                      "type": "core_details",
                      "focus": "Explore specific responsibilities and projects at StartupX",
                      "duration": 600,
                      "actual_duration": 0,
                      "status": "pending",
                      "related_threads": [],
                      "fields": ["highlights"]
                    },
                    {
                      "id": "3.2",
                      "type": "achievements",
                      "focus": "Discuss key achievements and their impacts at StartupX",
                      "duration": 600,
                      "actual_duration": 0,
                      "status": "pending",
                      "related_threads": [],
                      "fields": ["highlights"]
                    },
                    {
                      "id": "3.3",
                      "type": "skills",
                      "focus": "Identify skills developed during the time at StartupX",
                      "duration": 300,
                      "actual_duration": 0,
                      "status": "pending",
                      "related_threads": [],
                      "fields": ["highlights"]
                    },
                    {
                      "id": "3.4",
                      "type": "challenges",
                      "focus": "Identify challenges faced and learnings from experiences at StartupX",
                      "duration": 600,
                      "actual_duration": 0,
                      "status": "pending",
                      "related_threads": [],
                      "fields": ["highlights"]
                    }
                  ]
                },
                {
                  "id": "4",
                  "type": "education",
                  "reference": {
                    "section": "education",
                    "identifier": {
                      "name": "TechU Berlin",
                      "startDate": "2015-09"
                    }
                  },
                  "threads": [
                    {
                      "id": "4.1",
                      "type": "core_details",
                      "focus": "Discuss coursework and projects during Master's at TechU Berlin",
                      "duration": 600,
                      "actual_duration": 0,
                      "status": "pending",
                      "related_threads": [],
                      "fields": ["courses"]
                    },
                    {
                      "id": "4.2",
                      "type": "skills",
                      "focus": "Identify skills gained during education at TechU Berlin",
                      "duration": 300,
                      "actual_duration": 0,
                      "status": "pending",
                      "related_threads": [],
                      "fields": ["score"]
                    }
                  ]
                },
                {
                  "id": "5",
                  "type": "certificates",
                  "reference": {
                    "section": "certificates",
                    "identifier": {
                      "name": "Certified Scrum Master"
                    }
                  },
                  "threads": [
                    {
                      "id": "5.1",
                      "type": "core_details",
                      "focus": "Discuss the relevance and application of certifications in current role",
                      "duration": 600,
                      "actual_duration": 0,
                      "status": "pending",
                      "related_threads": [],
                      "fields": ["name"]
                    }
                  ]
                }
              ]
            }        
            """;
        InterviewPlan interviewPlan = new ObjectMapper().readValue(json, InterviewPlan.class);
    }
}