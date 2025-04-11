package io.promptics.jopbagent;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PreProcessorTest {

    @Autowired
    PreProcessor preProcessor;

    @Autowired
    ChatMemory chatMemory;

    @Test
    @DisplayName("simple prompt")
    void simplePrompt() {
        chatMemory.add(AbstractChatMemoryAdvisor.DEFAULT_CHAT_MEMORY_CONVERSATION_ID, new UserMessage("Howdie!"));
        String run = preProcessor.run();
        System.out.println(run);
    }
}