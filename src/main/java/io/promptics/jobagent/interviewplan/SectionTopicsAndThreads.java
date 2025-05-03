package io.promptics.jobagent.interviewplan;

import io.promptics.jobagent.interviewplan.model.Topic;
import io.promptics.jobagent.interviewplan.model.TopicThread;
import lombok.Value;

import java.util.List;

@Value
public class SectionTopicsAndThreads {

    private final List<Topic> topics;
    private final List<TopicThread> threads;

}
