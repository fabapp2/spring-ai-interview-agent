package io.promptics.jobagent.web;

import io.promptics.jobagent.CareerDataInterview;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/career-interview")
public class CareerDataInterviewApi {

    private final CareerDataInterview careerDataInterview;

    public CareerDataInterviewApi(CareerDataInterview careerDataInterview) {
        this.careerDataInterview = careerDataInterview;
    }

    @RequestMapping("/start")
    public String startInterview() {
        return careerDataInterview.start();
    }

    @RequestMapping("/message")
    public String message(String userMessage) {
        return careerDataInterview.message(userMessage);
    }
}
