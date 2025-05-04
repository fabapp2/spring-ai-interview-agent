package io.promptics.jobagent.interviewplan.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Singular;

import java.util.List;


/**
 * Links to specific resume entries or spans (optional).
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "resumeItemIds",
    "startDate",
    "endDate"
})
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Reference {

    @JsonProperty("resumeItemIds")
    @JsonPropertyDescription("List of resume item IDs this topic relates to")
    @Valid
    @Singular
    private List<String> resumeItemIds;

    @JsonProperty("startDate")
    @JsonPropertyDescription("Start of time period, format YYYY or YYYY-MM")
    private String startDate;

    @JsonProperty("endDate")
    @JsonPropertyDescription("End of time period, format YYYY or YYYY-MM")
    private String endDate;

    @JsonProperty("resumeItemIds")
    public List<String> getResumeItemIds() {
        return resumeItemIds;
    }

    @JsonProperty("resumeItemIds")
    public void setResumeItemIds(List<String> resumeItemIds) {
        this.resumeItemIds = resumeItemIds;
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

}
