package io.promptics.jobagent.careerdata;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.promptics.jobagent.careerdata.model.CareerData;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CareerDataMongoDbTools {

    private final CareerDataMongoService careerDataMongoService;

    public CareerDataMongoDbTools(CareerDataMongoService careerDataMongoService) {
        this.careerDataMongoService = careerDataMongoService;
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
            CareerData careerData = careerDataMongoService.getById(id);
            return new ObjectMapper().writeValueAsString(careerData);
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
        String s = careerDataMongoService.updateDocument(query);
        return s;
    }

}
