package io.promptics.jobagent.interviewplan;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConversationEntry {
    // ISO-8601 format
    @JsonProperty("timestamp")
    private String timestamp = Instant.now().toString();

    @JsonProperty("role")
    private String role;

    @JsonProperty("text")
    private String text;

}
