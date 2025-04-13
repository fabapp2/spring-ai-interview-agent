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
        
       Expected Result:
       Always return plain JSON without any additional text.
       Do not wrap the returned JSON in ```json and ```
       
       1. When data was successfully queried by id:
       Return the data
       
       2. When an update was successfully performed or an error occured:
       Return JSON with a status message.
       
       Json: 
       
       {
        "message": "message describing the result"
       }
       
       Examples:
       - "Successfully changed company name from XXX to YYY"
       - "Successfully removed XXX from the set of skills"
       - "Successfully added XXX to certifications"
       - "Could not remove XXX from set of skills because it does not exist" 
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
