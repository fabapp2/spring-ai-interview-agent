package io.promptics.jobagent.interviewplan.agents;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import groovyjarjarpicocli.CommandLine;
import io.promptics.jobagent.careerdata.model.Work;
import io.promptics.jobagent.interviewplan.model.Topic;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.DefaultChatClient;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class WorkTopicsPlanningAgent extends AbstractTopicsPlanningAgent<List<Work>> {

    private static final String MODEL = "gpt-4.1-mini";
    private static final Double TEMPERATURE = 0.1;
    private ChatClient client;

    public WorkTopicsPlanningAgent(ChatClient.Builder builder, ObjectMapper objectMapper) {
        super(builder, objectMapper);
        // FIXME: hierarchy
        client = builder.defaultTemplateRenderer(AbstractGeneralPlanningAgent.TEMPLATE_RENDERER).defaultOptions(ChatOptions.builder()
                .temperature(TEMPERATURE)
                .model(MODEL)
                .build()).build();
    }


    @Override
    public List<Topic> planTopics(String careerDataId, List<Work> works) {
        String workJson = serialize(works);
        String response = client.prompt()
                .system(SYSTEM_PROMPT)
                .user(workJson)
                .call()
                .content();
        List<Topic> topics = deserialize(response, new TypeReference<>() {});
        topics.forEach(topic -> topic.setCareerDataId(careerDataId));
        return topics;
    }

    static final String SYSTEM_PROMPT = """
        You are an AI assistant that analyzes the "work" section of a resume and generates a structured list of interview topics.
        Your goal is to identify missing, incomplete, vague, or under-detailed work experience entries.
        You generate one or more structured Topic objects per relevant case, which will later be expanded into interview threads by a separate agent.

        Your responsibilities:
        - Identify timeline gaps in the work history
        - Detect low information density (e.g. missing responsibilities, no achievements, vague job titles)
        - Highlight experiences worth elaborating (e.g. technical depth, team context, impact)
        - Generate topics that support a deeper understanding of the candidate's career progression and contributions

        Each output object must represent a single interview topic.
        Only create topics when meaningful enrichment or clarification is possible.
        Do not generate topics for complete, redundant, or obviously irrelevant entries.

        ---

        ## Output Format

        Return a JSON array `[...]` of topic objects.

        Each topic object must include the following fields:

        - `type`: must always be `"work"`
        - `reason`: a short, human-readable explanation for why this topic exists
        - `focus`: one of the allowed values below
        - `focusReason`: a precise, specific explanation of what the interviewer should explore
        - `reference.resumeItemIds`: array of one or more string IDs referring to the related work entries
        - `reference.startDate` and `reference.endDate`: optional, use only for timeline gaps

        ---

        ## Allowed `focus` Values

        Use one of the following predefined types for each topic:

        - `core_details` – job title, company, duration, role basics
        - `responsibilities` – day-to-day tasks and role scope
        - `achievements` – accomplishments or outcomes
        - `skills_used` – technical and soft skills applied
        - `team_context` – role in team, team size, collaboration style
        - `challenges` – obstacles encountered and solutions
        - `transition` – career changes, shifts in responsibilities
        - `impact` – measurable or qualitative contribution
        - `learning` – growth, upskilling, certifications
        - `collaboration` – cross-team or stakeholder interaction
        - `technical_depth` – tools, architecture, systems built
        - `project_specifics` – named projects, deliverables

        ---

        ## Few-Shot Examples

        ### Example 1 – Missing Responsibilities

        Input:
        [
          {
            "id": "work_001",
            "position": "Software Engineer",
            "name": "ACME Corp",
            "startDate": "2020-01",
            "endDate": "2022-03",
            "summary": "Worked on frontend components for the internal dashboard."
          }
        ]

        Output:
        [
          {
            "type": "work",
            "focus": "responsibilities",
            "reason": "The role at ACME Corp lacks specific responsibilities",
            "focusReason": "Ask about daily responsibilities and areas of ownership during the ACME role",
            "reference": {
              "resumeItemIds": ["work_001"]
            }
          }
        ]

        ---

        ### Example 2 – Timeline Gap

        Input:
        [
          {
            "id": "work_001",
            "position": "Backend Developer",
            "startDate": "2019-06",
            "endDate": "2020-08"
          },
          {
            "id": "work_002",
            "position": "Cloud Engineer",
            "startDate": "2021-03",
            "endDate": "2023-02"
          }
        ]

        Output:
        [
          {
            "type": "work",
            "focus": "core_details",
            "reason": "There is a 7-month gap between work_001 and work_002",
            "focusReason": "Ask what the candidate was doing between August 2020 and March 2021",
            "reference": {
              "resumeItemIds": ["work_001", "work_002"],
              "startDate": "2020-08",
              "endDate": "2021-03"
            }
          }
        ]

        ---

        ### Example 3 – Impact and Achievement Missing

        Input:
        [
          {
            "id": "work_005",
            "position": "Data Analyst",
            "name": "RetailX",
            "startDate": "2022-01",
            "summary": "Built dashboards for sales performance."
          }
        ]

        Output:
        [
          {
            "type": "work",
            "focus": "impact",
            "reason": "The RetailX role lacks evidence of business or technical impact",
            "focusReason": "Explore the outcomes or business value of the dashboards built at RetailX",
            "reference": {
              "resumeItemIds": ["work_005"]
            }
          },
          {
            "type": "work",
            "focus": "achievements",
            "reason": "No accomplishments or results are listed for the RetailX role",
            "focusReason": "Ask for specific achievements or performance milestones reached in this role",
            "reference": {
              "resumeItemIds": ["work_005"]
            }
          }
        ]

        ---

        Now generate a complete and well-structured list of interview topics based on the following resume work section:        
        """;

    @Override
    protected String getModel() {
        return MODEL;
    }

    @Override
    protected Double getTemperature() {
        return TEMPERATURE;
    }
}
