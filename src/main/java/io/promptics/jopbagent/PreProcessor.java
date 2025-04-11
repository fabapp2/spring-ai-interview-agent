package io.promptics.jopbagent;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.objenesis.SpringObjenesis;
import org.springframework.stereotype.Component;

@Component
public class PreProcessor {

    private final ChatClient client;

    private final ChatMemory chatMemory;

    private static final String prompt = """
        You are pre-processing user inputs for a career data interview.
        
        ## Task
        - Read the user input
        - Corrrect obvious spelling errors
        - Recognize the user intent
        
        ## Expected output
        You return JSON.
        Use this structure: 
        
        {{
            "intent": "QnA or Verification, Invalid",
            "reason": "The reason for the intent",
            "was_corrected": "true|false depending if the user message was corrected",
            "previous": "The previous ai response if any",
            "original": "The original usesr input",
            "corrected": "The corrected user input"
        }}
        """;

    public PreProcessor(ChatClient.Builder chatClientBuilder, ChatMemory chatMemory) {
        this.chatMemory = chatMemory;
        this.client = chatClientBuilder.build();
    }

    public String run() {
        chatMemory.add("id", new AssistantMessage("What was you last job?"));
        chatMemory.add("id", new UserMessage("I worked at Mc Donalds"));
        chatMemory.add("id", new AssistantMessage("How long did you work there?"));

        return client.prompt()
                .system(prompt)
                .advisors(
                        new PromptChatMemoryAdvisor(chatMemory),
                        new SimpleLoggerAdvisor()
                )
                .user("What was your question?")
                .call()
                .content();
    }
}
