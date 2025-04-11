
package io.promptics.jobagent.careerdata.model;

import java.net.URI;
import java.util.LinkedHashMap;
import java.util.List;
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
    "name",
    "location",
    "description",
    "position",
    "url",
    "startDate",
    "endDate",
    "summary",
    "highlights"
})

public class Work {

    /**
     * e.g. Facebook
     * 
     */
    @JsonProperty("name")
    @JsonPropertyDescription("e.g. Facebook")
    private String name;
    /**
     * e.g. Menlo Park, CA
     * 
     */
    @JsonProperty("location")
    @JsonPropertyDescription("e.g. Menlo Park, CA")
    private String location;
    /**
     * e.g. Social Media Company
     * 
     */
    @JsonProperty("description")
    @JsonPropertyDescription("e.g. Social Media Company")
    private String description;
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
     * Specify multiple accomplishments
     * 
     */
    @JsonProperty("highlights")
    @JsonPropertyDescription("Specify multiple accomplishments")
    @Valid
    private List<String> highlights;
    @JsonIgnore
    @Valid
    private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();

    /**
     * e.g. Facebook
     * 
     */
    @JsonProperty("name")
    public String getName() {
        return name;
    }

    /**
     * e.g. Facebook
     * 
     */
    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    /**
     * e.g. Menlo Park, CA
     * 
     */
    @JsonProperty("location")
    public String getLocation() {
        return location;
    }

    /**
     * e.g. Menlo Park, CA
     * 
     */
    @JsonProperty("location")
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * e.g. Social Media Company
     * 
     */
    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    /**
     * e.g. Social Media Company
     * 
     */
    @JsonProperty("description")
    public void setDescription(String description) {
        this.description = description;
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
     * Specify multiple accomplishments
     * 
     */
    @JsonProperty("highlights")
    public List<String> getHighlights() {
        return highlights;
    }

    /**
     * Specify multiple accomplishments
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
