package io.promptics.jobagent.interviewplan;

import org.springframework.stereotype.Component;

@Component
public class InterviewPlanMongoDbTools {

    private final InterviewPlanMongoDbService interviewPlanMongoDbService;

    public InterviewPlanMongoDbTools(InterviewPlanMongoDbService interviewPlanMongoDbService) {
        this.interviewPlanMongoDbService = interviewPlanMongoDbService;
    }

}
