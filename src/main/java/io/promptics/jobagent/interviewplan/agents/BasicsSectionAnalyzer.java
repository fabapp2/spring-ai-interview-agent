package io.promptics.jobagent.interviewplan.agents;

import io.promptics.jobagent.careerdata.model.Basics;
import io.promptics.jobagent.careerdata.model.CareerData;
import io.promptics.jobagent.interviewplan.TopicRepository;
import io.promptics.jobagent.interviewplan.TopicThreadRepository;
import org.springframework.stereotype.Component;

@Component
public class BasicsSectionAnalyzer extends AbstractSectionAnalyzer<Basics> {

    public BasicsSectionAnalyzer(BasicsTopicPlanningAgent topicPlanningAgent, BasicsThreadsPlanningAgent threadsPlanningAgent, TopicRepository topicRepository, TopicThreadRepository topicThreadRepository) {
        super(topicPlanningAgent, threadsPlanningAgent, topicRepository, topicThreadRepository);
    }

    protected Basics getSectionData(CareerData careerData) {
        Basics section = careerData.getBasics();
        return section;
    }

}
