package io.promptics.jobagent.interview;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.promptics.jobagent.interviewplan.TopicAndThread;
import io.promptics.jobagent.interviewplan.model.Reference;
import io.promptics.jobagent.interviewplan.model.Topic;
import io.promptics.jobagent.interviewplan.model.TopicThread;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.compiler.CompiledST;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class TopicAndThreadRendererTest {

    @Test
    @DisplayName("stringtemplate with conditionals")
    void stringtemplate() {
        STGroup g = new STGroup('<', '>');
        // Define a template with proper if-else syntax
        g.defineTemplate("name", "val",
                """
                    <if(val)>
                        YES<else>
                        NO
                        <endif>
                    """);

        // Create instance of the template
        ST add = g.getInstanceOf("name").add("val", null);

        String val = add.render();
        System.out.println(val); // Outputs: YES
    }
    
    @Test
    @DisplayName("should render")
    void shouldRender() throws JsonProcessingException {

        Topic topic = Topic.builder()
                .type(Topic.Type.BASICS)
                .reference(Reference.builder()
                        .resumeItemId("1122334455")
                        .build())
                .build();
        TopicThread thread = TopicThread.builder()
                .focus(TopicThread.Focus.CORE_DETAILS)
                .focusReason("No location information for city")
                .build();
        TopicAndThread topicAndThread = new TopicAndThread(topic, thread);

        TopicAndThreadRenderer sut = new TopicAndThreadRenderer(new ThreadTypeDescriptionMapper());
        String rendered = sut.renderTopicAndThread(topicAndThread.getTopic(), topicAndThread.getThread());
        System.out.println(rendered);
        assertThat(rendered).isEqualTo("""
            The current topic covers the section "basics" in career data with id "1122334455".
            The current thread in this topic is: "core_details" and handles "core_details".
            
            Type: Core Details
            - Essential information about the topic
            - Main responsibilities and activities
            - Key methods and approaches used
            - Regular duties and tasks
            - Overall context and environment
            """);
    }

}