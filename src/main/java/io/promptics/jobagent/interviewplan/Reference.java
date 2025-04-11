package io.promptics.jobagent.interviewplan;

import lombok.Builder;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Builder
public class Reference {
    // The section in Resume JSON
    private String section;
    // Describes the sub-section inside section
    private String description;
}