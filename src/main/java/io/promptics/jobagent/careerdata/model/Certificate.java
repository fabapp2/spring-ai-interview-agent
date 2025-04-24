
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
    "date",
    "url",
    "issuer"
})
public class Certificate extends SectionWithId  {

    /**
     * e.g. Certified Kubernetes Administrator
     * 
     */
    @JsonProperty("name")
    @JsonPropertyDescription("e.g. Certified Kubernetes Administrator")
    private String name;
    /**
     * Similar to the standard date type, but each section after the year is optional. e.g. 2014-06-29 or 2023-04
     * 
     */
    @JsonProperty("date")
    @JsonPropertyDescription("Similar to the standard date type, but each section after the year is optional. e.g. 2014-06-29 or 2023-04")
    @Pattern(regexp = "^([1-2][0-9]{3}-[0-1][0-9]-[0-3][0-9]|[1-2][0-9]{3}-[0-1][0-9]|[1-2][0-9]{3})$")
    private String date;
    /**
     * e.g. http://example.com
     * 
     */
    @JsonProperty("url")
    @JsonPropertyDescription("e.g. http://example.com")
    private URI url;
    /**
     * e.g. CNCF
     * 
     */
    @JsonProperty("issuer")
    @JsonPropertyDescription("e.g. CNCF")
    private String issuer;
    @JsonIgnore
    @Valid
    private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();

    /**
     * e.g. Certified Kubernetes Administrator
     * 
     */
    @JsonProperty("name")
    public String getName() {
        return name;
    }

    /**
     * e.g. Certified Kubernetes Administrator
     * 
     */
    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
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
     * e.g. http://example.com
     * 
     */
    @JsonProperty("url")
    public URI getUrl() {
        return url;
    }

    /**
     * e.g. http://example.com
     * 
     */
    @JsonProperty("url")
    public void setUrl(URI url) {
        this.url = url;
    }

    /**
     * e.g. CNCF
     * 
     */
    @JsonProperty("issuer")
    public String getIssuer() {
        return issuer;
    }

    /**
     * e.g. CNCF
     * 
     */
    @JsonProperty("issuer")
    public void setIssuer(String issuer) {
        this.issuer = issuer;
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
