package io.promptics.jobagent.interviewplan;

import io.promptics.jobagent.interviewplan.model.Topic;
import io.promptics.jobagent.interviewplan.model.TopicThread;
import lombok.Data;
import lombok.Value;

import java.util.List;

@Value
public class TopicAndThread {

    private final Topic topic;
    private final TopicThread thread;

}
