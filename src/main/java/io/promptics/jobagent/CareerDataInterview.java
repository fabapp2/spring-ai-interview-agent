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

    public CareerDataInterview(PreProcessor preProcessor, InterviewPlanner interviewPlanner, Interviewer interviewer, DataVerifier verifier) {
        this.preProcessor = preProcessor;
        this.interviewPlanner = interviewPlanner;
        this.interviewer = interviewer;
        this.verifier = verifier;
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
        String careerDataId = ""; // TODO: init mongoDB and provide id here
        String sessionId = "1";
//        InterviewContext context = new InterviewContext(careerDataId, null, sessionId, "Max");
//        InterviewPlan plan = interviewPlanner.execute(context);
//        interviewer.execute()
        return null;
    }
}
