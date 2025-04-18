package io.promptics.jobagent.interview;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Document("thread_conversation")
public class ThreadConversation {
    @Id
    private String id;
    private String threadId;
    private Date lastUpdated;
    private List<ConversationEntry> entries = new ArrayList<>();
}