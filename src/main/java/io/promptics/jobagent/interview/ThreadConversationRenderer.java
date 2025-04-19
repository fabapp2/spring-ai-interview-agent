package io.promptics.jobagent.interview;

import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class ThreadConversationRenderer {

    public String renderConversation(ThreadConversation conversation) {
        return conversation.getEntries().stream()
                .map(e -> e.getRole() + ": " + e.getText() + "\n")
                .collect(Collectors.joining("\n"));
    }

}
