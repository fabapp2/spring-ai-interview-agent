
package io.promptics.jobagent.interviewplan;

import com.fasterxml.jackson.annotation.*;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 * Structure for planning career data gathering interviews
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "topics"
})
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document("interview_plan")
public class InterviewPlan {

    @Id
    private String id;

    private String careerDataId;

    @JsonProperty("topics")
    @Singular
    public List<Topic> topics = new ArrayList<Topic>();

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
