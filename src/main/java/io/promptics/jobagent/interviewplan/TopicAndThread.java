package io.promptics.jobagent.interviewplan;

import io.promptics.jobagent.interview.ConversationEntry;
import lombok.Data;

@Data
public class TopicAndThread {

    private Topic topic;
    private Thread thread;

    public String getThreadId() {
        return thread.getIdentifier();
    }

    public String render() {
        StringBuilder md = new StringBuilder();

        // Topic Section
        md.append("# Current Topic\n");
        md.append("- ID: ").append(topic.getIdentifier()).append("\n");
        md.append("- Type: ").append(topic.getType()).append("\n");
        md.append("- Reference: ").append(topic.getReference().getSection());
        if (topic.getReference().getIdentifier() != null) {
            md.append(" (").append(topic.getReference().getIdentifier().getName());
            if (topic.getReference().getIdentifier().getStartDate() != null) {
                md.append(", ").append(topic.getReference().getIdentifier().getStartDate());
            }
            md.append(")");
        }
        md.append("\n\n");

        // Thread Section
        md.append("# Active Thread\n");
        md.append("- ID: ").append(thread.getIdentifier()).append("\n");
        md.append("- Type: ").append(thread.getType()).append("\n");
        md.append("- Focus: ").append(thread.getFocus()).append("\n");
        md.append("- Status: ").append(thread.getStatus()).append("\n\n");

        // Conversation History
        if (thread.getConversation() != null && !thread.getConversation().isEmpty()) {
            md.append("# Conversation History\n");
            for (ConversationEntry entry : thread.getConversation()) {
                md.append("## ").append(entry.getTimestamp()).append("\n");
                md.append(entry.getRole().equals("assistant") ? "Assistant: " : "User: ")
                        .append(entry.getText()).append("\n\n");
            }
        }

        return md.toString();
    }

    public String renderFake() {
        return """
            # Current Topic: Employment Gap
            Type: gap
            Focus: Current employment status
            
            ## Reference
            - Company: TechGiant
            - Last Position: Senior Developer
            - Period: January 2024 - December 2024
            
            ## Active Thread
            ID: current_status
            Type: core_details
            Focus: Determine current employment status and activities since December 2024
            
            ## Relevant Context
            Last Known Position:
            - Company: TechGiant
            - Role: Senior Developer
            - Key Achievements:
              * Led a team of developers
              * Architected scalable solutions
              * Reduced system downtime by 30%
            
            Gap Period:
            - From: December 2024
            - To: Present (April 2024)
            - Duration: 4 months
            
            ## Previous Conversation
            {memory}
            
            ## Current Date
            2025-04-12
            """;
    }
}
