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
                .type(TopicThread.Type.CORE_DETAILS)
                .focus("No location information for city")
                .build();
        TopicAndThread topicAndThread = new TopicAndThread(topic, thread);

        TopicAndThreadRenderer sut = new TopicAndThreadRenderer(new ThreadTypeDescriptionMapper());
        String rendered = sut.renderTopicAndThread(topicAndThread.getTopic(), topicAndThread.getThread());
        System.out.println(rendered);
        assertThat(rendered).isEqualTo("""
            ##  Current interview topic
            The current topic covers "" of career data.
            It references the career data section with id "1122334455"
            
            ## Thread of interview topic
            Thread focus: No location information for city
            
            Type: Core Details
            - Essential information about the topic
            - Main responsibilities and activities
            - Key methods and approaches used
            - Regular duties and tasks
            - Overall context and environment
            """);
    }

}