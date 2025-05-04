package io.promptics.jobagent.interview;

import io.promptics.jobagent.interviewplan.TopicAndThread;
import io.promptics.jobagent.interviewplan.model.Reference;
import io.promptics.jobagent.interviewplan.model.Topic;
import io.promptics.jobagent.interviewplan.model.TopicThread;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.time.ZonedDateTime;
import java.util.List;

class ConversationAnalyzerPromptRendererTest {
    
    @Test
    @DisplayName("render prompt")
    void renderPrompt() throws ParseException {
        ConversationAnalyzer.PromptRenderer sut = new ConversationAnalyzer.PromptRenderer(new TopicAndThreadRenderer(new ThreadTypeDescriptionMapper()), new ThreadConversationRenderer());
        ThreadConversation conversation = new ThreadConversation(
                "thread-conversation-id",
                "topic-thread-id",
                ZonedDateTime.parse("2025-03-04T11:23:24Z"),
                List.of(
                        ConversationEntry.builder()
                                .role("assistant")
                                .timestamp("2025-03-04 11:22:19")
                                .text("The telephone number has no country code")
                                .build(),
                        ConversationEntry.builder()
                                .role("user")
                                .timestamp("2025-03-04 11:22:46")
                                .text("The country code is +49")
                                .build(),
                        ConversationEntry.builder()
                                .role("assistant")
                                .timestamp("2025-03-04 11:23:22")
                                .text("What is the link for your LinkedIn profile?")
                                .build()
                )
        );
        TopicAndThread topicAndThread = new TopicAndThread(
                Topic.builder()
                    .type(Topic.Type.BASICS)
                    .reference(Reference.builder()
                            .resumeItemId("123123123")
                            .build())
                    .reason("Incomplete social media profile information")
                .build(),
                TopicThread.builder()
                        .id("topic-thread-id")
                        .focus(TopicThread.Focus.CORE_DETAILS)
                        .focusReason("No link is given for your LinkedIn profile")
                    .build()
        );
        String prompt = sut.renderPrompt(topicAndThread, conversation);
    }

}