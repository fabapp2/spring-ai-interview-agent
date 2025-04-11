package io.promptics.jopbagent;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.DefaultChatClientBuilder;
import org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.model.chat.observation.autoconfigure.ChatObservationAutoConfiguration;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.actuate.observability.AutoConfigureObservability;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@AutoConfigureObservability
public class ChatHistoryExperimentTest {

   @Value("${spring.ai.openai.api-key}")
   String apiKey;

    @Test
    @DisplayName("prompt chat memory")
    void promptChatMemory() {
        ChatModel chatModel = OpenAiChatModel.builder()
                .openAiApi(
                        OpenAiApi.builder().apiKey(apiKey).build()
                )
                .build();

        ChatClient chatClient = ChatClient.create(chatModel);
        ChatMemory chatMemory = new InMemoryChatMemory();

        chatMemory.add(AbstractChatMemoryAdvisor.DEFAULT_CHAT_MEMORY_CONVERSATION_ID, new UserMessage("Hi my name is Fabian"));

        String reply = chatClient.prompt()
                .advisors(
                    new PromptChatMemoryAdvisor(chatMemory),
                    new SimpleLoggerAdvisor()
                )
                .user("What's my name?")
                .options(ChatOptions.builder().temperature(0.0).build())
                .call()
                .content();

        System.out.println(reply);
    }
}
