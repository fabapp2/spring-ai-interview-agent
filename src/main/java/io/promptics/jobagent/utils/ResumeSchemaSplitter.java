package io.promptics.jobagent.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Iterator;

public class ResumeSchemaSplitter {
    public static void main(String[] args) throws IOException {
        ResumeSchemaSplitter splitter = new ResumeSchemaSplitter();
        splitter.splitSchema(new ClassPathResource("resume-schema.json").getFile(), Path.of("./target").toFile());
    }

    public void splitSchema(File fullSchemaFile, File outputDirectory) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(fullSchemaFile);

        JsonNode properties = root.get("properties");

        for (Iterator<String> it = properties.fieldNames(); it.hasNext();) {
            String sectionName = it.next();
            JsonNode sectionSchema = properties.get(sectionName);

            ObjectNode partialSchema = mapper.createObjectNode();
            partialSchema.put("$schema", "http://json-schema.org/draft-07/schema#");
            partialSchema.put("title", "Career Section: " + sectionName);
            partialSchema.put("type", "object");

            ObjectNode props = mapper.createObjectNode();
            props.set(sectionName, sectionSchema);

            ArrayNode required = mapper.createArrayNode();
            required.add(sectionName);

            partialSchema.set("required", required);
            partialSchema.set("properties", props);

            File outFile = new File(outputDirectory, "career_" + sectionName + ".json");
            mapper.writerWithDefaultPrettyPrinter().writeValue(outFile, partialSchema);
        }
    }
}