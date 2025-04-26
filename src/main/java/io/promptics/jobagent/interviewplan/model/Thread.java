
package io.promptics.jobagent.interviewplan.model;

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
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "id",
    "topicId",
    "type",
    "focus",
    "status",
    "duration",
    "actualDuration",
    "relatedThreads",
    "contextObject",
    "contextAction",
    "contextGoal",
    "createdAt",
    "updatedAt"
})
@Document("threads")
public class Thread {

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
    @JsonProperty("type")
    @JsonPropertyDescription("Defines the inquiry focus of the thread.")
    @NotNull
    private Thread.Type type;
    /**
     * Optional freeform text providing additional focus or nuance for the thread.
     * 
     */
    @JsonProperty("focus")
    @JsonPropertyDescription("Optional freeform text providing additional focus or nuance for the thread.")
    private String focus;
    /**
     * Current lifecycle status of the thread.
     * (Required)
     * 
     */
    @JsonProperty("status")
    @JsonPropertyDescription("Current lifecycle status of the thread.")
    @NotNull
    private Thread.Status status;
    /**
     * Estimated intended duration for handling this thread, in seconds.
     * 
     */
    @JsonProperty("duration")
    @JsonPropertyDescription("Estimated intended duration for handling this thread, in seconds.")
    @DecimalMin("1")
    private Integer duration;
    /**
     * Actual recorded duration spent on this thread, in seconds.
     * 
     */
    @JsonProperty("actualDuration")
    @JsonPropertyDescription("Actual recorded duration spent on this thread, in seconds.")
    @DecimalMin("0")
    private Integer actualDuration;
    /**
     * IDs of threads related to this one for cross-context purposes.
     * 
     */
    @JsonProperty("relatedThreads")
    @JsonPropertyDescription("IDs of threads related to this one for cross-context purposes.")
    @Valid
    private List<String> relatedThreads;
    /**
     * Optional structured context data to enhance thread prompting.
     * 
     */
    @JsonProperty("contextObject")
    @JsonPropertyDescription("Optional structured context data to enhance thread prompting.")
    @Valid
    private ContextObject contextObject;
    /**
     * Optional field describing the preferred agent behavior for this thread, such as 'ask', 'confirm', 'summarize'.
     * 
     */
    @JsonProperty("contextAction")
    @JsonPropertyDescription("Optional field describing the preferred agent behavior for this thread, such as 'ask', 'confirm', 'summarize'.")
    private Thread.ContextAction contextAction;
    /**
     * Optional field describing the specific goal of the thread, such as 'extract_responsibilities' or 'capture_achievements'.
     * 
     */
    @JsonProperty("contextGoal")
    @JsonPropertyDescription("Optional field describing the specific goal of the thread, such as 'extract_responsibilities' or 'capture_achievements'.")
    private Thread.ContextGoal contextGoal;
    /**
     * Timestamp when this thread was created.
     * 
     */
    @JsonProperty("createdAt")
    @JsonPropertyDescription("Timestamp when this thread was created.")
    private Date createdAt;
    /**
     * Timestamp when this thread was last updated.
     * 
     */
    @JsonProperty("updatedAt")
    @JsonPropertyDescription("Timestamp when this thread was last updated.")
    private Date updatedAt;

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
    @JsonProperty("type")
    public Thread.Type getType() {
        return type;
    }

    /**
     * Defines the inquiry focus of the thread.
     * (Required)
     * 
     */
    @JsonProperty("type")
    public void setType(Thread.Type type) {
        this.type = type;
    }

    /**
     * Optional freeform text providing additional focus or nuance for the thread.
     * 
     */
    @JsonProperty("focus")
    public String getFocus() {
        return focus;
    }

    /**
     * Optional freeform text providing additional focus or nuance for the thread.
     * 
     */
    @JsonProperty("focus")
    public void setFocus(String focus) {
        this.focus = focus;
    }

    /**
     * Current lifecycle status of the thread.
     * (Required)
     * 
     */
    @JsonProperty("status")
    public Thread.Status getStatus() {
        return status;
    }

    /**
     * Current lifecycle status of the thread.
     * (Required)
     * 
     */
    @JsonProperty("status")
    public void setStatus(Thread.Status status) {
        this.status = status;
    }

    /**
     * Estimated intended duration for handling this thread, in seconds.
     * 
     */
    @JsonProperty("duration")
    public Integer getDuration() {
        return duration;
    }

    /**
     * Estimated intended duration for handling this thread, in seconds.
     * 
     */
    @JsonProperty("duration")
    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    /**
     * Actual recorded duration spent on this thread, in seconds.
     * 
     */
    @JsonProperty("actualDuration")
    public Integer getActualDuration() {
        return actualDuration;
    }

    /**
     * Actual recorded duration spent on this thread, in seconds.
     * 
     */
    @JsonProperty("actualDuration")
    public void setActualDuration(Integer actualDuration) {
        this.actualDuration = actualDuration;
    }

    /**
     * IDs of threads related to this one for cross-context purposes.
     * 
     */
    @JsonProperty("relatedThreads")
    public List<String> getRelatedThreads() {
        return relatedThreads;
    }

    /**
     * IDs of threads related to this one for cross-context purposes.
     * 
     */
    @JsonProperty("relatedThreads")
    public void setRelatedThreads(List<String> relatedThreads) {
        this.relatedThreads = relatedThreads;
    }

    /**
     * Optional structured context data to enhance thread prompting.
     * 
     */
    @JsonProperty("contextObject")
    public ContextObject getContextObject() {
        return contextObject;
    }

    /**
     * Optional structured context data to enhance thread prompting.
     * 
     */
    @JsonProperty("contextObject")
    public void setContextObject(ContextObject contextObject) {
        this.contextObject = contextObject;
    }

    /**
     * Optional field describing the preferred agent behavior for this thread, such as 'ask', 'confirm', 'summarize'.
     * 
     */
    @JsonProperty("contextAction")
    public Thread.ContextAction getContextAction() {
        return contextAction;
    }

    /**
     * Optional field describing the preferred agent behavior for this thread, such as 'ask', 'confirm', 'summarize'.
     * 
     */
    @JsonProperty("contextAction")
    public void setContextAction(Thread.ContextAction contextAction) {
        this.contextAction = contextAction;
    }

    /**
     * Optional field describing the specific goal of the thread, such as 'extract_responsibilities' or 'capture_achievements'.
     * 
     */
    @JsonProperty("contextGoal")
    public Thread.ContextGoal getContextGoal() {
        return contextGoal;
    }

    /**
     * Optional field describing the specific goal of the thread, such as 'extract_responsibilities' or 'capture_achievements'.
     * 
     */
    @JsonProperty("contextGoal")
    public void setContextGoal(Thread.ContextGoal contextGoal) {
        this.contextGoal = contextGoal;
    }

    /**
     * Timestamp when this thread was created.
     * 
     */
    @JsonProperty("createdAt")
    public Date getCreatedAt() {
        return createdAt;
    }

    /**
     * Timestamp when this thread was created.
     * 
     */
    @JsonProperty("createdAt")
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Timestamp when this thread was last updated.
     * 
     */
    @JsonProperty("updatedAt")
    public Date getUpdatedAt() {
        return updatedAt;
    }

    /**
     * Timestamp when this thread was last updated.
     * 
     */
    @JsonProperty("updatedAt")
    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }


    /**
     * Optional field describing the preferred agent behavior for this thread, such as 'ask', 'confirm', 'summarize'.
     * 
     */
    public enum ContextAction {

        ASK("ask"),
        CONFIRM("confirm"),
        VALIDATE("validate"),
        SUMMARIZE("summarize"),
        GENERATE("generate"),
        REFLECT("reflect");
        private final String value;
        private final static Map<String, Thread.ContextAction> CONSTANTS = new HashMap<String, Thread.ContextAction>();

        static {
            for (Thread.ContextAction c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        ContextAction(String value) {
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
        public static Thread.ContextAction fromValue(String value) {
            Thread.ContextAction constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }


    /**
     * Optional field describing the specific goal of the thread, such as 'extract_responsibilities' or 'capture_achievements'.
     * 
     */
public enum ContextGoal {

        EXTRACT_RESPONSIBILITIES("extract_responsibilities"),
        CAPTURE_ACHIEVEMENTS("capture_achievements"),
        UNDERSTAND_TEAM_CONTEXT("understand_team_context"),
        VALIDATE_TIMEFRAMES("validate_timeframes"),
        ENRICH_SKILL_APPLICATION("enrich_skill_application"),
        DESCRIBE_CHALLENGES("describe_challenges"),
        IDENTIFY_LEARNING_OUTCOMES("identify_learning_outcomes"),
        ASSESS_LANGUAGE_USAGE("assess_language_usage"),
        MAP_INTEREST_RELEVANCE("map_interest_relevance");
        private final String value;
        private final static Map<String, Thread.ContextGoal> CONSTANTS = new HashMap<String, Thread.ContextGoal>();

        static {
            for (Thread.ContextGoal c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        ContextGoal(String value) {
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
        public static Thread.ContextGoal fromValue(String value) {
            Thread.ContextGoal constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

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
        private final static Map<String, Thread.Status> CONSTANTS = new HashMap<String, Thread.Status>();

        static {
            for (Thread.Status c: values()) {
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
        public static Thread.Status fromValue(String value) {
            Thread.Status constant = CONSTANTS.get(value);
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
    public enum Type {

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
        PROJECT_SPECIFICS("project_specifics"),
        CERTIFICATION_DETAILS("certification_details"),
        PUBLICATION_IMPACT("publication_impact"),
        SKILL_APPLICATION("skill_application"),
        LANGUAGE_USAGE("language_usage"),
        INTEREST_RELEVANCE("interest_relevance");
        private final String value;
        private final static Map<String, Thread.Type> CONSTANTS = new HashMap<String, Thread.Type>();

        static {
            for (Thread.Type c: values()) {
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
        public static Thread.Type fromValue(String value) {
            Thread.Type constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
