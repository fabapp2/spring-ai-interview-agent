package io.promptics.jobagent.interview;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.promptics.jobagent.interviewplan.TopicAndThread;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

@Component
public class ConversationAnalyzer {

    private final ChatClient chatClient;
    private final PromptRenderer promptRenderer;

    public ConversationAnalyzer(ChatClient.Builder builder, ThreadConversationRenderer conversationRenderer, TopicAndThreadRenderer topicAndThreadRenderer) {
        chatClient = builder.defaultOptions(ChatOptions.builder().model("gpt-4").temperature(0.0).build()).build();
        this.promptRenderer = new PromptRenderer(topicAndThreadRenderer, conversationRenderer);
    }

    public String analyzeUserInput(TopicAndThread topicAndThread, ThreadConversation conversation, String input) {
        String prompt = promptRenderer.renderPrompt(topicAndThread, conversation);
        return chatClient.prompt()
                .system(prompt)
                .user(input)
                .call()
                .content();
    }

    static class PromptRenderer {

        public static final String PROMPT_TEMPLATE = """
            You analyze user messages in an interview gathering career information.
            
            ## Task
            - Analyze the user message
            - Extract information related to career data
            - Assign the pieces of information to their matching sections of the career data
            - Return the analysis result
            
            ## Given information
            The thread conversation history:
            {threadConversation}
            
            The current interview topic and thread:
            {topicAndThread}
            
            Career data sections:
            - basics: Basic data like address and contact information
            - work: The users work history
            - certifications: Certifications the user has
            
            ## Expected Response
            Return plain JSON, no additional markup, no additional text.
            
            Example response format:
            {{
                "originalMessage": "the message as it was provided",
                "extractedInformation": [
                    {{
                        "section": "The name of the section in the career data where extracted information belongs to",
                        "information": [
                            "A complete bit of relevant information that was extracted",
                            "Another bit of relevant information that was extracted"
                        ]
                    }},
                    {{
                        "section": "The name of another section in the career data where extracted information belongs to",
                        "information": [
                            "A complete bit of relevant information that was extracted for this section",
                            "Another bit of relevant information that was extracted for this section"
                        ]
                    }},
                ]
            }}
            
            """;

        /*

            Update the interview plan:
            - Update the information in the current thread
            - Add new topics and threads when required.
            - Topics and threads are sorted by their priority, add new entries accordingly. Higher in the list means higher priority.
            - Update the state of the current topic and thread when required
                - When a current thread is finished, set the status to "complete"
                - If another thread exists in the topic with statzus "pending", set its status to "in_progress"
                - If no thread with status "pending" exists in the current topic, set the first thread in the next topic to "in_progress"
                - If no topic exists verify that all threads have status "complete", otherwise pick the topmost thread with status "pending" and set the state to "in_progress"

            The interview plan to update:

            {interviewPlan}
        */

        private final ThreadConversationRenderer conversationRenderer;
        private final TopicAndThreadRenderer topicAndThreadRenderer;

        public PromptRenderer(TopicAndThreadRenderer topicAndThreadRenderer, ThreadConversationRenderer conversationRenderer) {
            this.conversationRenderer = conversationRenderer;
            this.topicAndThreadRenderer = topicAndThreadRenderer;
        }

        String renderPrompt(TopicAndThread topicAndThread, ThreadConversation conversation) {
            String renderedTopicAndThread = topicAndThreadRenderer.renderTopicAndThread(topicAndThread.getTopic(), topicAndThread.getThread());
            String renderedConversation = conversationRenderer.renderConversation(conversation);

            String plan = "";
            try {
                // FIXME: Render current plan instead
                plan = new String(new ClassPathResource("interview-plan.json").getInputStream().readAllBytes());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            return new PromptTemplate(PROMPT_TEMPLATE, Map.of(
                    "threadConversation", renderedConversation,
                    "topicAndThread", renderedTopicAndThread,
                    "interviewPlan", plan
            )).render();
        }
    }

}
