package io.promptics.jobagent.interviewplan;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.promptics.jobagent.interviewplan.model.Topic;
import io.promptics.jobagent.interviewplan.model.TopicThread;
import lombok.Value;

@Value
public class TopicAndThread {

    private final Topic topic;
    private final TopicThread thread;

    @JsonCreator
    public TopicAndThread(@JsonProperty("topic") Topic topic, @JsonProperty("thread") TopicThread thread) {
        this.topic = topic;
        this.thread = thread;
    }
}
