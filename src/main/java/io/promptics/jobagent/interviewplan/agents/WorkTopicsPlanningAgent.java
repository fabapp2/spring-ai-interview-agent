package io.promptics.jobagent.interviewplan.agents;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.promptics.jobagent.careerdata.model.Work;
import io.promptics.jobagent.interviewplan.model.Topic;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class WorkTopicsPlanningAgent extends AbstractTopicsPlanningAgent<List<Work>> {

    private static final String MODEL = "gpt-4o-mini";
    private static final Double TEMPERATURE = 0.1;
    private ChatClient client;

    public WorkTopicsPlanningAgent(ObjectMapper objectMapper, ChatClient.Builder builder) {
        super(objectMapper);
        ChatOptions options = ChatOptions.builder().model(MODEL).temperature(TEMPERATURE).build();
        client = builder.defaultOptions(options).build();
    }

    @Override
    public List<Topic> planTopics(String careerDataId, List<Work> works) {
        String workJson = serialize(works);
        String response = client.prompt()
                .system(SYSTEM_PROMPT)
                .user(workJson)
                .call()
                .content();
        List<Topic> topics = deserialize(response, new TypeReference<List<Topic>>() {});
        topics.forEach(topic -> topic.setCareerDataId(careerDataId));
        return topics;
    }

    static final String SYSTEM_PROMPT = """
        You are a career interview planner.
        
        The interview is to enhance and improve career information.
        
        You plan topics that need to be covered to complete, improve and enhance the work history of provided career data.
        
        ## Response format
        The topic has to follow a fixed JSON format.
        
        ## Required Fields for Each Topic:
        - type - A string defining the topic type. Use one of the allowed values listed below.
        
        Allowed Values for the type field (with explanations)
        
        - basics - For gaps or uncertainties directly in the basics section (e.g., missing summary, unclear location, incomplete profiles).
        - work - Topics about missing or unclear work experience entries (e.g., missing company name, unclear responsibilities).
        - gap - Topics identifying missing time periods between resume activities.
        - career_transition - Topics about major career changes (e.g., industry switch).
        - freelance_period - Topics about periods of freelance/self-employment.
        - timeline_arc - Topics that span multiple years or activities in a major way.
        - role - Topics about specific roles or responsibilities that are unclear.
        - narrative - Topics for general career storytelling or motivation aspects.
        
        Always select the most specific and meaningful type based on what needs clarification.
        
        ### Optional Fields
        Include only when relevant:
        
        - reference - An object describing the element linked to the topic. Use to specify which exact element in the provided Basics data the topic refers to. Linkage rules:
        - resumeItemId (string) - ID of a specific resume item (e.g., profile entry id inside basics.profiles) that this topic is about. Use the ID given in the input Basics section.
        - spans (array of strings) - Identifiers for time spans related to this topic, used for timeline or gap-related topics.
        - startDate (string, format YYYY or YYYY-MM) - Optional start date when the topic refers to a time period.
        - endDate (string, format YYYY or YYYY-MM) - Optional end date when the topic refers to a time period.
        - resumeItemBeforeId (string) - ID of a resume item before a detected gap.
        - resumeItemAfterId (string) - ID of a resume item after a detected gap.
        + gapType - Required if the topic type is "gap". Allowed values:
        - time - For normal missing periods between activities.
        - timeline_boundary - For gaps at the start or end of the resume timeline.
        - reason - Short human-readable explanation for why the topic is needed.
        - reasonMeta - Machine-readable structured metadata. Allowed fields:
        - detectedBy (string)
        - gapDurationInMonths (integer)
        - missingSectionCandidates (array of strings)
        - patternType (string)
        - confidenceScore (number)
        - relatedResumeItems (array of strings)
        - timestamp (ISO 8601 string)
        - priorityScore - Integer between 0 and 100.
        - priority - Priority level as "low", "medium", or "high".
        - createdAt - Timestamp (ISO 8601 format). Leave empty.
        - updatedAt - Timestamp (ISO 8601 format). Leave empty.
        
        Respond only with the correct JSON structure, no explanations, no comments, no additional markup.
        """;
}
