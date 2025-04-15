package io.promptics.jobagent.interviewplan;

import org.springframework.stereotype.Component;

@Component
public class InterviewPlanMongoTools {

    private final InterviewPlanService interviewPlanService;

    public InterviewPlanMongoTools(InterviewPlanService interviewPlanService) {
        this.interviewPlanService = interviewPlanService;
    }

}
