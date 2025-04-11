
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
import jakarta.validation.constraints.Pattern;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "title",
    "date",
    "awarder",
    "summary"
})

public class Award {

    /**
     * e.g. One of the 100 greatest minds of the century
     * 
     */
    @JsonProperty("title")
    @JsonPropertyDescription("e.g. One of the 100 greatest minds of the century")
    private String title;
    /**
     * Similar to the standard date type, but each section after the year is optional. e.g. 2014-06-29 or 2023-04
     * 
     */
    @JsonProperty("date")
    @JsonPropertyDescription("Similar to the standard date type, but each section after the year is optional. e.g. 2014-06-29 or 2023-04")
    @Pattern(regexp = "^([1-2][0-9]{3}-[0-1][0-9]-[0-3][0-9]|[1-2][0-9]{3}-[0-1][0-9]|[1-2][0-9]{3})$")
    private String date;
    /**
     * e.g. Time Magazine
     * 
     */
    @JsonProperty("awarder")
    @JsonPropertyDescription("e.g. Time Magazine")
    private String awarder;
    /**
     * e.g. Received for my work with Quantum Physics
     * 
     */
    @JsonProperty("summary")
    @JsonPropertyDescription("e.g. Received for my work with Quantum Physics")
    private String summary;
    @JsonIgnore
    @Valid
    private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();

    /**
     * e.g. One of the 100 greatest minds of the century
     * 
     */
    @JsonProperty("title")
    public String getTitle() {
        return title;
    }

    /**
     * e.g. One of the 100 greatest minds of the century
     * 
     */
    @JsonProperty("title")
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Similar to the standard date type, but each section after the year is optional. e.g. 2014-06-29 or 2023-04
     * 
     */
    @JsonProperty("date")
    public String getDate() {
        return date;
    }

    /**
     * Similar to the standard date type, but each section after the year is optional. e.g. 2014-06-29 or 2023-04
     * 
     */
    @JsonProperty("date")
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * e.g. Time Magazine
     * 
     */
    @JsonProperty("awarder")
    public String getAwarder() {
        return awarder;
    }

    /**
     * e.g. Time Magazine
     * 
     */
    @JsonProperty("awarder")
    public void setAwarder(String awarder) {
        this.awarder = awarder;
    }

    /**
     * e.g. Received for my work with Quantum Physics
     * 
     */
    @JsonProperty("summary")
    public String getSummary() {
        return summary;
    }

    /**
     * e.g. Received for my work with Quantum Physics
     * 
     */
    @JsonProperty("summary")
    public void setSummary(String summary) {
        this.summary = summary;
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
