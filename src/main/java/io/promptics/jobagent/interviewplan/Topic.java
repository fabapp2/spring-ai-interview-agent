
package io.promptics.jobagent.interviewplan;

import com.fasterxml.jackson.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "id",
    "type",
    "reference",
    "threads"
})
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Topic {

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
    public Topic.Type type;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("reference")
    public Reference reference;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("threads")
    public List<Thread> threads = new ArrayList<Thread>();
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

    public enum Type {

        WORK_EXPERIENCE("work_experience"),
        VOLUNTEER_WORK("volunteer_work"),
        EDUCATION("education"),
        PROJECT("project"),
        AWARD("award"),
        CERTIFICATE("certificate"),
        PUBLICATION("publication"),
        SKILL_AREA("skill_area"),
        LANGUAGE("language"),
        INTEREST("interest"),
        REFERENCE("reference"),
        GAP("gap"),
        ROLE("role");

        private final String value;
        private final static Map<String, Topic.Type> CONSTANTS = new HashMap<String, Topic.Type>();

        static {
            for (Topic.Type c: values()) {
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
