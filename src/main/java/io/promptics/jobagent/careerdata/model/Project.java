
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
    "name",
    "description",
    "highlights",
    "keywords",
    "startDate",
    "endDate",
    "url",
    "roles",
    "entity",
    "type"
})

public class Project extends SectionWithId {

    /**
     * e.g. The World Wide Web
     * 
     */
    @JsonProperty("name")
    @JsonPropertyDescription("e.g. The World Wide Web")
    private String name;
    /**
     * Short summary of project. e.g. Collated works of 2017.
     * 
     */
    @JsonProperty("description")
    @JsonPropertyDescription("Short summary of project. e.g. Collated works of 2017.")
    private String description;
    /**
     * Specify multiple features
     * 
     */
    @JsonProperty("highlights")
    @JsonPropertyDescription("Specify multiple features")
    @Valid
    private List<String> highlights;
    /**
     * Specify special elements involved
     * 
     */
    @JsonProperty("keywords")
    @JsonPropertyDescription("Specify special elements involved")
    @Valid
    private List<String> keywords;
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
     * e.g. http://www.computer.org/csdl/mags/co/1996/10/rx069-abs.html
     * 
     */
    @JsonProperty("url")
    @JsonPropertyDescription("e.g. http://www.computer.org/csdl/mags/co/1996/10/rx069-abs.html")
    private URI url;
    /**
     * Specify your role on this project or in company
     * 
     */
    @JsonProperty("roles")
    @JsonPropertyDescription("Specify your role on this project or in company")
    @Valid
    private List<String> roles;
    /**
     * Specify the relevant company/entity affiliations e.g. 'greenpeace', 'corporationXYZ'
     * 
     */
    @JsonProperty("entity")
    @JsonPropertyDescription("Specify the relevant company/entity affiliations e.g. 'greenpeace', 'corporationXYZ'")
    private String entity;
    /**
     *  e.g. 'volunteering', 'presentation', 'talk', 'application', 'conference'
     * 
     */
    @JsonProperty("type")
    @JsonPropertyDescription(" e.g. 'volunteering', 'presentation', 'talk', 'application', 'conference'")
    private String type;
    @JsonIgnore
    @Valid
    private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();

    /**
     * e.g. The World Wide Web
     * 
     */
    @JsonProperty("name")
    public String getName() {
        return name;
    }

    /**
     * e.g. The World Wide Web
     * 
     */
    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Short summary of project. e.g. Collated works of 2017.
     * 
     */
    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    /**
     * Short summary of project. e.g. Collated works of 2017.
     * 
     */
    @JsonProperty("description")
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Specify multiple features
     * 
     */
    @JsonProperty("highlights")
    public List<String> getHighlights() {
        return highlights;
    }

    /**
     * Specify multiple features
     * 
     */
    @JsonProperty("highlights")
    public void setHighlights(List<String> highlights) {
        this.highlights = highlights;
    }

    /**
     * Specify special elements involved
     * 
     */
    @JsonProperty("keywords")
    public List<String> getKeywords() {
        return keywords;
    }

    /**
     * Specify special elements involved
     * 
     */
    @JsonProperty("keywords")
    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
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
     * e.g. http://www.computer.org/csdl/mags/co/1996/10/rx069-abs.html
     * 
     */
    @JsonProperty("url")
    public URI getUrl() {
        return url;
    }

    /**
     * e.g. http://www.computer.org/csdl/mags/co/1996/10/rx069-abs.html
     * 
     */
    @JsonProperty("url")
    public void setUrl(URI url) {
        this.url = url;
    }

    /**
     * Specify your role on this project or in company
     * 
     */
    @JsonProperty("roles")
    public List<String> getRoles() {
        return roles;
    }

    /**
     * Specify your role on this project or in company
     * 
     */
    @JsonProperty("roles")
    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    /**
     * Specify the relevant company/entity affiliations e.g. 'greenpeace', 'corporationXYZ'
     * 
     */
    @JsonProperty("entity")
    public String getEntity() {
        return entity;
    }

    /**
     * Specify the relevant company/entity affiliations e.g. 'greenpeace', 'corporationXYZ'
     * 
     */
    @JsonProperty("entity")
    public void setEntity(String entity) {
        this.entity = entity;
    }

    /**
     *  e.g. 'volunteering', 'presentation', 'talk', 'application', 'conference'
     * 
     */
    @JsonProperty("type")
    public String getType() {
        return type;
    }

    /**
     *  e.g. 'volunteering', 'presentation', 'talk', 'application', 'conference'
     * 
     */
    @JsonProperty("type")
    public void setType(String type) {
        this.type = type;
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
