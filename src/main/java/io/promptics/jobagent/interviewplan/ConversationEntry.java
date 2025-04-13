package io.promptics.jobagent.interviewplan;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConversationEntry {
    @JsonProperty("timestamp")
    private String timestamp;  // ISO-8601 format
    
    @JsonProperty("question")
    private String question;
    
    @JsonProperty("response")
    private String response;
}
