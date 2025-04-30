package io.promptics.jobagent.interview;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document("thread_conversation")
public class ThreadConversation {
    @Id
    private String id;
    private String threadId;
    private Date lastUpdated;
    @Singular
    private List<ConversationEntry> entries = new ArrayList<>();
}