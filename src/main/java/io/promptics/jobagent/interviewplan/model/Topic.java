
package io.promptics.jobagent.interviewplan.model;

import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "type",
        "reference",
        "gapType",
        "reason",
        "reasonMeta",
        "priorityScore",
        "priority",
        "createdAt",
        "updatedAt"
})
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document("topics")
public class Topic {

    /**
     * MongoDB ObjectId (24 character hex string) uniquely identifies the document
     * (Required)
     */
    @Id
    @NotNull
    private String id;
    /**
     * Defines the high-level topic area linked to resume sections or narrative concepts.
     * (Required)
     */
    @JsonProperty("type")
    @JsonPropertyDescription("Defines the high-level topic area linked to resume sections or narrative concepts.")
    @NotNull
    private Topic.Type type;
    /**
     * Links to specific resume entries or spans (optional).
     */
    @JsonProperty("reference")
    @JsonPropertyDescription("Links to specific resume entries or spans (optional).")
    @Valid
    private Reference reference;
    /**
     * Specifies if a gap is a timeline boundary or time-based.
     */
    @JsonProperty("gapType")
    @JsonPropertyDescription("Specifies if a gap is a timeline boundary or time-based.")
    private Topic.GapType gapType;
    /**
     * Optional human-readable explanation for why this topic exists.
     */
    @JsonProperty("reason")
    @JsonPropertyDescription("Optional human-readable explanation for why this topic exists.")
    private String reason;
    /**
     * Optional machine-readable metadata that explains how this topic was created.
     */
    @JsonProperty("reasonMeta")
    @JsonPropertyDescription("Optional machine-readable metadata that explains how this topic was created.")
    @Valid
    private ReasonMeta reasonMeta;
    /**
     * Priority of the topic, from 0 to 100, for machine-based sorting.
     */
    @JsonProperty("priorityScore")
    @JsonPropertyDescription("Priority of the topic, from 0 to 100, for machine-based sorting.")
    private Integer priorityScore;
    /**
     * Human-readable priority label, derived from priorityScore.
     */
    @JsonProperty("priority")
    @JsonPropertyDescription("Human-readable priority label, derived from priorityScore.")
    private Topic.Priority priority;
    /**
     * Timestamp when the topic was created.
     */
    @JsonProperty("createdAt")
    @JsonPropertyDescription("Timestamp when the topic was created.")
    private Instant createdAt;
    /**
     * Timestamp when the topic was created.
     */
    @JsonProperty("updatedAt")
    @JsonPropertyDescription("Timestamp when the topic was last updated.")
    private Instant updatedAt;

    /**
     * Specifies if a gap is a timeline boundary or time-based.
     */
    public enum GapType {

        TIME("time"),
        TIMELINE_BOUNDARY("timeline_boundary");
        private final String value;
        private final static Map<String, Topic.GapType> CONSTANTS = new HashMap<String, Topic.GapType>();

        static {
            for (Topic.GapType c : values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        GapType(String value) {
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
        public static Topic.GapType fromValue(String value) {
            Topic.GapType constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }


    /**
     * Human-readable priority label, derived from priorityScore.
     */
    public enum Priority {

        LOW("low"),
        MEDIUM("medium"),
        HIGH("high");
        private final String value;
        private final static Map<String, Topic.Priority> CONSTANTS = new HashMap<String, Topic.Priority>();

        static {
            for (Topic.Priority c : values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        Priority(String value) {
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
        public static Topic.Priority fromValue(String value) {
            Topic.Priority constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }


    /**
     * Defines the high-level topic area linked to resume sections or narrative concepts.
     */

    public enum Type {

        BASICS("basics"),
        WORK("work"),
        VOLUNTEER("volunteer"),
        EDUCATION("education"),
        PROJECTS("projects"),
        AWARDS("awards"),
        CERTIFICATES("certificates"),
        PUBLICATIONS("publications"),
        SKILLS("skills"),
        LANGUAGES("languages"),
        INTERESTS("interests"),
        REFERENCES("references"),
        GAP("gap"),
        CAREER_TRANSITION("career_transition"),
        FREELANCE_PERIOD("freelance_period"),
        TIMELINE_ARC("timeline_arc"),
        ROLE("role"),
        NARRATIVE("narrative");
        private final String value;
        private final static Map<String, Topic.Type> CONSTANTS = new HashMap<String, Topic.Type>();

        static {
            for (Topic.Type c : values()) {
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
            Topic.Type constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
