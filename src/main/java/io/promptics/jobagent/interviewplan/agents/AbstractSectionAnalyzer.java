package io.promptics.jobagent.interviewplan.agents;

import io.promptics.jobagent.careerdata.model.Basics;
import io.promptics.jobagent.careerdata.model.CareerData;
import io.promptics.jobagent.interviewplan.SectionTopicsAndThreads;
import io.promptics.jobagent.interviewplan.TopicRepository;
import io.promptics.jobagent.interviewplan.TopicThreadRepository;
import io.promptics.jobagent.interviewplan.model.Topic;
import io.promptics.jobagent.interviewplan.model.TopicThread;
import lombok.RequiredArgsConstructor;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

public abstract class AbstractSectionAnalyzer<S> {

    private final AbstractTopicsPlanningAgent<S, Topic> topicPlanningAgent;
    private final AbstractThreadsPlanningAgent threadsPlanningAgent;
    private final TopicRepository topicRepository;
    private final TopicThreadRepository topicThreadRepository;
    private final S type;

    protected AbstractSectionAnalyzer(AbstractTopicsPlanningAgent<S, Topic> topicPlanningAgent, AbstractThreadsPlanningAgent threadsPlanningAgent, TopicRepository topicRepository, TopicThreadRepository topicThreadRepository) {
        this.topicPlanningAgent = topicPlanningAgent;
        this.threadsPlanningAgent = threadsPlanningAgent;
        this.topicRepository = topicRepository;
        this.topicThreadRepository = topicThreadRepository;
        Type superClass = getClass().getGenericSuperclass();
        if (superClass instanceof ParameterizedType parameterizedType) {
            this.type = (S) parameterizedType.getActualTypeArguments()[0];
        } else {
            throw new IllegalStateException("Could not determine generic type S.");
        }
    }

    protected abstract S getSectionData(CareerData careerData);

    public abstract String getSectionName();

    public S getType() {
        return type;
    }

    public SectionTopicsAndThreads analyzeSection(CareerData careerData) {
        S sectionData = getSectionData(careerData);
        List<Topic> topics = topicPlanningAgent.planTopics(careerData.getId(), sectionData);
        topicRepository.saveAll(topics);
        List<TopicThread> topicThreads = threadsPlanningAgent.planThreads(careerData.getId(), sectionData, topics);
        topicThreadRepository.saveAll(topicThreads);
        return new SectionTopicsAndThreads(topics, topicThreads);
    }
}
