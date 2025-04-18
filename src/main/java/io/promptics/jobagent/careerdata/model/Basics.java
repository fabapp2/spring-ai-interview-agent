
package io.promptics.jobagent.careerdata.model;

import com.fasterxml.jackson.annotation.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;

import java.net.URI;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "name",
    "label",
    "image",
    "email",
    "phone",
    "url",
    "summary",
    "location",
    "profiles"
})

public class Basics {

    @JsonProperty("name")
    private String name;
    /**
     * e.g. Web Developer
     * 
     */
    @JsonProperty("label")
    @JsonPropertyDescription("e.g. Web Developer")
    private String label;
    /**
     * URL (as per RFC 3986) to a image in JPEG or PNG format
     * 
     */
    @JsonProperty("image")
    @JsonPropertyDescription("URL (as per RFC 3986) to a image in JPEG or PNG format")
    private String image;
    /**
     * e.g. thomas@gmail.com
     * 
     */
    @Email
    @JsonProperty("email")
    @JsonPropertyDescription("e.g. thomas@gmail.com")
    private String email;
    /**
     * Phone numbers are stored as strings so use any format you like, e.g. 712-117-2923
     * 
     */
    @JsonProperty("phone")
    @JsonPropertyDescription("Phone numbers are stored as strings so use any format you like, e.g. 712-117-2923")
    private String phone;
    /**
     * URL (as per RFC 3986) to your website, e.g. personal homepage
     * 
     */
    @JsonProperty("url")
    @JsonPropertyDescription("URL (as per RFC 3986) to your website, e.g. personal homepage")
    private URI url;
    /**
     * Write a short 2-3 sentence biography about yourself
     * 
     */
    @JsonProperty("summary")
    @JsonPropertyDescription("Write a short 2-3 sentence biography about yourself")
    private String summary;
    @JsonProperty("location")
    @Valid
    private Location location;
    /**
     * Specify any number of social networks that you participate in
     * 
     */
    @JsonProperty("profiles")
    @JsonPropertyDescription("Specify any number of social networks that you participate in")
    @Valid
    private List<Profile> profiles;
    @JsonIgnore
    @Valid
    private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    /**
     * e.g. Web Developer
     * 
     */
    @JsonProperty("label")
    public String getLabel() {
        return label;
    }

    /**
     * e.g. Web Developer
     * 
     */
    @JsonProperty("label")
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * URL (as per RFC 3986) to a image in JPEG or PNG format
     * 
     */
    @JsonProperty("image")
    public String getImage() {
        return image;
    }

    /**
     * URL (as per RFC 3986) to a image in JPEG or PNG format
     * 
     */
    @JsonProperty("image")
    public void setImage(String image) {
        this.image = image;
    }

    /**
     * e.g. thomas@gmail.com
     * 
     */
    @JsonProperty("email")
    public String getEmail() {
        return email;
    }

    /**
     * e.g. thomas@gmail.com
     * 
     */
    @JsonProperty("email")
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Phone numbers are stored as strings so use any format you like, e.g. 712-117-2923
     * 
     */
    @JsonProperty("phone")
    public String getPhone() {
        return phone;
    }

    /**
     * Phone numbers are stored as strings so use any format you like, e.g. 712-117-2923
     * 
     */
    @JsonProperty("phone")
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * URL (as per RFC 3986) to your website, e.g. personal homepage
     * 
     */
    @JsonProperty("url")
    public URI getUrl() {
        return url;
    }

    /**
     * URL (as per RFC 3986) to your website, e.g. personal homepage
     * 
     */
    @JsonProperty("url")
    public void setUrl(URI url) {
        this.url = url;
    }

    /**
     * Write a short 2-3 sentence biography about yourself
     * 
     */
    @JsonProperty("summary")
    public String getSummary() {
        return summary;
    }

    /**
     * Write a short 2-3 sentence biography about yourself
     * 
     */
    @JsonProperty("summary")
    public void setSummary(String summary) {
        this.summary = summary;
    }

    @JsonProperty("location")
    public Location getLocation() {
        return location;
    }

    @JsonProperty("location")
    public void setLocation(Location location) {
        this.location = location;
    }

    /**
     * Specify any number of social networks that you participate in
     * 
     */
    @JsonProperty("profiles")
    public List<Profile> getProfiles() {
        return profiles;
    }

    /**
     * Specify any number of social networks that you participate in
     * 
     */
    @JsonProperty("profiles")
    public void setProfiles(List<Profile> profiles) {
        this.profiles = profiles;
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
