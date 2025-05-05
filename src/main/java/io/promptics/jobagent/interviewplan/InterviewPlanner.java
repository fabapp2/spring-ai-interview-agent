package io.promptics.jobagent.interviewplan;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.promptics.jobagent.careerdata.CareerDataService;
import io.promptics.jobagent.careerdata.model.CareerData;
import io.promptics.jobagent.interview.ConversationEntry;
import io.promptics.jobagent.interview.ThreadConversation;
import io.promptics.jobagent.interview.ThreadConversationRepository;
import io.promptics.jobagent.interviewplan.agents.*;
import io.promptics.jobagent.interviewplan.model.TopicThread;
import io.promptics.jobagent.interviewplan.model.Topic;
import io.promptics.jobagent.utils.DateTimeProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Component
public class InterviewPlanner {

    private final ThreadConversationRepository conversationRepository;
    private final TopicRepository topicRepository;
    private final TopicThreadRepository topicThreadRepository;
    private final Map<String, ? extends AbstractSectionAnalyzer> analyzersMap;

    // find prompts at the end...

    public InterviewPlanner(List<? extends AbstractSectionAnalyzer> sectionAnalyzers, ChatClient.Builder chatClientBuilder, CareerDataService careerDataService, ObjectMapper objectMapper, DateTimeProvider datetimeProvider, BasicsTopicPlanningAgent basicsTopicPlanningAgent, BasicsThreadsPlanningAgent basicsThreadsPlanningAgent, WorkTopicsPlanningAgent workTopicsPlanningAgent, ThreadConversationRepository conversationRepository, TopicRepository topicRepository, TopicThreadRepository topicThreadRepository) {
        this.conversationRepository = conversationRepository;
        this.topicRepository = topicRepository;
        this.topicThreadRepository = topicThreadRepository;
        this.analyzersMap = sectionAnalyzers.stream().collect(Collectors.toMap(a -> a.getSectionName(), a -> a));
    }

    /**
     * Find the next active topic and thread.
     */
    public TopicAndThread findCurrentTopicAndThread(String careerDataId) {
        // FIXME: get the next topic with highest priority
        Topic curTopic = topicRepository.findFirstByCareerDataIdOrderByPriorityScoreDesc(careerDataId);
        List<TopicThread> curThread = topicThreadRepository.findByTopicId(curTopic.getId());
        return new TopicAndThread(curTopic, curThread.get(0));
    }

    /**
     * Add a new message to the conversation of a thread.
     */
    public ThreadConversation addToThreadConversation(String threadId, ConversationEntry entry) {
        Optional<ThreadConversation> optionalConversation = conversationRepository.findById(threadId);
        if (optionalConversation.isEmpty()) {
            ThreadConversation threadConversation = new ThreadConversation();
            threadConversation.setThreadId(threadId);
            ThreadConversation saved = conversationRepository.save(threadConversation);
            optionalConversation = Optional.of(saved);
        }
        ThreadConversation conversation = optionalConversation.get();
        conversation.getEntries().add(entry);
        return conversationRepository.save(conversation);
    }


    public TopicAndThread createInitialInterviewPlan(CareerData careerData) {
        analyzersMap.values().parallelStream()
                .forEach(sectionAnalyzer -> {
                    sectionAnalyzer.analyzeSection(careerData);
                });
        return findCurrentTopicAndThread(careerData.getId());
    }

    public List<Topic> adjustPLan(CareerData careerData, List<String> conversationAnalysis) {
        // FIXME: Adjust initial plan,
        return List.of();
    }
}
