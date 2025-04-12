package io.promptics.jobagent.interviewplan;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "interview_plan")
public class InterviewPlan {

    @Id
    @JsonProperty("career_data_id")
    private String careerDataId;

    @Singular
    private List<InterviewTopic> topics = new ArrayList<>();
}
