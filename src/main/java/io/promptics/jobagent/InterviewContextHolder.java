package io.promptics.jobagent;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class InterviewContextHolder {
    private InterviewContext context;
}
