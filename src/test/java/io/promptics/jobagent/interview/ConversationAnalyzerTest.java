package io.promptics.jobagent.interview;

import io.promptics.jobagent.interviewplan.TopicAndThread;
import io.promptics.jobagent.interviewplan.model.Reference;
import io.promptics.jobagent.interviewplan.model.Topic;
import io.promptics.jobagent.interviewplan.model.TopicThread;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ResponseEntity;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ConversationAnalyzerTest {

    @Autowired
    ConversationAnalyzer analyzer;

    @Test
    @DisplayName("analyze")
    void analyze() {
        Topic topic = Topic.builder()
                .type(Topic.Type.BASICS)
                .reference(Reference.builder()
                        .resumeItemId("1122334455")
                        .build())
                .build();
        TopicThread thread = TopicThread.builder()
                .type(TopicThread.Type.CORE_DETAILS)
                .focus("Some data is missing")
                .build();
        TopicAndThread topicAndThread = new TopicAndThread(topic, thread);

        ThreadConversation conversation = ThreadConversation.builder()
                .entry(ConversationEntry.builder()
                        .role("assistant")
                        .timestamp("2025-02-14 22:24:01")
                        .text("What did you do after leaving TechGiant January 2024 and now?")
                        .build())
                .build();

        ConversationAnalysis s = analyzer.analyzeUserInput(topicAndThread, conversation, "I got certified with AWS and did a deep dive into AI and large language models.");
        // FIXME: finish test
        System.out.println(s);
    }
}