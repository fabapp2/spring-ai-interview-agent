package io.promptics.jobagent.interviewplan.agents;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.promptics.jobagent.careerdata.model.Work;
import io.promptics.jobagent.interviewplan.model.Topic;
import io.promptics.jobagent.interviewplan.model.TopicThread;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class WorkThreadsPlanningAgent extends AbstractThreadsPlanningAgent<List<Work>> {

    public WorkThreadsPlanningAgent(ObjectMapper objectMapper) {
        super(objectMapper);
    }

    @Override
    public List<TopicThread> planThreads(List<Work> sectionData, List<Topic> topics) {
        return List.of();
    }

    @Override
    protected List<TopicThread> promptLlm(String basicsSectionJson, String topicsJson) {
        return null;
    }

    private static final String SYSTEM_PROMPT = """
            You are a career interview planning agent.
            Your task is to create a structured **interview plan** that guides an AI through gathering missing and meaningful career information. The plan will be used to conduct interactive, thread-based interviews.
            
            ---
            
            CURRENT DATE TIME: {datetime}
            TIME LEFT: {time_left} minutes
            TIME PLANNED: {time_planned} minutes
            
            ---
            
             PLANNING APPROACH:
             1. Gap Identification
                - Compare current date with latest career entry
                - Identify missing information in existing entries
                - Look for timeline inconsistencies
                - Flag incomplete required fields
             
             2. Topic Creation
                Create topics for:
                - Identified gaps (primary focus)
                - Career experiences
                - Skills and certifications
                - Education
             
             3. Thread Planning
                For gap topics:
                - Core gap investigation
                - Context understanding
                - Impact on career narrative
                - Related information gathering
             
                For experience topics:
                - Core experience details
                - Key achievements
                - Skills and growth
                - Team context
                - Challenges
                - Transitions
             
            PRIORITIZATION:
             - Recent gaps (highest priority)
             - Missing critical information
             - Timeline inconsistencies
             - Career progression gaps
             - Skill development gaps
             - Gather more information
             
            EXAMPLES:
            {few_shot}        
                    
                            
            EXPECTED OUTPUT:
            Create a structured interview plan following this exact JSON schema:
            {json_schema}
                        
            Return an interview plan that follows this schema exactly. Do not return the schema itself.
            Do not wrap the JSON in ```json and ```                                    
            """;

    private final String fewShot = """
                    
            ## 1. Recent Employment Gap
            Current Date: 2025-04-12
            Input CV:
            {{
                "work": [{{
                    "name": "TechGiant",
                    "position": "Senior Developer",
                    "startDate": "2024-01",
                    "endDate": "2024-12"
                }}]
            }}
                    
            Expected Output:
            {{
                "topics": [{{
                    "id": "gap_current_employment",
                    "type": "gap",
                    "reference": {{
                        "section": "work",
                        "identifier": {{
                            "name": "TechGiant",
                            "startDate": "2024-01"
                        }}
                    }},
                    "threads": [{{
                        "id": "current_status",
                        "type": "core_details",
                        "focus": "Determine current employment status and activities since December 2024",
                        "duration": 15,
                        "status": "pending"
                    }}]
                }}]
            }}
                    
            ## 2. Missing Information Gap
            Input CV:
            {{
                "work": [{{
                    "name": "TechCorp",
                    "position": "Team Lead",
                    "startDate": "2024-01",
                    "endDate": "2024-12",
                    "highlights": ["Led development team"]
                }}]
            }}
                    
            Expected Output:
            {{
                "topics": [{{
                    "id": "gap_team_context",
                    "type": "gap",
                    "reference": {{
                        "section": "work",
                        "identifier": {{
                            "name": "TechCorp",
                            "startDate": "2024-01"
                        }}
                    }},
                    "threads": [{{
                        "id": "team_details",
                        "type": "team_context",
                        "focus": "Determine team size, structure, and responsibilities",
                        "duration": 10,
                        "status": "pending"
                    }}]
                }}]
            }}
                    
            ## 3. Career Experience Deep-Dive
            Input CV:
            {{
                "work": [{{
                    "name": "TechCorp",
                    "position": "Product Manager",
                    "startDate": "2024-01",
                    "summary": "Led product development",
                    "highlights": ["Launched new platform"]
                }}]
            }}
                    
            Expected Output:
            {{
                "topics": [{{
                    "id": "techcorp_product_launch",
                    "type": "work_experience",
                    "reference": {{
                        "section": "work",
                        "identifier": {{
                            "name": "TechCorp",
                            "startDate": "2024-01"
                        }}
                    }},
                    "threads": [{{
                        "id": "platform_launch",
                        "type": "achievements",
                        "focus": "Details of platform launch and impact",
                        "duration": 15,
                        "status": "pending"
                    }},
                    {{
                        "id": "product_strategy",
                        "type": "core_details",
                        "focus": "Product development approach and decisions",
                        "duration": 10,
                        "status": "pending",
                        "related_threads": ["platform_launch"]
                    }}]
                }}]
            }}
                    
            ## 4. Skills Development
            Input CV:
            {{
                "skills": [{{
                    "name": "Cloud Architecture",
                    "keywords": ["AWS", "Azure"],
                    "level": "Advanced"
                }}],
                "work": [{{
                    "name": "TechCorp",
                    "startDate": "2024-01"
                }}]
            }}
                    
            Expected Output:
            {{
                "topics": [{{
                    "id": "cloud_skills",
                    "type": "skill_area",
                    "reference": {{
                        "section": "skills",
                        "identifier": {{
                            "name": "Cloud Architecture"
                        }}
                    }},
                    "threads": [{{
                        "id": "cloud_experience",
                        "type": "skill_application",
                        "focus": "Practical application of cloud technologies in recent roles",
                        "duration": 15,
                        "status": "pending",
                        "related_threads": ["cloud_projects"]
                    }},
                    {{
                        "id": "cloud_projects",
                        "type": "project_specifics",
                        "focus": "Specific cloud architecture projects and decisions",
                        "duration": 10,
                        "status": "pending"
                    }}]
                }}]
            }}    
            """;
}
