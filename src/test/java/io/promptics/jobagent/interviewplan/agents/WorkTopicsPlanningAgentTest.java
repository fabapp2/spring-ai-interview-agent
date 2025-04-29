package io.promptics.jobagent.interviewplan.agents;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.promptics.jobagent.careerdata.model.Work;
import io.promptics.jobagent.interviewplan.model.Topic;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class WorkTopicsPlanningAgentTest {

    @Autowired
    private WorkTopicsPlanningAgent agent;
    @Autowired
    ObjectMapper objectMapper;
    @Test
    @DisplayName("Test name")
    void testName() throws JsonProcessingException {
        List<Work> works = objectMapper.readValue("""
                [
                    {
                      "name": "StartupX",
                      "position": "Junior Developer",
                      "startDate": "2017-07",
                      "endDate": "2019-12",
                      "highlights": [
                        "Wrote groundbreaking 'Hello World' programs in 17 different languages",
                        "Debugged senior developers' code while they were on coffee breaks",
                        "Implemented a revolutionary system for organizing the office snack drawer",
                        "Mastered the art of looking busy during pointless meetings",
                        "Survived multiple pivots and learned to code faster than the business model could change",
                        "Mastered the art of translating 'I don't know' into 'Let me research that and get back to you'",
                        "Contributed to open-source projects, mostly by opening issues and forgetting about them"
                      ],
                      "id": "111111"
                    },
                    {
                      "name": "MiniCorp",
                      "position": "Software Engineer",
                      "startDate": "2020-01",
                      "endDate": "2022-06",
                      "highlights": [
                        "Architected scalable solutions while simultaneously scaling the office coffee consumption",
                        "Debugged legacy code older than some team members",
                        "Implemented agile methodologies, because who doesn't love a good sprint... away from deadlines",
                        "Mentored junior developers in the ancient art of Stack Overflow navigation"
                      ]
                    },
                    {
                      "name": "TechGiant",
                      "position": "Senior Developer",
                      "startDate": "2024-01",
                      "endDate": "2024-12",
                      "highlights": [
                        "Lead a team of developers who miraculously haven't quit yet",
                        "Architect scalable solutions that only crash during demos",
                        "Implement cutting-edge technologies nobody understands but everyone pretends to",
                        "Mentor junior developers in the art of Stack Overflow copying",
                        "Key Achievement: Reduced system downtime by 30% by turning it off and on again"
                      ]
                    }
                  ]
                """, new TypeReference<List<Work>>() {
        });
        List<Topic> topics = agent.plan(works);
        assertThat(topics).isNotEmpty();
    }
}