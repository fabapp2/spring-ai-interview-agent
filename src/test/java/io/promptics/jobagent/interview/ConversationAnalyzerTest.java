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

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ConversationAnalyzerTest {

    @Autowired
    ConversationAnalyzer analyzer;

    @Test
    @DisplayName("analyze")
    void analyze() {
        Topic topic = Topic.builder()
                .type(Topic.Type.WORK)
                .reference(Reference.builder()
                        .resumeItemId("1122334455")
                        .build())
                .reason("Some data is missing")
                .build();
        // FIXME: Simplify and fix data model
        TopicThread thread = TopicThread.builder()
                .focus(TopicThread.Focus.CORE_DETAILS)
                .focusReason("There's a gap between teh last position at TechGiant and now")
                .build();
        TopicAndThread topicAndThread = new TopicAndThread(topic, thread);

        ThreadConversation conversation = ThreadConversation.builder()
                .entry(ConversationEntry.builder()
                        .role("assistant")
                        .timestamp("2025-02-14 22:24:01")
                        .text("What did you do after leaving TechGiant January 2024 and now?")
                        .build())
                .build();

        ConversationAnalysis analysis = analyzer.analyzeUserInput(topicAndThread, conversation, "I got certified with AWS and did a deep dive into AI and large language models.");
        assertThat(analysis.getOriginalMessage()).isEqualTo("I got certified with AWS and did a deep dive into AI and large language models.");
        assertThat(analysis.getExtractedInformations().get(0).getSection()).isEqualTo("certifications");
        assertThat(analysis.getExtractedInformations().get(0).getInformations()).contains("Certified with AWS");
        assertThat(analysis.getExtractedInformations().get(1).getSection()).isEqualTo("work");
        assertThat(analysis.getExtractedInformations().get(1).getInformations()).contains("Did a deep dive into AI and large language models");
    }
}