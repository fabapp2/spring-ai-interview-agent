package io.promptics.jobagent;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.stereotype.Component;

@Component
public class PreProcessor {

    private final ChatClient client;

    private final ChatMemory chatMemory;

    private static final String prompt = """
        You are pre-processing user inputs for a career data interview.
        
        ## Task
        - Read the user input
        - Correct obvious spelling errors
        - Set the isCorrected falg to 'true' when the user message was corrected.
        - Recognize the user intent
        
        ## Expected output
        You return JSON.
        Use this structure: 
        
        {{
            "intent": "QNA, VERIFICATION or INVALID",
            "reason": "The reason for the intent",
            "isCorrected": "true|false depending if the user message was corrected",
            "previousMessage": "The previous ai response if any",
            "originalMessage": "The original usesr input",
            "correctedMessage": "The corrected user input"
        }}
        """;

    public PreProcessor(ChatClient.Builder chatClientBuilder, ChatMemory chatMemory) {
        this.chatMemory = chatMemory;
        this.client = chatClientBuilder.build();
    }

    public MessageAnalysis execute(String userMessage) {
        chatMemory.add(AbstractChatMemoryAdvisor.DEFAULT_CHAT_MEMORY_CONVERSATION_ID, new AssistantMessage("What was you last job?"));
        chatMemory.add(AbstractChatMemoryAdvisor.DEFAULT_CHAT_MEMORY_CONVERSATION_ID, new UserMessage("I worked at Mc Donalds"));
        chatMemory.add(AbstractChatMemoryAdvisor.DEFAULT_CHAT_MEMORY_CONVERSATION_ID, new AssistantMessage("How long did you work there?"));

        return client.prompt()
                .system(prompt)
                .advisors(
                        new PromptChatMemoryAdvisor(chatMemory),
                        new SimpleLoggerAdvisor()
                )
                .user(userMessage)
                .call()
                .responseEntity(MessageAnalysis.class)
                .getEntity();

    }
}
