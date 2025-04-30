package io.promptics.jobagent.interview;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
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
    private List<ConversationEntry> entries = new ArrayList<>();
}