package io.promptics.jobagent;

import io.promptics.jobagent.interview.Interviewer;
import io.promptics.jobagent.interviewplan.InterviewPlanner;
import io.promptics.jobagent.verification.DataVerifier;
import org.springframework.stereotype.Service;

@Service
public class CareerDataInterview {

    private final PreProcessor preProcessor;
    private final InterviewPlanner interviewPlanner;
    private final Interviewer interviewer;
    private final DataVerifier verifier;
    private final InterviewContextHolder contextHolder;

    public CareerDataInterview(PreProcessor preProcessor, InterviewPlanner interviewPlanner, Interviewer interviewer, DataVerifier verifier, InterviewContextHolder contextHolder) {
        this.preProcessor = preProcessor;
        this.interviewPlanner = interviewPlanner;
        this.interviewer = interviewer;
        this.verifier = verifier;
        this.contextHolder = contextHolder;
    }

    public String message(String userMessage) {
        // TODO: context from "session"
        InterviewContext context = new InterviewContext(null, null, null, null);

        // pre processing
        MessageAnalysis analysis = preProcessor.execute(userMessage);
        String message = analysis.getCorrectedMessage();
        String response = "Sorry, didn't get you.";

        if (analysis.getIntent() == MessageAnalysis.Intent.VERIFICATION) {
            verifier.execute(message, context.getCareerDataId());
        } else if(analysis.getIntent() == MessageAnalysis.Intent.QNA) {
            response = interviewer.execute(context, message);
        }

        return response;
    }

    public String start() {
        InterviewContext context = contextHolder.getContext();
        String careerDataId = context.getCareerDataId();
        String sessionId = context.getSessionId();
        interviewPlanner.execute(context);
        interviewer.execute(context, "Please start the interview");
        return null;
    }
}
