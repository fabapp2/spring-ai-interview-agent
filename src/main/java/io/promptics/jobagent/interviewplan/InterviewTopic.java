package io.promptics.jobagent.interviewplan;

import lombok.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InterviewTopic {

    private String id;

    // Reference to section in career data JSON
    private Reference reference;

    @Singular
    private List<InterviewThread> threads;
}
