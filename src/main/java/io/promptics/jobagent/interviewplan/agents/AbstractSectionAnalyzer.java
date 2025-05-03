package io.promptics.jobagent.interviewplan.agents;

import io.promptics.jobagent.careerdata.model.Basics;
import io.promptics.jobagent.careerdata.model.CareerData;
import io.promptics.jobagent.interviewplan.SectionTopicsAndThreads;
import io.promptics.jobagent.interviewplan.TopicRepository;
import io.promptics.jobagent.interviewplan.TopicThreadRepository;
import io.promptics.jobagent.interviewplan.model.Topic;
import io.promptics.jobagent.interviewplan.model.TopicThread;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public abstract class AbstractSectionAnalyzer<S> {

    private final AbstractTopicsPlanningAgent<S> topicPlanningAgent;
    private final AbstractThreadsPlanningAgent threadsPlanningAgent;
    private final TopicRepository topicRepository;
    private final TopicThreadRepository topicThreadRepository;

    protected abstract S getSectionData(CareerData careerData);

    public SectionTopicsAndThreads analyzeSection(CareerData careerData) {
        S sectionData = getSectionData(careerData);
        List<Topic> topics = topicPlanningAgent.planTopics(careerData.getId(), sectionData);
        topicRepository.saveAll(topics);
        List<TopicThread> topicThreads = threadsPlanningAgent.planThreads(sectionData, topics);
        topicThreadRepository.saveAll(topicThreads);
        return new SectionTopicsAndThreads(topics, topicThreads);
    }
}
