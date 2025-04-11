package io.promptics.jopbagent;

import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CareerDataInterviewConfig {
    @Bean
    ChatMemory chatMemory() {
        return new InMemoryChatMemory();
    }
}
