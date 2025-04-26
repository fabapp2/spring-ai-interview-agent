
package io.promptics.jobagent.interviewplan.model;

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
    private Date createdAt;
    /**
     * Timestamp when the topic was last updated.
     */
    @JsonProperty("updatedAt")
    @JsonPropertyDescription("Timestamp when the topic was last updated.")
    private Date updatedAt;

    /**
     * MongoDB ObjectId (24 character hex string) uniquely identifies the document
     * (Required)
     */
    @JsonProperty("id")
    public String getId() {
        return id;
    }

    /**
     * MongoDB ObjectId (24 character hex string) uniquely identifies the document
     * (Required)
     */
    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Defines the high-level topic area linked to resume sections or narrative concepts.
     * (Required)
     */
    @JsonProperty("type")
    public Topic.Type getType() {
        return type;
    }

    /**
     * Defines the high-level topic area linked to resume sections or narrative concepts.
     * (Required)
     */
    @JsonProperty("type")
    public void setType(Topic.Type type) {
        this.type = type;
    }

    /**
     * Links to specific resume entries or spans (optional).
     */
    @JsonProperty("reference")
    public Reference getReference() {
        return reference;
    }

    /**
     * Links to specific resume entries or spans (optional).
     */
    @JsonProperty("reference")
    public void setReference(Reference reference) {
        this.reference = reference;
    }

    /**
     * Specifies if a gap is a timeline boundary or time-based.
     */
    @JsonProperty("gapType")
    public Topic.GapType getGapType() {
        return gapType;
    }

    /**
     * Specifies if a gap is a timeline boundary or time-based.
     */
    @JsonProperty("gapType")
    public void setGapType(Topic.GapType gapType) {
        this.gapType = gapType;
    }

    /**
     * Optional human-readable explanation for why this topic exists.
     */
    @JsonProperty("reason")
    public String getReason() {
        return reason;
    }

    /**
     * Optional human-readable explanation for why this topic exists.
     */
    @JsonProperty("reason")
    public void setReason(String reason) {
        this.reason = reason;
    }

    /**
     * Optional machine-readable metadata that explains how this topic was created.
     */
    @JsonProperty("reasonMeta")
    public ReasonMeta getReasonMeta() {
        return reasonMeta;
    }

    /**
     * Optional machine-readable metadata that explains how this topic was created.
     */
    @JsonProperty("reasonMeta")
    public void setReasonMeta(ReasonMeta reasonMeta) {
        this.reasonMeta = reasonMeta;
    }

    /**
     * Priority of the topic, from 0 to 100, for machine-based sorting.
     */
    @JsonProperty("priorityScore")
    public Integer getPriorityScore() {
        return priorityScore;
    }

    /**
     * Priority of the topic, from 0 to 100, for machine-based sorting.
     */
    @JsonProperty("priorityScore")
    public void setPriorityScore(Integer priorityScore) {
        this.priorityScore = priorityScore;
    }

    /**
     * Human-readable priority label, derived from priorityScore.
     */
    @JsonProperty("priority")
    public Topic.Priority getPriority() {
        return priority;
    }

    /**
     * Human-readable priority label, derived from priorityScore.
     */
    @JsonProperty("priority")
    public void setPriority(Topic.Priority priority) {
        this.priority = priority;
    }

    /**
     * Timestamp when the topic was created.
     */
    @JsonProperty("createdAt")
    public Date getCreatedAt() {
        return createdAt;
    }

    /**
     * Timestamp when the topic was created.
     */
    @JsonProperty("createdAt")
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Timestamp when the topic was last updated.
     */
    @JsonProperty("updatedAt")
    public Date getUpdatedAt() {
        return updatedAt;
    }

    /**
     * Timestamp when the topic was last updated.
     */
    @JsonProperty("updatedAt")
    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }


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
