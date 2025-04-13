package io.promptics.jobagent.interview;

import io.promptics.jobagent.InterviewContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

@SpringBootTest
class InterviewerTest {

    @Autowired
    Interviewer interviewer;
    private String careerDataId;

    @Test
    @DisplayName("start interview")
    void startInterview() {
        InterviewContext context = new InterviewContext(careerDataId, "userid", "sessionid", "Max");

//        String um1 = "start the interview";
//        String response = interviewer.run(um1);
//        user(um1);
//        assistant(response);
//
//        String um2 = "I am unemployed since then.";
//        String response2 = interviewer.run(um2);
//        user(um2);
//        assistant(response2);
//
//        String um3 = "I learned abut AI, LLMs andagentic systems implementing different toy projects.";
//        String response3 = interviewer.run(um2);
//        user(um3);
//        assistant(response3);
//
//        String um4 = "Why is this relevant?";
//        String response4 = interviewer.run(um4);
//        user(um4);
//        assistant(response4);
        Map<String, String> memory = interviewer.getMemory();
        memory.put("user", "start the interview");
        memory.put("assistant", "Thank you for participating in this interview. To begin, can you please share your current employment status since leaving TechGiant in December 2024?");
        memory.put("user", "I am unemployed since then.");
        memory.put("assistant", "Thank you for sharing that. Can you tell me what activities or projects you have been involved in during your time since leaving TechGiant?");
        memory.put("user", "I learned abut AI, LLMs and agentic systems implementing different toy projects.");
        memory.put("assistant", "Thank you for sharing that. Can you tell me how you have been spending your time since December 2024? Have you been looking for new job opportunities or engaging in any other activities?");
        memory.put("user", "Why is this relevant?");
        memory.put("assistant", "Understanding your current employment status and activities since December 2024 helps us gain insight into your career journey and any factors that may have influenced your employment gap. It allows us to better understand your skills, experiences, and how you are navigating your career path. With that in mind, can you share what you have been doing since December 2024?");

//        String um5 = "I spent time working on different projects usingh AI codegeneration tools like OpenHands and Claude code. I built a job search tool with a set of AI workflows to gather career data and enrich my CV data through an interview. Another workflow readds job offers from linkedin and filters them using the cv data. THius project is still ongoiing. Additionally, I did a AWS certification about AI.";
//        String response5 = interviewer.run(um5);
//        user(um5);
//        assistant(response5);
        memory.put("user", "  I spent time working on different projects using AI codegeneration tools like OpenHands and Claude code. I built a job search tool with a set of AI workflows to gather career data and enrich my CV data through an interview. Another workflow reads job offers from linkedin and filters them using the cv data. THius project is still ongoiing. Additionally, I did a AWS certification about AI.");
        memory.put("assistant", "That sounds like a productive use of your time! Can you tell me more about the job search tool you built? What specific features does it have, and how do you envision it helping users in their job search?");

        String um6 = "I spent time working on different projects using AI codegeneration tools like OpenHands and Claude code. I built a job search tool with a set of AI workflows to gather career data and enrich my CV data through an interview. Another workflow readds job offers from linkedin and filters them using the cv data. THius project is still ongoiing. Additionally, I did a AWS certification about AI.";
        String response6 = interviewer.run(context, um6);
        user(um6);
        assistant(response6);
    }

    private void assistant(String message) {
        System.out.println("Assistant:\n  %s\n".formatted(message));
    }

    private void user(String message) {
        System.out.println("User:\n  %s\n".formatted(message));
    }
}