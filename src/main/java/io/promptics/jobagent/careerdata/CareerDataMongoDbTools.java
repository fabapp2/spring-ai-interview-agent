package io.promptics.jobagent.careerdata;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;

@Component
public class CareerDataMongoDbTools {

    private final MongoUpdateService mongoUpdateService;

    public CareerDataMongoDbTools(MongoUpdateService mongoUpdateService) {
        this.mongoUpdateService = mongoUpdateService;
    }

    @Tool(description = """
            Query MongoDB career data document by its ObjectId.
              Args:
                  tool_input: String with document ID
              Returns:
                  JSON string of the document or error message
            """
    )
    String findCareerDataById(@ToolParam(
            required = true,
            description = """
                    Always strip any quotes or
                    extra characters from the ID string before using it. For example, use 67e31ae04b0cd916828428fa\s
                    instead of '67e31ae04b0cd916828428fa' or "67e31ae04b0cd916828428fa"
                    """) String id) {
        try {
            ClassPathResource resource = new ClassPathResource("career-data.json");
            String careerDataJson = Files.readString(resource.getFile().toPath(), Charset.forName("UTF-8"));
            return careerDataJson;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Tool(description = """
            Versatile MongoDB document update function that can add, modify, or delete any field.
            """
    )
    String updateCareerData(@ToolParam(required = true, description = """
            Input format: JSON string or comma-separated values with the following components:
            - id_str: MongoDB document ID (required)
            - operation: 'set', 'push', 'pull', 'unset', 'add_to_set', etc. (MongoDB update operators without the $)
            - field_path: Path to the field using dot notation (e.g., "basics.name", "skills.0.keywords")
            - value: The value to use in the operation (not needed for 'unset')
            
            Examples:
            - '67e31ae04b0cd916828428fa, set, basics.name, John Doe'
            - '67e31ae04b0cd916828428fa, push, skills, {"name": "Advanced Problem Solving"}'
            - '67e31ae04b0cd916828428fa, unset, skills.0.level'
            
            Returns:
                Result message
            """) String query) {
        String s = mongoUpdateService.updateDocument(query);
        return s;
    }

}
