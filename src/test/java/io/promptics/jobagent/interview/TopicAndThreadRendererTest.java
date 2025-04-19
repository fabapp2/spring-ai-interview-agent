package io.promptics.jobagent.interview;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.promptics.jobagent.interviewplan.TopicAndThread;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TopicAndThreadRendererTest {

    @Test
    @DisplayName("should render")
    void shouldRender() throws JsonProcessingException {

        String json = """
                {
                  "topic" : {
                    "identifier" : "gap_current_employment",
                    "type" : "gap",
                    "reference" : {
                      "section" : "work",
                      "identifier" : {
                        "name" : "TechGiant",
                        "startDate" : "2024-01"
                      }
                    },
                    "threads" : [ ]
                  },
                  "thread" : {
                    "identifier" : "current_status",
                    "type" : "core_details",
                    "focus" : "Determine current employment status and activities since December 2024",
                    "duration" : 15,
                    "status" : "in_progress",
                    "related_threads" : [ ],
                    "conversation" : [ ]
                  }
                }
                """;

        String expectedOutput = """
                # Current Topic: gap_current_employment
                                
                Type: Career Gap
                - Duration and timing
                - Reason and context
                - Skills maintained/developed
                - Professional development
                - Return to work transition
                                
                                
                                
                ## Reference Information
                - Section: work
                - Organization: TechGiant
                - Start Date: 2024-01
                                
                                
                # Current Thread: current_status
                                
                Type: Core Details
                - Essential information about the topic
                - Main responsibilities and activities
                - Key methods and approaches used
                - Regular duties and tasks
                - Overall context and environment
                                
                                
                                
                ## Focus Area
                Determine current employment status and activities since December 2024
                                
                ## Thread Parameters
                - Allocated Time: 15 minutes
                - Current Status: in_progress
                
                """;

        TopicAndThread topicAndThread = new ObjectMapper().readValue(json, TopicAndThread.class);
        TopicAndThreadRenderer sut = new TopicAndThreadRenderer(new TopicTypeDescriptionMapper(), new ThreadTypeDescriptionMapper());
        String rendered = sut.renderTopicAndThread(topicAndThread.getTopic(), topicAndThread.getThread());
//        System.out.println(rendered);
        assertThat(rendered).isEqualTo(expectedOutput);
    }

}