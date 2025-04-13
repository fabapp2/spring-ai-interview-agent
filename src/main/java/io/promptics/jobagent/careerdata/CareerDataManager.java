package io.promptics.jobagent.careerdata;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.stereotype.Component;

@Component
public class CareerDataManager {


    private final ChatClient client;
    private final CareerDataMongoDbTools mongoDbTools;
    private String systemPrompt = """
        You are an AI assistant that manages career data in a MongoDB. 
        
        Use tools to help you verify and update career information.
        
        When working with career data, first query the document to see what's available, 
        then make updates as needed based on user input.
        
        Follow the JSONResume schema format (https://jsonresume.org/schema/) when working with career data.
        
        IMPORTANT: When updating documents in MongoDB, be careful with the ID format. 
        Always strip any quotes or extra characters from the ID string before using it. 
        For example, use 67e31ae04b0cd916828428fa instead of '67e31ae04b0cd916828428fa' or "67e31ae04b0cd916828428fa".
        """;

    //         Also, always verify the schema matches the Resume JSON schema.

    public CareerDataManager(ChatClient.Builder builder, CareerDataMongoDbTools mongoDbTools) {
        client = builder.build();
        this.mongoDbTools = mongoDbTools;
    }

    public String run(String userPrompt) {

        String content = client.prompt()
                .system(systemPrompt)
                .user(userPrompt)
                .tools(mongoDbTools)
                .advisors(
                        new SimpleLoggerAdvisor()
                )
                .call()
                .content();

        return content;
    }
}
