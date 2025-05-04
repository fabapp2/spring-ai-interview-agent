package io.promptics.jobagent.interviewplan.model;

import com.fasterxml.jackson.annotation.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "careerDataId",
        "type",
        "reason",
        "reference",
        "priorityScore",
        "createdAt",
        "updatedAt"
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Topic {

    @JsonProperty("id")
    @JsonPropertyDescription("MongoDB document ID (Spring Data compatible)")
    private String id;

    @JsonProperty("careerDataId")
    @JsonPropertyDescription("ID of the resume or career data this topic belongs to")
    @NotNull
    private String careerDataId;

    @JsonProperty("type")
    @JsonPropertyDescription("The type of the topic, aligned to a resume section or 'narrative' for overarching topics")
    @NotNull
    private Topic.Type type;

    @JsonProperty("reason")
    @JsonPropertyDescription("Human-readable explanation for why this topic exists")
    @NotNull
    private String reason;

    @JsonProperty("reference")
    @JsonPropertyDescription("Reference to one or more resume items or a time period")
    @Valid
    private Reference reference;

    @JsonProperty("priorityScore")
    @JsonPropertyDescription("Numerical priority from 0 to 100")
    private Integer priorityScore;

    @JsonProperty("createdAt")
    @JsonPropertyDescription("ISO 8601 creation timestamp")
    @NotNull
    private Instant createdAt;

    @JsonProperty("updatedAt")
    @JsonPropertyDescription("ISO 8601 update timestamp")
    @NotNull
    private Instant updatedAt;

    public enum Type {
        BASICS("basics"),
        WORK("work"),
        EDUCATION("education"),
        PROJECTS("projects"),
        SKILLS("skills"),
        CERTIFICATES("certificates"),
        PUBLICATIONS("publications"),
        LANGUAGES("languages"),
        INTERESTS("initerests"),
        REFERENCES("references"),
        NARRATIVE("narrative");

        private final static Map<String, Type> CONSTANTS = new HashMap<String, Type>();
        private final String value;

        static {
            for (Type c : values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        Type(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        @JsonValue
        public String value() {
            return this.value;
        }

        @JsonCreator
        public static Topic.Type fromValue(String value) {
            Topic.Type constant = CONSTANTS.get(value.toLowerCase());
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }
    }
}
