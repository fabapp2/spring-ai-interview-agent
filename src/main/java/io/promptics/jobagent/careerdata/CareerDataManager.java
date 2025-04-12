package io.promptics.jobagent.careerdata;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.stereotype.Component;

@Component
public class CareerDataManager {


    private final ChatClient client;
    private String systemPrompt = """
            """;

    public CareerDataManager(ChatClient.Builder builder) {
        client = builder.build();
    }

    public String run() {

        String userPrompt = """
                Get data for id "123abc"
                """;

        String content = client.prompt()
//                .system(systemPrompt)
                .user(userPrompt)
                .tools(new CareerDataMongoDbTools())
                .advisors(
                        new SimpleLoggerAdvisor()
                )
                .call()
                .content();

        return content;
    }
}
