
package io.promptics.jobagent.interviewplan;

import com.fasterxml.jackson.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "section",
    "identifier"
})
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Deprecated
public class Reference {

    @JsonProperty("section")
    public Reference.Section section;
    @JsonProperty("identifier")
    public Identifier identifier;
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

    public enum Section {
        BASICS("basics"),
        WORK("work"),
        VOLUNTEER("volunteer"),
        EDUCATION("education"),
        AWARDS("awards"),
        CERTIFICATES("certificates"),
        PUBLICATIONS("publications"),
        SKILLS("skills"),
        LANGUAGES("languages"),
        INTERESTS("interests"),
        REFERENCES("references"),
        PROJECTS("projects");
        private final String value;
        private final static Map<String, Reference.Section> CONSTANTS = new HashMap<String, Reference.Section>();

        static {
            for (Reference.Section c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        Section(String value) {
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
        public static Reference.Section fromValue(String value) {
            Reference.Section constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
