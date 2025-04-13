package io.promptics.jobagent.interview;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class InterviewerTest {

    @Autowired
    Interviewer interviewer;

    @Test
    @DisplayName("start interview")
    void startInterview() {
        String um1 = "start the interview";
        String response = interviewer.run(um1);
        user(um1);
        assistant(response);
        String um2 = "I am unemployed since then.";
        String response2 = interviewer.run(um2);
        user(um2);
        assistant(response2);
    }

    private void assistant(String message) {
        System.out.println("Assistant:\n  %s\n".formatted(message));
    }

    private void user(String message) {
        System.out.println("User:\n  %s\n".formatted(message));
    }
}