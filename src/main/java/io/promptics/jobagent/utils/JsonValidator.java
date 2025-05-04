package io.promptics.jobagent.utils;

import com.networknt.schema.*;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.util.Set;

public class JsonValidator {
    public static Set<ValidationMessage> validateJson(String json, String jsonSchema) {
        try {
            ClassPathResource resource = new ClassPathResource(jsonSchema);
            JsonSchemaFactory schemaFactory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V7);
            JsonSchema schema = schemaFactory.getSchema(resource.getURI());
            Set<ValidationMessage> messages = schema.validate(json, InputFormat.JSON);
            return messages;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
