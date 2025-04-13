package io.promptics.jobagent.careerdata;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.mongodb.core.query.Update.Position;
import com.mongodb.client.result.UpdateResult;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.data.mongodb.core.query.Update;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

@Service
public class MongoUpdateService {

    @Autowired
    private MongoTemplate mongoTemplate;

    public String updateDocument(String toolInput) {
        try {
            String idStr;
            String operation;
            String fieldPath;
            Object value;

            // Try to parse as JSON first
            try {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode parsedInput = mapper.readTree(toolInput);
                idStr = parsedInput.get("id_str") != null ? 
                        parsedInput.get("id_str").asText() : 
                        parsedInput.get("id").asText();
                operation = parsedInput.get("operation").asText();
                fieldPath = parsedInput.get("field_path").asText();
                value = parsedInput.has("value") ? parsedInput.get("value") : null;

            } catch (JsonProcessingException e) {
                // If not JSON, try comma-separated format
                String[] parts = toolInput.split(",", 4);
                if (parts.length < 3) {
                    return "Error: Input should be in format 'id_str, operation, field_path[, value]' or valid JSON";
                }

                idStr = parts[0].trim();
                operation = parts[1].trim();
                fieldPath = parts[2].trim();
                value = parts.length > 3 ? parseValue(parts[3].trim()) : null;
            }

            if (Stream.of(idStr, operation, fieldPath).anyMatch(String::isEmpty)) {
                return "Error: Missing required parameters. Need id_str, operation, and field_path.";
            }

            // Map operation to MongoDB update operator
            operation = operation.toLowerCase();
            Map<String, String> mongoOperators = new HashMap<>();
            mongoOperators.put("set", "$set");
            mongoOperators.put("push", "$push");
            mongoOperators.put("add_to_set", "$addToSet");
            mongoOperators.put("pull", "$pull");
            mongoOperators.put("unset", "$unset");
            mongoOperators.put("inc", "$inc");
            mongoOperators.put("mul", "$mul");
            mongoOperators.put("min", "$min");
            mongoOperators.put("max", "$max");
            mongoOperators.put("rename", "$rename");
            mongoOperators.put("pop", "$pop");


            if (!mongoOperators.containsKey(operation)) {
                return String.format("Error: Invalid operation '%s'. Valid operations are: %s",
                        operation, String.join(", ", mongoOperators.keySet()));
            }

            String mongoOperator = mongoOperators.get(operation);

            // Handle special case for unset
            if ("unset".equals(operation) && value == null) {
                value = 1;
            }

            // Build the update document
            Update update = new Update();
            switch (mongoOperator) {
                case "$set" -> update.set(fieldPath, value);
                case "$push" -> update.push(fieldPath, value);
                case "$addToSet" -> update.addToSet(fieldPath, value);
                case "$pull" -> update.pull(fieldPath, value);
                case "$unset" -> update.unset(fieldPath);
                case "$inc" -> update.inc(fieldPath, value instanceof Number ? (Number) value : 0);
                case "$mul" -> update.multiply(fieldPath, value instanceof Number ? (Number) value : 0);
                case "$min" -> update.min(fieldPath, value);
                case "$max" -> update.max(fieldPath, value);
                case "$rename" -> update.rename(fieldPath, value.toString());
                case "$pop" -> {
                    int popValue = Integer.parseInt(value.toString());
                    // 1 means pop last element, -1 means pop first element
                    Position position = popValue >= 0 ? Position.LAST : Position.FIRST;
                    update.pop(fieldPath, position);
                }
            }

            // Execute the update
            UpdateResult result = mongoTemplate.updateFirst(
                Query.query(Criteria.where("_id").is(new ObjectId(idStr))),
                update,
                "career_data"
            );

            if (result.getMatchedCount() == 0) {
                return String.format("No document found with ID: %s", idStr);
            } else if (result.getModifiedCount() > 0) {
                return String.format("Successfully updated document using %s on %s", operation, fieldPath);
            } else {
                return "Document found but no changes were made. The value might already be set to what you specified.";
            }

        } catch (Exception e) {
            return String.format("Error updating MongoDB: %s", e.getMessage());
        }
    }

    private Object parseValue(String valueStr) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(valueStr, Object.class);
        } catch (JsonProcessingException e) {
            return valueStr;
        }
    }
}
