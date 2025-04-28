package io.promptics.jobagent;

import io.promptics.jobagent.interview.Interviewer;
import io.promptics.jobagent.interviewplan.InterviewPlanner;
import io.promptics.jobagent.verification.DataVerifier;
import org.springframework.stereotype.Service;

@Service
public class CareerDataInterviewEngine {

    private final PreProcessor preProcessor;
    private final InterviewPlanner interviewPlanner;
    private final Interviewer interviewer;
    private final DataVerifier verifier;
    private final InterviewContextHolder contextHolder;

    public CareerDataInterviewEngine(PreProcessor preProcessor, InterviewPlanner interviewPlanner, Interviewer interviewer, DataVerifier verifier, InterviewContextHolder contextHolder) {
        this.preProcessor = preProcessor;
        this.interviewPlanner = interviewPlanner;
        this.interviewer = interviewer;
        this.verifier = verifier;
        this.contextHolder = contextHolder;
    }

    public String message(String userMessage) {
        InterviewContext context = contextHolder.getContext();

        // pre processing
        MessageAnalysis analysis = preProcessor.execute(userMessage);
        String message = analysis.getCorrectedMessage();
        String response = "Sorry, didn't get you.";

        if (analysis.getIntent() == MessageAnalysis.Intent.VERIFICATION) {
            verifier.execute(message, context.getCareerDataId());
        } else if(analysis.getIntent() == MessageAnalysis.Intent.INTERVIEW) {
            response = interviewer.execute(context, message);
        }

        return response;
    }

    public String start() {
        InterviewContext context = contextHolder.getContext();
        interviewPlanner.createPlan(context);
        return interviewer.execute(context, "Please start the interview");
    }
}
