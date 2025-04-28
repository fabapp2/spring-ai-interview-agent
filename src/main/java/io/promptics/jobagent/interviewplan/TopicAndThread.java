package io.promptics.jobagent.interviewplan;

import io.promptics.jobagent.interviewplan.model.Topic;
import io.promptics.jobagent.interviewplan.model.TopicThread;
import lombok.Data;

@Data
@Deprecated
public class TopicAndThread {

    private Topic topic;
    private TopicThread thread;

}
