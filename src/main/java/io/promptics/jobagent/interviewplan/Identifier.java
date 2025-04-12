
package io.promptics.jobagent.interviewplan;

import com.fasterxml.jackson.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedHashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "name",
    "startDate",
    "date"
})
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Identifier {

    @JsonProperty("name")
    public String name;
    @JsonProperty("startDate")
    public String startDate;
    @JsonProperty("date")
    public String date;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
