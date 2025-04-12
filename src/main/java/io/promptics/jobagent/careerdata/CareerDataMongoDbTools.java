package io.promptics.jobagent.careerdata;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

@Component
public class CareerDataMongoDbTools {

    @Tool(description = """
            Query MongoDB career data document by its ObjectId.
              Args:
                  tool_input: String with document ID
              Returns:
                  JSON string of the document or error message
            """)
    String findCareerDataById(@ToolParam(
            required = true,
            description = """
                Always strip any quotes or
                extra characters from the ID string before using it. For example, use 67e31ae04b0cd916828428fa\s
                instead of '67e31ae04b0cd916828428fa' or "67e31ae04b0cd916828428fa"
                """) String id) {
        return "{\"career_data\": \"My career\"}";
    }

}
