package io.promptics.jobagent.interview;

import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class ConversationAnalysis {
    private String originalMessage;
    private List<ExtractedInformation> extractedInformations;
}
