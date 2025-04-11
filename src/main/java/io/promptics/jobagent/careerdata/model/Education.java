
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
    "institution",
    "url",
    "area",
    "studyType",
    "startDate",
    "endDate",
    "score",
    "courses"
})

public class Education {

    /**
     * e.g. Massachusetts Institute of Technology
     * 
     */
    @JsonProperty("institution")
    @JsonPropertyDescription("e.g. Massachusetts Institute of Technology")
    private String institution;
    /**
     * e.g. http://facebook.example.com
     * 
     */
    @JsonProperty("url")
    @JsonPropertyDescription("e.g. http://facebook.example.com")
    private URI url;
    /**
     * e.g. Arts
     * 
     */
    @JsonProperty("area")
    @JsonPropertyDescription("e.g. Arts")
    private String area;
    /**
     * e.g. Bachelor
     * 
     */
    @JsonProperty("studyType")
    @JsonPropertyDescription("e.g. Bachelor")
    private String studyType;
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
     * grade point average, e.g. 3.67/4.0
     * 
     */
    @JsonProperty("score")
    @JsonPropertyDescription("grade point average, e.g. 3.67/4.0")
    private String score;
    /**
     * List notable courses/subjects
     * 
     */
    @JsonProperty("courses")
    @JsonPropertyDescription("List notable courses/subjects")
    @Valid
    private List<String> courses;
    @JsonIgnore
    @Valid
    private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();

    /**
     * e.g. Massachusetts Institute of Technology
     * 
     */
    @JsonProperty("institution")
    public String getInstitution() {
        return institution;
    }

    /**
     * e.g. Massachusetts Institute of Technology
     * 
     */
    @JsonProperty("institution")
    public void setInstitution(String institution) {
        this.institution = institution;
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
     * e.g. Arts
     * 
     */
    @JsonProperty("area")
    public String getArea() {
        return area;
    }

    /**
     * e.g. Arts
     * 
     */
    @JsonProperty("area")
    public void setArea(String area) {
        this.area = area;
    }

    /**
     * e.g. Bachelor
     * 
     */
    @JsonProperty("studyType")
    public String getStudyType() {
        return studyType;
    }

    /**
     * e.g. Bachelor
     * 
     */
    @JsonProperty("studyType")
    public void setStudyType(String studyType) {
        this.studyType = studyType;
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
     * grade point average, e.g. 3.67/4.0
     * 
     */
    @JsonProperty("score")
    public String getScore() {
        return score;
    }

    /**
     * grade point average, e.g. 3.67/4.0
     * 
     */
    @JsonProperty("score")
    public void setScore(String score) {
        this.score = score;
    }

    /**
     * List notable courses/subjects
     * 
     */
    @JsonProperty("courses")
    public List<String> getCourses() {
        return courses;
    }

    /**
     * List notable courses/subjects
     * 
     */
    @JsonProperty("courses")
    public void setCourses(List<String> courses) {
        this.courses = courses;
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
