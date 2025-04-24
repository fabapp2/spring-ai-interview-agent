
package io.promptics.jobagent.careerdata.model;

import com.fasterxml.jackson.annotation.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;

import java.net.URI;
import java.util.LinkedHashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "name",
    "publisher",
    "releaseDate",
    "url",
    "summary"
})

public class Publication extends SectionWithId  {

    /**
     * e.g. The World Wide Web
     * 
     */
    @JsonProperty("name")
    @JsonPropertyDescription("e.g. The World Wide Web")
    private String name;
    /**
     * e.g. IEEE, Computer Magazine
     * 
     */
    @JsonProperty("publisher")
    @JsonPropertyDescription("e.g. IEEE, Computer Magazine")
    private String publisher;
    /**
     * Similar to the standard date type, but each section after the year is optional. e.g. 2014-06-29 or 2023-04
     * 
     */
    @JsonProperty("releaseDate")
    @JsonPropertyDescription("Similar to the standard date type, but each section after the year is optional. e.g. 2014-06-29 or 2023-04")
    @Pattern(regexp = "^([1-2][0-9]{3}-[0-1][0-9]-[0-3][0-9]|[1-2][0-9]{3}-[0-1][0-9]|[1-2][0-9]{3})$")
    private String releaseDate;
    /**
     * e.g. http://www.computer.org.example.com/csdl/mags/co/1996/10/rx069-abs.html
     * 
     */
    @JsonProperty("url")
    @JsonPropertyDescription("e.g. http://www.computer.org.example.com/csdl/mags/co/1996/10/rx069-abs.html")
    private URI url;
    /**
     * Short summary of publication. e.g. Discussion of the World Wide Web, HTTP, HTML.
     * 
     */
    @JsonProperty("summary")
    @JsonPropertyDescription("Short summary of publication. e.g. Discussion of the World Wide Web, HTTP, HTML.")
    private String summary;
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
     * e.g. IEEE, Computer Magazine
     * 
     */
    @JsonProperty("publisher")
    public String getPublisher() {
        return publisher;
    }

    /**
     * e.g. IEEE, Computer Magazine
     * 
     */
    @JsonProperty("publisher")
    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    /**
     * Similar to the standard date type, but each section after the year is optional. e.g. 2014-06-29 or 2023-04
     * 
     */
    @JsonProperty("releaseDate")
    public String getReleaseDate() {
        return releaseDate;
    }

    /**
     * Similar to the standard date type, but each section after the year is optional. e.g. 2014-06-29 or 2023-04
     * 
     */
    @JsonProperty("releaseDate")
    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    /**
     * e.g. http://www.computer.org.example.com/csdl/mags/co/1996/10/rx069-abs.html
     * 
     */
    @JsonProperty("url")
    public URI getUrl() {
        return url;
    }

    /**
     * e.g. http://www.computer.org.example.com/csdl/mags/co/1996/10/rx069-abs.html
     * 
     */
    @JsonProperty("url")
    public void setUrl(URI url) {
        this.url = url;
    }

    /**
     * Short summary of publication. e.g. Discussion of the World Wide Web, HTTP, HTML.
     * 
     */
    @JsonProperty("summary")
    public String getSummary() {
        return summary;
    }

    /**
     * Short summary of publication. e.g. Discussion of the World Wide Web, HTTP, HTML.
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
