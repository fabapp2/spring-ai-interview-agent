package io.promptics.jobagent;

import io.promptics.jobagent.careerdata.CareerDataManager;
import io.promptics.jobagent.careerdata.CareerDataService;
import io.promptics.jobagent.careerdata.model.CareerData;
import io.promptics.jobagent.interview.*;
import io.promptics.jobagent.interviewplan.InterviewPlanner;
import io.promptics.jobagent.interviewplan.TopicAndThread;
import io.promptics.jobagent.interviewplan.model.Topic;
import io.promptics.jobagent.verification.DataVerifier;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InterviewEngine {

    private final ChatMemory chatMemory;
    private final PreProcessor preProcessor;
    private final InterviewPlanner interviewPlanner;
    private final Interviewer interviewer;
    private final DataVerifier verifier;
    private final InterviewContextHolder contextHolder;
    private final CareerDataService careerDataService;
    private final ConversationAnalyzer conversationAnalyzer;
    // FIXME: Add service for ThreadConversation or more general for ConversationMemory
    private final ThreadConversationRepository conversationRepository;
    private final CareerDataManager careerDataManager;

    public String message(String userMessage) {
        chatMemory.add(ChatMemory.DEFAULT_CONVERSATION_ID, UserMessage.builder().text(userMessage).build());
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
            Optional<ThreadConversation> optionalConversation = conversationRepository.findThreadConversationByThreadId(topicAndThread.getThread().getId());
            ThreadConversation conversation = optionalConversation.orElse(conversationRepository.save(new ThreadConversation()));
            ConversationAnalysis conversationAnalysis = conversationAnalyzer.analyzeUserInput(topicAndThread, conversation, message);
            // decide to which setions the information belongs
            conversationAnalysis.getExtractedInformations().stream()
                    .forEach(info -> {
                        String section = info.getSection();
                        info.getInformations().forEach(i -> {
                            String prompt = "Update the %s section with this information: %s".formatted(section, i);
                            careerDataManager.run(prompt, careerDataId);
                        });
                        // TODO: decide who closes topics/thread
                    });

            // update topics/threads for updated sections
            List<String> sections = conversationAnalysis.getExtractedInformations().stream().map(info -> info.getSection()).collect(Collectors.toList());
            List<Topic> plan = interviewPlanner.adjustPLan(careerData, sections);

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
        String response = interviewer.startInterview(careerData, topicAndThread);
        chatMemory.add(ChatMemory.DEFAULT_CONVERSATION_ID, SystemMessage.builder().text(response).build());
        return response;
    }
}
