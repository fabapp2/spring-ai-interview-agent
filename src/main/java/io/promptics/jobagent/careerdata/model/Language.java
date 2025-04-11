
package io.promptics.jobagent.careerdata.model;

import java.util.LinkedHashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.validation.Valid;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "language",
    "fluency"
})

public class Language {

    /**
     * e.g. English, Spanish
     * 
     */
    @JsonProperty("language")
    @JsonPropertyDescription("e.g. English, Spanish")
    private String language;
    /**
     * e.g. Fluent, Beginner
     * 
     */
    @JsonProperty("fluency")
    @JsonPropertyDescription("e.g. Fluent, Beginner")
    private String fluency;
    @JsonIgnore
    @Valid
    private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();

    /**
     * e.g. English, Spanish
     * 
     */
    @JsonProperty("language")
    public String getLanguage() {
        return language;
    }

    /**
     * e.g. English, Spanish
     * 
     */
    @JsonProperty("language")
    public void setLanguage(String language) {
        this.language = language;
    }

    /**
     * e.g. Fluent, Beginner
     * 
     */
    @JsonProperty("fluency")
    public String getFluency() {
        return fluency;
    }

    /**
     * e.g. Fluent, Beginner
     * 
     */
    @JsonProperty("fluency")
    public void setFluency(String fluency) {
        this.fluency = fluency;
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
