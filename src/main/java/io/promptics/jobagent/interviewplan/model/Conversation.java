
package io.promptics.jobagent.interviewplan.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "id",
    "threadId",
    "timestamp",
    "role",
    "content",
    "type"
})
@Document("conversations")
public class Conversation {

    /**
     * that uniquely identifies this conversation entry.
     * (Required)
     * 
     */
    @Id
    @NotNull
    private String id;
    /**
     * ID linking back to the thread this conversation belongs to.
     * (Required)
     * 
     */
    @JsonProperty("threadId")
    @JsonPropertyDescription("ID linking back to the thread this conversation belongs to.")
    @NotNull
    private String threadId;
    /**
     * Timestamp when the message occurred.
     * (Required)
     * 
     */
    @JsonProperty("timestamp")
    @JsonPropertyDescription("Timestamp when the message occurred.")
    @NotNull
    private Date timestamp;
    /**
     * Role of the message sender.
     * (Required)
     * 
     */
    @JsonProperty("role")
    @JsonPropertyDescription("Role of the message sender.")
    @NotNull
    private Conversation.Role role;
    /**
     * The actual text content of the conversation message.
     * (Required)
     * 
     */
    @JsonProperty("content")
    @JsonPropertyDescription("The actual text content of the conversation message.")
    @NotNull
    private String content;
    /**
     * Optional label classifying the message as question, answer, command, note, etc.
     * 
     */
    @JsonProperty("type")
    @JsonPropertyDescription("Optional label classifying the message as question, answer, command, note, etc.")
    private Type type;

    /**
     * that uniquely identifies this conversation entry.
     * (Required)
     * 
     */
    @JsonProperty("id")
    public String getId() {
        return id;
    }

    /**
     * that uniquely identifies this conversation entry.
     * (Required)
     * 
     */
    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    /**
     * ID linking back to the thread this conversation belongs to.
     * (Required)
     * 
     */
    @JsonProperty("threadId")
    public String getThreadId() {
        return threadId;
    }

    /**
     * ID linking back to the thread this conversation belongs to.
     * (Required)
     * 
     */
    @JsonProperty("threadId")
    public void setThreadId(String threadId) {
        this.threadId = threadId;
    }

    /**
     * Timestamp when the message occurred.
     * (Required)
     * 
     */
    @JsonProperty("timestamp")
    public Date getTimestamp() {
        return timestamp;
    }

    /**
     * Timestamp when the message occurred.
     * (Required)
     * 
     */
    @JsonProperty("timestamp")
    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * Role of the message sender.
     * (Required)
     * 
     */
    @JsonProperty("role")
    public Role getRole() {
        return role;
    }

    /**
     * Role of the message sender.
     * (Required)
     * 
     */
    @JsonProperty("role")
    public void setRole(Role role) {
        this.role = role;
    }

    /**
     * The actual text content of the conversation message.
     * (Required)
     * 
     */
    @JsonProperty("content")
    public String getContent() {
        return content;
    }

    /**
     * The actual text content of the conversation message.
     * (Required)
     * 
     */
    @JsonProperty("content")
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * Optional label classifying the message as question, answer, command, note, etc.
     * 
     */
    @JsonProperty("type")
    public Type getType() {
        return type;
    }

    /**
     * Optional label classifying the message as question, answer, command, note, etc.
     * 
     */
    @JsonProperty("type")
    public void setType(Type type) {
        this.type = type;
    }


    /**
     * Role of the message sender.
     * 
     */
    public enum Role {

        USER("user"),
        ASSISTANT("assistant"),
        SYSTEM("system");
        private final String value;
        private final static Map<String, Role> CONSTANTS = new HashMap<String, Role>();

        static {
            for (Role c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        Role(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        @JsonValue
        public String value() {
            return this.value;
        }

        @JsonCreator
        public static Role fromValue(String value) {
            Role constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }


    /**
     * Optional label classifying the message as question, answer, command, note, etc.
     * 
     */
    public enum Type {

        QUESTION("question"),
        ANSWER("answer"),
        COMMAND("command"),
        NOTE("note");
        private final String value;
        private final static Map<String, Type> CONSTANTS = new HashMap<String, Type>();

        static {
            for (Type c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        Type(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        @JsonValue
        public String value() {
            return this.value;
        }

        @JsonCreator
        public static Type fromValue(String value) {
            Type constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
