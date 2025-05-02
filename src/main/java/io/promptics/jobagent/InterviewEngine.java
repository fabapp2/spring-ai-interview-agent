package io.promptics.jobagent;

import io.promptics.jobagent.careerdata.CareerDataService;
import io.promptics.jobagent.careerdata.model.CareerData;
import io.promptics.jobagent.interview.Interviewer;
import io.promptics.jobagent.interviewplan.InterviewPlanner;
import io.promptics.jobagent.interviewplan.model.Topic;
import io.promptics.jobagent.verification.DataVerifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InterviewEngine {

    private final PreProcessor preProcessor;
    private final InterviewPlanner interviewPlanner;
    private final Interviewer interviewer;
    private final DataVerifier verifier;
    private final InterviewContextHolder contextHolder;
    private final CareerDataService careerDataService;

    public InterviewEngine(PreProcessor preProcessor, InterviewPlanner interviewPlanner, Interviewer interviewer, DataVerifier verifier, InterviewContextHolder contextHolder, CareerDataService careerDataService) {
        this.preProcessor = preProcessor;
        this.interviewPlanner = interviewPlanner;
        this.interviewer = interviewer;
        this.verifier = verifier;
        this.contextHolder = contextHolder;
        this.careerDataService = careerDataService;
    }

    public String message(String userMessage) {
        InterviewContext context = contextHolder.getContext();
        String careerDataId = context.getCareerDataId();
        CareerData careerData = careerDataService.getById(careerDataId);

        // pre processing
        MessageAnalysis analysis = preProcessor.execute(userMessage);
        String message = analysis.getCorrectedMessage();
        String response = "Sorry, didn't get you.";

        if (analysis.getIntent() == MessageAnalysis.Intent.VERIFICATION) {
            verifier.execute(message, context.getCareerDataId());
        } else if(analysis.getIntent() == MessageAnalysis.Intent.INTERVIEW) {
            List<Topic> plan = interviewPlanner.adjustPLan(careerData);
            response = interviewer.execute(careerData, plan, message);
        }

        return response;
    }

    public String start() {
        InterviewContext context = contextHolder.getContext();
        String careerDataId = context.getCareerDataId();
        CareerData careerData = careerDataService.getById(careerDataId);
        List<Topic> initialInterviewPlan = interviewPlanner.createInitialInterviewPlan(careerData);
        return interviewer.startInterview(careerData, initialInterviewPlan);
    }
}
