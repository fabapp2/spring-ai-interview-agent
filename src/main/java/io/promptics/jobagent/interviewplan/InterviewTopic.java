package io.promptics.jobagent.interviewplan;

import lombok.Builder;
import lombok.Singular;

import java.util.List;

@Builder
public class InterviewTopic {
    // Reference to section in career data JSON
    private Reference reference;
    @Singular
    private List<InterviewThread> threads;
}
