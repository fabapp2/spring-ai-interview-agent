
package io.promptics.jobagent.interviewplan;

import com.fasterxml.jackson.annotation.*;
import io.promptics.jobagent.interview.ConversationEntry;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "id",
    "type",
    "focus",
    "duration",
    "actual_duration",
    "status",
    "related_threads",
    "conversation"
})
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Thread {

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("identifier")
    public String identifier;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("type")
    public Thread.Type type;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("focus")
    public String focus;
    @JsonProperty("duration")
    public Integer duration;
    @JsonProperty("actual_duration")
    public Integer actualDuration;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("status")
    public Thread.Status status;
    @JsonProperty("related_threads")
    public List<String> relatedThreads = new ArrayList<String>();
    @JsonProperty("conversation")
    public List<ConversationEntry> conversation = new ArrayList<>();
    @JsonIgnore
    private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    public enum Status {

        PENDING("pending"),
        IN_PROGRESS("in_progress"),
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

    public enum Type {

        CORE_DETAILS("core_details"),
        ACHIEVEMENTS("achievements"),
        RESPONSIBILITIES("responsibilities"),
        SKILLS_USED("skills_used"),
        TEAM_CONTEXT    ("team_context"),
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
        public static Type fromValue(String value) {
            Type constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            }
            return constant;
        }

    }

}
