
package io.promptics.jobagent.careerdata.model;

import java.net.URI;
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
    "network",
    "username",
    "url"
})

public class Profile {

    /**
     * e.g. Facebook or Twitter
     * 
     */
    @JsonProperty("network")
    @JsonPropertyDescription("e.g. Facebook or Twitter")
    private String network;
    /**
     * e.g. neutralthoughts
     * 
     */
    @JsonProperty("username")
    @JsonPropertyDescription("e.g. neutralthoughts")
    private String username;
    /**
     * e.g. http://twitter.example.com/neutralthoughts
     * 
     */
    @JsonProperty("url")
    @JsonPropertyDescription("e.g. http://twitter.example.com/neutralthoughts")
    private URI url;
    @JsonIgnore
    @Valid
    private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();

    /**
     * e.g. Facebook or Twitter
     * 
     */
    @JsonProperty("network")
    public String getNetwork() {
        return network;
    }

    /**
     * e.g. Facebook or Twitter
     * 
     */
    @JsonProperty("network")
    public void setNetwork(String network) {
        this.network = network;
    }

    /**
     * e.g. neutralthoughts
     * 
     */
    @JsonProperty("username")
    public String getUsername() {
        return username;
    }

    /**
     * e.g. neutralthoughts
     * 
     */
    @JsonProperty("username")
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * e.g. http://twitter.example.com/neutralthoughts
     * 
     */
    @JsonProperty("url")
    public URI getUrl() {
        return url;
    }

    /**
     * e.g. http://twitter.example.com/neutralthoughts
     * 
     */
    @JsonProperty("url")
    public void setUrl(URI url) {
        this.url = url;
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
