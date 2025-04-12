
package io.promptics.jobagent.careerdata.model;

import com.fasterxml.jackson.annotation.*;
import jakarta.validation.Valid;

import java.util.LinkedHashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "name",
    "reference"
})

public class Reference {

    /**
     * e.g. Timothy Cook
     * 
     */
    @JsonProperty("name")
    @JsonPropertyDescription("e.g. Timothy Cook")
    private String name;
    /**
     * e.g. Joe blogs was a great employee, who turned up to work at least once a week. He exceeded my expectations when it came to doing nothing.
     * 
     */
    @JsonProperty("reference")
    @JsonPropertyDescription("e.g. Joe blogs was a great employee, who turned up to work at least once a week. He exceeded my expectations when it came to doing nothing.")
    private String reference;
    @JsonIgnore
    @Valid
    private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();

    /**
     * e.g. Timothy Cook
     * 
     */
    @JsonProperty("name")
    public String getName() {
        return name;
    }

    /**
     * e.g. Timothy Cook
     * 
     */
    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    /**
     * e.g. Joe blogs was a great employee, who turned up to work at least once a week. He exceeded my expectations when it came to doing nothing.
     * 
     */
    @JsonProperty("reference")
    public String getReference() {
        return reference;
    }

    /**
     * e.g. Joe blogs was a great employee, who turned up to work at least once a week. He exceeded my expectations when it came to doing nothing.
     * 
     */
    @JsonProperty("reference")
    public void setReference(String reference) {
        this.reference = reference;
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
