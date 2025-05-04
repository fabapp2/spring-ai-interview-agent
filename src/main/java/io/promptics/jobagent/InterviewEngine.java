package io.promptics.jobagent;

import io.promptics.jobagent.careerdata.CareerDataService;
import io.promptics.jobagent.careerdata.model.CareerData;
import io.promptics.jobagent.interview.ConversationAnalyzer;
import io.promptics.jobagent.interview.Interviewer;
import io.promptics.jobagent.interview.ThreadConversation;
import io.promptics.jobagent.interview.ThreadConversationRepository;
import io.promptics.jobagent.interviewplan.InterviewPlanner;
import io.promptics.jobagent.interviewplan.TopicAndThread;
import io.promptics.jobagent.interviewplan.model.Topic;
import io.promptics.jobagent.verification.DataVerifier;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InterviewEngine {

    private final PreProcessor preProcessor;
    private final InterviewPlanner interviewPlanner;
    private final Interviewer interviewer;
    private final DataVerifier verifier;
    private final InterviewContextHolder contextHolder;
    private final CareerDataService careerDataService;
    private final ConversationAnalyzer conversationAnalyzer;
    // FIXME: Add service for ThreadConversation or more general for ConversationMemory
    private final ThreadConversationRepository conversationRepository;

    public String message(String userMessage) {
        InterviewContext context = contextHolder.getContext();
        String careerDataId = context.getCareerDataId();
        CareerData careerData = careerDataService.loadCareerData(careerDataId);

        // pre processing
        MessageAnalysis analysis = preProcessor.execute(userMessage);
        String message = analysis.getCorrectedMessage();
        String response = "Sorry, didn't get you.";


        if (analysis.getIntent() == MessageAnalysis.Intent.VERIFICATION) {
            verifier.execute(message, context.getCareerDataId());
        } else if(analysis.getIntent() == MessageAnalysis.Intent.INTERVIEW) {
            TopicAndThread topicAndThread = interviewPlanner.findCurrentTopicAndThread(context.getCareerDataId());
            // extract information from message
            ThreadConversation conversation = conversationRepository.findThreadConversationByThreadId(topicAndThread.getThread().getId());
            String s = conversationAnalyzer.analyzeUserInput(topicAndThread, conversation, message);

            // decide to which setions the information belongs
            // update sections with new information

            // update topics/threads for updated sections
            List<Topic> plan = interviewPlanner.adjustPLan(careerData);

            response = interviewer.execute(careerData, topicAndThread, message);
        }

        return response;
    }

    public String start() {
        InterviewContext context = contextHolder.getContext();
        String careerDataId = context.getCareerDataId();
        CareerData careerData = careerDataService.loadCareerData(careerDataId);
        interviewPlanner.createInitialInterviewPlan(careerData);
        TopicAndThread topicAndThread = interviewPlanner.findCurrentTopicAndThread(careerDataId);
        return interviewer.startInterview(careerData, topicAndThread);
    }
}
