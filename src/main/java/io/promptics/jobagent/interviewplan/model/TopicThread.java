
package io.promptics.jobagent.interviewplan.model;

import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "id",
    "careerDataId",
    "topicId",
    "focus",
    "focusReason",
    "status",
    "createdAt",
    "updatedAt"
})
@Document("threads")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TopicThread {

    /**
     * MongoDB ObjectId (24 character hex string) uniquely identifies the document
     * (Required)
     *
     */
    @Id
    @NotNull
    private String id;
    /**
     * MongoDB ObjectId (24 character hex string) of the topic this thread belongs to
     * (Required)
     *
     */
    @JsonProperty("topicId")
    @JsonPropertyDescription("MongoDB ObjectId (24 character hex string) of the topic this thread belongs to")
    @NotNull
    private String topicId;
    /**
     * Defines the inquiry focus of the thread.
     * (Required)
     *
     */
    @JsonProperty("focus")
    @JsonPropertyDescription("Defines the inquiry focus of the thread.")
    @NotNull
    private TopicThread.Focus focus;
    /**
     * Optional human-readable explanation for the purpose or nuance of this thread
     */
    @JsonProperty("focusReason")
    @JsonPropertyDescription("Optional human-readable explanation for the purpose or nuance of this thread")
    private String focusReason;
    /**
     * ID of the resume or career data this thread belongs to
     * (Required)
     */
    @JsonProperty("careerDataId")
    @JsonPropertyDescription("ID of the resume or career data this thread belongs to")
    @NotNull
    private String careerDataId;
    /**
     * Current lifecycle status of the thread.
     * (Required)
     *
     */
    @JsonProperty("status")
    @JsonPropertyDescription("Current lifecycle status of the thread.")
    @NotNull
    private TopicThread.Status status;
    /**
     * Timestamp when this thread was created.
     *
     */
    @JsonProperty("createdAt")
    @JsonPropertyDescription("Timestamp when this thread was created.")
    private Instant createdAt;
    /**
     * Timestamp when this thread was last updated.
     *
     */
    @JsonProperty("updatedAt")
    @JsonPropertyDescription("Timestamp when this thread was last updated.")
    private Instant updatedAt;

    /**
     * MongoDB ObjectId (24 character hex string) uniquely identifies the document
     * (Required)
     *
     */
    @JsonProperty("id")
    public String getId() {
        return id;
    }

    /**
     * MongoDB ObjectId (24 character hex string) uniquely identifies the document
     * (Required)
     *
     */
    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    /**
     * MongoDB ObjectId (24 character hex string) of the topic this thread belongs to
     * (Required)
     *
     */
    @JsonProperty("topicId")
    public String getTopicId() {
        return topicId;
    }

    /**
     * MongoDB ObjectId (24 character hex string) of the topic this thread belongs to
     * (Required)
     *
     */
    @JsonProperty("topicId")
    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }

    /**
     * Defines the inquiry focus of the thread.
     * (Required)
     *
     */
    @JsonProperty("focus")
    public TopicThread.Focus getFocus() {
        return focus;
    }

    /**
     * Defines the inquiry focus of the thread.
     * (Required)
     *
     */
    @JsonProperty("focus")
    public void setFocus(TopicThread.Focus focus) {
        this.focus = focus;
    }

    /**
     * Optional human-readable explanation for the purpose or nuance of this thread
     */
    @JsonProperty("focusReason")
    public String getFocusReason() {
        return focusReason;
    }

    /**
     * Optional human-readable explanation for the purpose or nuance of this thread
     */
    @JsonProperty("focusReason")
    public void setFocusReason(String focusReason) {
        this.focusReason = focusReason;
    }

    /**
     * ID of the resume or career data this thread belongs to
     * (Required)
     */
    @JsonProperty("careerDataId")
    public String getCareerDataId() {
        return careerDataId;
    }

    /**
     * ID of the resume or career data this thread belongs to
     * (Required)
     */
    @JsonProperty("careerDataId")
    public void setCareerDataId(String careerDataId) {
        this.careerDataId = careerDataId;
    }

    /**
     * Current lifecycle status of the thread.
     * (Required)
     *
     */
    @JsonProperty("status")
    public TopicThread.Status getStatus() {
        return status;
    }

    /**
     * Current lifecycle status of the thread.
     * (Required)
     *
     */
    @JsonProperty("status")
    public void setStatus(TopicThread.Status status) {
        this.status = status;
    }


    /**
     * Timestamp when this thread was created.
     *
     */
    @JsonProperty("createdAt")
    public Instant getCreatedAt() {
        return createdAt;
    }

    /**
     * Timestamp when this thread was created.
     *
     */
    @JsonProperty("createdAt")
    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Timestamp when this thread was last updated.
     *
     */
    @JsonProperty("updatedAt")
    public Instant getUpdatedAt() {
        return updatedAt;
    }

    /**
     * Timestamp when this thread was last updated.
     *
     */
    @JsonProperty("updatedAt")
    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }




    /**
     * Current lifecycle status of the thread.
     *
     */
    public enum Status {

        PENDING("pending"),
        IN_PROGRESS("in_progress"),
        ON_HOLD("on_hold"),
        COMPLETED("completed"),
        SKIPPED("skipped");
        private final String value;
        private final static Map<String, TopicThread.Status> CONSTANTS = new HashMap<>();

        static {
            for (TopicThread.Status c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        Status(String value) {
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
        public static TopicThread.Status fromValue(String value) {
            TopicThread.Status constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }


    /**
     * Defines the inquiry focus of the thread.
     *
     */
    public enum Focus {

        CORE_DETAILS("core_details"),
        ACHIEVEMENTS("achievements"),
        RESPONSIBILITIES("responsibilities"),
        SKILLS_USED("skills_used"),
        TEAM_CONTEXT("team_context"),
        CHALLENGES("challenges"),
        TRANSITION("transition"),
        IMPACT("impact"),
        LEARNING("learning"),
        COLLABORATION("collaboration"),
        TECHNICAL_DEPTH("technical_depth"),
        PROJECT_SPECIFICS("project_specifics");
        private final String value;
        private final static Map<String, TopicThread.Focus> CONSTANTS = new HashMap<String, TopicThread.Focus>();

        static {
            for (TopicThread.Focus c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        Focus(String value) {
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
        public static TopicThread.Focus fromValue(String value) {
            TopicThread.Focus constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
