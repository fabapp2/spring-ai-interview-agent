package io.promptics.jobagent.verification;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.promptics.jobagent.careerdata.CareerDataManager;
import io.promptics.jobagent.careerdata.CareerDataMongoService;
import io.promptics.jobagent.careerdata.model.CareerData;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.stereotype.Component;

@Component
public class DataVerifier {

    public static final String RENDER_MODEL = "gpt-3.5-turbo";
    private final CareerDataMongoService careerDataMongoService;
    private final ChatClient renderClient;
    private final CareerDataManager careerDataManager;
    private ObjectMapper objectMapper = new ObjectMapper();

    public DataVerifier(ChatClient.Builder builder, CareerDataMongoService careerDataMongoService, CareerDataManager careerDataManager) {
        this.renderClient = builder.defaultOptions(ChatOptions.builder()
                .model(RENDER_MODEL)
                .temperature(0.0).build()).build();
        this.careerDataMongoService = careerDataMongoService;
        this.careerDataManager = careerDataManager;
    }

    public String execute(String userInput, String careerDataId) {

        String content = "here's your career data: \n\n";

        // User provided a message and probably wants to change data
        if(userInput != null && !userInput.isEmpty()) {
            content = careerDataManager.run(userInput, careerDataId);
            try {
                content = objectMapper.readTree(content).get("message").asText();
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }

        CareerData careerData = careerDataMongoService.getById(careerDataId);
        String currentCareerData = renderCareerData(careerData);

        return content + "\n" + currentCareerData;
    }

    private String renderCareerData(CareerData careerData) {
        try {
            // TODO: Remove rendering by model with code
            String json = objectMapper.writeValueAsString(careerData);
            String rendered = renderClient.prompt()
                    .user("""
                            Render the given JSON as {target}.
                            
                            Given JSON: {json}
                            
                            Expected output:
                            Only return the plain Markdown. No ``` or any other additional text or markup.
                            Do not return technical IDs.
                            """.replace("{json}", json).replace("{target}", "Plain text"))
                    .call()
                    .content();
            return rendered;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
