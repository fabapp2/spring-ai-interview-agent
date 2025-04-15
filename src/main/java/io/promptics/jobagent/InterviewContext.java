package io.promptics.jobagent;

import lombok.Getter;

@Getter
public class InterviewContext {
    private final String careerDataId;
    private final String planId;
    private final String sessionId;
    private final String username;

    public InterviewContext(String careerDataId, String planId, String sessionId, String username, String max) {
        this.careerDataId = careerDataId;
        this.planId = planId;
        this.sessionId = sessionId;
        this.username = username;
    }
}
