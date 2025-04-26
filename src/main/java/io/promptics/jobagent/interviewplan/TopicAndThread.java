package io.promptics.jobagent.interviewplan;

import lombok.Data;

@Data
@Deprecated
public class TopicAndThread {

    private TopicDep topic;
    private Thread thread;

}
