
package io.promptics.jobagent.interviewplan.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.List;


/**
 * Links to specific resume entries or spans (optional).
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "resumeItemId",
    "spans",
    "startDate",
    "endDate",
    "resumeItemBeforeId",
    "resumeItemAfterId"
})
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Reference {

    @JsonProperty("resumeItemId")
    private String resumeItemId;
    @JsonProperty("spans")
    @Valid
    private List<String> spans;
    @JsonProperty("startDate")
    private String startDate;
    @JsonProperty("endDate")
    private String endDate;
    @JsonProperty("resumeItemBeforeId")
    private String resumeItemBeforeId;
    @JsonProperty("resumeItemAfterId")
    private String resumeItemAfterId;

    @JsonProperty("resumeItemId")
    public String getResumeItemId() {
        return resumeItemId;
    }

    @JsonProperty("resumeItemId")
    public void setResumeItemId(String resumeItemId) {
        this.resumeItemId = resumeItemId;
    }

    @JsonProperty("spans")
    public List<String> getSpans() {
        return spans;
    }

    @JsonProperty("spans")
    public void setSpans(List<String> spans) {
        this.spans = spans;
    }

    @JsonProperty("startDate")
    public String getStartDate() {
        return startDate;
    }

    @JsonProperty("startDate")
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    @JsonProperty("endDate")
    public String getEndDate() {
        return endDate;
    }

    @JsonProperty("endDate")
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    @JsonProperty("resumeItemBeforeId")
    public String getResumeItemBeforeId() {
        return resumeItemBeforeId;
    }

    @JsonProperty("resumeItemBeforeId")
    public void setResumeItemBeforeId(String resumeItemBeforeId) {
        this.resumeItemBeforeId = resumeItemBeforeId;
    }

    @JsonProperty("resumeItemAfterId")
    public String getResumeItemAfterId() {
        return resumeItemAfterId;
    }

    @JsonProperty("resumeItemAfterId")
    public void setResumeItemAfterId(String resumeItemAfterId) {
        this.resumeItemAfterId = resumeItemAfterId;
    }

}
