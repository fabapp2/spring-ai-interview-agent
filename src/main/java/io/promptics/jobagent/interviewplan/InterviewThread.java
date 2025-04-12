package io.promptics.jobagent.interviewplan;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InterviewThread {

    private Type type;

    // The focus of this thread, e.g. "Confirm personal contact information and validate email"
    private String focus;

    private Status status = Status.PENDING;

    // The time planned in seconds
    private int timePlanned;

    // The time spent in seconds
    private int timeSpent;

    // the fields in the topic's referenced section
    @Singular
    private List<String> fields = new ArrayList<>();

    public enum Status {
        PENDING,
        ACTIVE,
        SUSPENDED,
        COMPLETE;
    }

    public enum Type {
        CORE_DETAILS,
        ACHIEVEMENTS,
        SKILLS,
        TEAM_CONTEXT,
        CHALLENGES,
        TRANSITION,
        FILL_GAP
    }
}
