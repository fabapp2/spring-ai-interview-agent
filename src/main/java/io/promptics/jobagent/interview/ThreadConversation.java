package io.promptics.jobagent.interview;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Document("conversations")
public class ThreadConversation {
    @Id
    private String id;
    private String planId;
    private String threadId;
    private Date lastUpdated;
    private List<ConversationEntry> entries = new ArrayList<>();
}