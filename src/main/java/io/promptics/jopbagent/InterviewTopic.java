package io.promptics.jopbagent;

import lombok.Builder;
import lombok.Singular;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Builder
public class InterviewTopic {
    // Reference to section in career data JSON
    private Reference reference;
    @Singular
    private List<InterviewThread> threads;
}
