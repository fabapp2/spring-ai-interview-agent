
package io.promptics.jobagent.careerdata.model;

import com.fasterxml.jackson.annotation.*;
import jakarta.validation.Valid;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "name",
    "level",
    "keywords"
})

public class Skill extends SectionWithId {

    /**
     * e.g. Web Development
     * 
     */
    @JsonProperty("name")
    @JsonPropertyDescription("e.g. Web Development")
    private String name;
    /**
     * e.g. Master
     * 
     */
    @JsonProperty("level")
    @JsonPropertyDescription("e.g. Master")
    private String level;
    /**
     * List some keywords pertaining to this skill
     * 
     */
    @JsonProperty("keywords")
    @JsonPropertyDescription("List some keywords pertaining to this skill")
    @Valid
    private List<String> keywords;
    @JsonIgnore
    @Valid
    private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();

    /**
     * e.g. Web Development
     * 
     */
    @JsonProperty("name")
    public String getName() {
        return name;
    }

    /**
     * e.g. Web Development
     * 
     */
    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    /**
     * e.g. Master
     * 
     */
    @JsonProperty("level")
    public String getLevel() {
        return level;
    }

    /**
     * e.g. Master
     * 
     */
    @JsonProperty("level")
    public void setLevel(String level) {
        this.level = level;
    }

    /**
     * List some keywords pertaining to this skill
     * 
     */
    @JsonProperty("keywords")
    public List<String> getKeywords() {
        return keywords;
    }

    /**
     * List some keywords pertaining to this skill
     * 
     */
    @JsonProperty("keywords")
    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
