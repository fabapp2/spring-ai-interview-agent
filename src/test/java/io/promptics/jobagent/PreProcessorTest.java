package io.promptics.jobagent;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.ExpectedToFail;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class PreProcessorTest {

    @Autowired
    PreProcessor preProcessor;

    @Autowired
    ChatMemory chatMemory;

    @Test
    // FIXME:
    @ExpectedToFail("Needs some love")
    @DisplayName("simple prompt")
    void simplePrompt() {
//        chatMemory.add(AbstractChatMemoryAdvisor.DEFAULT_CHAT_MEMORY_CONVERSATION_ID, new UserMessage("Howdie!"));
        String userMessage = "This interview is boing.";
        MessageAnalysis analysis = preProcessor.execute(userMessage);
        assertThat(analysis.getIsCorrected()).isTrue();
        assertThat(analysis.getCorrectedMessage()).isEqualTo("This interview is boring.");
        assertThat(analysis.getIntent()).isEqualTo(MessageAnalysis.Intent.INVALID);
    }

    @Test
    @DisplayName("serialize")
    void serialize() throws JsonProcessingException {
        String json = """
                {
                  "intent": "INVALID",
                  "reason": "User is expressing their feelings about the interview being boring.",
                  "isCorrected": "true",
                  "previousMessage": "",
                  "originalMessage": "This interview is boing.",
                  "correctedMessage": "This interview is boring."
                }
                """;
        MessageAnalysis messageAnalysis = new ObjectMapper().readValue(json, MessageAnalysis.class);
        assertThat(messageAnalysis).isNotNull();
        assertThat(messageAnalysis.getIsCorrected()).isTrue();
        assertThat(messageAnalysis.getOriginalMessage()).isEqualTo("This interview is boing.");
        assertThat(messageAnalysis.getCorrectedMessage()).isEqualTo("This interview is boring.");
        assertThat(messageAnalysis.isCorrected()).isTrue();
        assertThat(messageAnalysis.getIntent()).isEqualTo(MessageAnalysis.Intent.INVALID);
        assertThat(messageAnalysis.getReason()).isEqualTo("User is expressing their feelings about the interview being boring.");
    }
}