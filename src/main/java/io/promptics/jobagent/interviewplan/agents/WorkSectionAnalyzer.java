package io.promptics.jobagent.interviewplan.agents;

import io.promptics.jobagent.careerdata.model.CareerData;
import io.promptics.jobagent.careerdata.model.Work;
import io.promptics.jobagent.interviewplan.TopicRepository;
import io.promptics.jobagent.interviewplan.TopicThreadRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class WorkSectionAnalyzer extends AbstractSectionAnalyzer<List<Work>> {

    public WorkSectionAnalyzer(WorkTopicsPlanningAgent topicPlanningAgent, WorkThreadsPlanningAgent threadsPlanningAgent, TopicRepository topicRepository, TopicThreadRepository topicThreadRepository) {
        super(topicPlanningAgent, threadsPlanningAgent, topicRepository, topicThreadRepository);
    }

    @Override
    protected List<Work> getSectionData(CareerData careerData) {
        return careerData.getWork();
    }

}
