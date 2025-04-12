package io.promptics.jobagent;

import lombok.Getter;

@Getter
public class InterviewContext {
    private final String careerDataId;
    private final String userId;
    private final String sessionId;
    private final String username;

    public InterviewContext(String careerDataId, String userId, String sessionId, String username) {
        this.careerDataId = careerDataId;
        this.userId = userId;
        this.sessionId = sessionId;
        this.username = username;
    }
}
