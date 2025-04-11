package io.promptics.jopbagent;

import lombok.Builder;
import lombok.Singular;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Builder
@Document(collection = "interview_plan")
public class InterviewPlan {
    @Id
    private String id;

    @Singular
    private List<InterviewTopic> topics = new ArrayList<>();
}
