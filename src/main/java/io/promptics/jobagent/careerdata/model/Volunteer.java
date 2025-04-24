
package io.promptics.jobagent.careerdata.model;

import com.fasterxml.jackson.annotation.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;

import java.net.URI;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "organization",
    "position",
    "url",
    "startDate",
    "endDate",
    "summary",
    "highlights"
})

public class Volunteer extends SectionWithId {

    /**
     * e.g. Facebook
     * 
     */
    @JsonProperty("organization")
    @JsonPropertyDescription("e.g. Facebook")
    private String organization;
    /**
     * e.g. Software Engineer
     * 
     */
    @JsonProperty("position")
    @JsonPropertyDescription("e.g. Software Engineer")
    private String position;
    /**
     * e.g. http://facebook.example.com
     * 
     */
    @JsonProperty("url")
    @JsonPropertyDescription("e.g. http://facebook.example.com")
    private URI url;
    /**
     * Similar to the standard date type, but each section after the year is optional. e.g. 2014-06-29 or 2023-04
     * 
     */
    @JsonProperty("startDate")
    @JsonPropertyDescription("Similar to the standard date type, but each section after the year is optional. e.g. 2014-06-29 or 2023-04")
    @Pattern(regexp = "^([1-2][0-9]{3}-[0-1][0-9]-[0-3][0-9]|[1-2][0-9]{3}-[0-1][0-9]|[1-2][0-9]{3})$")
    private String startDate;
    /**
     * Similar to the standard date type, but each section after the year is optional. e.g. 2014-06-29 or 2023-04
     * 
     */
    @JsonProperty("endDate")
    @JsonPropertyDescription("Similar to the standard date type, but each section after the year is optional. e.g. 2014-06-29 or 2023-04")
    @Pattern(regexp = "^([1-2][0-9]{3}-[0-1][0-9]-[0-3][0-9]|[1-2][0-9]{3}-[0-1][0-9]|[1-2][0-9]{3})$")
    private String endDate;
    /**
     * Give an overview of your responsibilities at the company
     * 
     */
    @JsonProperty("summary")
    @JsonPropertyDescription("Give an overview of your responsibilities at the company")
    private String summary;
    /**
     * Specify accomplishments and achievements
     * 
     */
    @JsonProperty("highlights")
    @JsonPropertyDescription("Specify accomplishments and achievements")
    @Valid
    private List<String> highlights;
    @JsonIgnore
    @Valid
    private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();

    /**
     * e.g. Facebook
     * 
     */
    @JsonProperty("organization")
    public String getOrganization() {
        return organization;
    }

    /**
     * e.g. Facebook
     * 
     */
    @JsonProperty("organization")
    public void setOrganization(String organization) {
        this.organization = organization;
    }

    /**
     * e.g. Software Engineer
     * 
     */
    @JsonProperty("position")
    public String getPosition() {
        return position;
    }

    /**
     * e.g. Software Engineer
     * 
     */
    @JsonProperty("position")
    public void setPosition(String position) {
        this.position = position;
    }

    /**
     * e.g. http://facebook.example.com
     * 
     */
    @JsonProperty("url")
    public URI getUrl() {
        return url;
    }

    /**
     * e.g. http://facebook.example.com
     * 
     */
    @JsonProperty("url")
    public void setUrl(URI url) {
        this.url = url;
    }

    /**
     * Similar to the standard date type, but each section after the year is optional. e.g. 2014-06-29 or 2023-04
     * 
     */
    @JsonProperty("startDate")
    public String getStartDate() {
        return startDate;
    }

    /**
     * Similar to the standard date type, but each section after the year is optional. e.g. 2014-06-29 or 2023-04
     * 
     */
    @JsonProperty("startDate")
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    /**
     * Similar to the standard date type, but each section after the year is optional. e.g. 2014-06-29 or 2023-04
     * 
     */
    @JsonProperty("endDate")
    public String getEndDate() {
        return endDate;
    }

    /**
     * Similar to the standard date type, but each section after the year is optional. e.g. 2014-06-29 or 2023-04
     * 
     */
    @JsonProperty("endDate")
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    /**
     * Give an overview of your responsibilities at the company
     * 
     */
    @JsonProperty("summary")
    public String getSummary() {
        return summary;
    }

    /**
     * Give an overview of your responsibilities at the company
     * 
     */
    @JsonProperty("summary")
    public void setSummary(String summary) {
        this.summary = summary;
    }

    /**
     * Specify accomplishments and achievements
     * 
     */
    @JsonProperty("highlights")
    public List<String> getHighlights() {
        return highlights;
    }

    /**
     * Specify accomplishments and achievements
     * 
     */
    @JsonProperty("highlights")
    public void setHighlights(List<String> highlights) {
        this.highlights = highlights;
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
