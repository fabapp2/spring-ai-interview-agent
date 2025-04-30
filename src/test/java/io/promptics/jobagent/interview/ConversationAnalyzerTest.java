package io.promptics.jobagent.interview;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.promptics.jobagent.MongoDbConfig;
import io.promptics.jobagent.careerdata.model.CareerData;
import io.promptics.jobagent.interviewplan.InterviewPlanService;
import io.promptics.jobagent.interviewplan.TopicAndThread;
import io.promptics.jobagent.interviewplan.model.Topic;
import io.promptics.jobagent.interviewplan.model.TopicThread;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;

import java.io.IOException;

@SpringBootTest
class ConversationAnalyzerTest {

    @Autowired
    ConversationAnalyzer analyzer;

    @Test
    @DisplayName("analyze")
    void analyze() {
        Topic topic = Topic.builder().build();
        TopicThread thread = TopicThread.builder().build();
        TopicAndThread topicAndThread = new TopicAndThread(topic, thread);

        ThreadConversation conversation = ThreadConversation.builder()
                .entry(ConversationEntry.builder()
                        .role("assistant")
                        .timestamp("2025-02-14 22:24:01")
                        .text("What did you do after leaving TechGiant January 2024 and now?")
                        .build())
                .build();

        String s = analyzer.analyzeUserInput(topicAndThread, conversation, "I got certified with AWS and did a deepdive into AI and large language models.");
        // FIXME: finish test
        System.out.println(s);
    }
    
}