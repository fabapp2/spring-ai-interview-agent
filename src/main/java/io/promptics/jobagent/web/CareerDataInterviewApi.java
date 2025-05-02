package io.promptics.jobagent.web;

import io.promptics.jobagent.InterviewEngine;
import lombok.Data;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/career-interview")
public class CareerDataInterviewApi {

    private final InterviewEngine careerDataInterview;

    public CareerDataInterviewApi(InterviewEngine careerDataInterview) {
        this.careerDataInterview = careerDataInterview;
    }

    @GetMapping("/start")
    public String startInterview() {
        return careerDataInterview.start();
    }

    @PostMapping("/message")
    public String message(@RequestBody UserReply userMessage) {
        return careerDataInterview.message(userMessage.getMessage());
    }

    @Data
    public static class UserReply {
        private String message;
    }
}
