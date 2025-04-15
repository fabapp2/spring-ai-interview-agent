package io.promptics.jobagent.interview;

import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class TopicTypeDescriptionMapper {
    private static final String TYPE_TEMPLATE = """
        Type: {typeName}
        {bulletPoints}
        """;

    private static final Map<String, TypeContent> TYPE_DESCRIPTIONS = Map.ofEntries(
            Map.entry("work_experience", new TypeContent(
                    "Work Experience",
                    """
                    - Professional role and responsibilities
                    - Company and industry context
                    - Duration and progression
                    - Key achievements and impact
                    - Team and organizational structure
                    """
            )),
            Map.entry("volunteer_work", new TypeContent(
                    "Volunteer Work",
                    """
                    - Organization and cause
                    - Role and contributions
                    - Impact on community
                    - Skills applied and developed
                    - Duration and commitment level
                    """
            )),
            Map.entry("education", new TypeContent(
                    "Education",
                    """
                    - Degree and specialization
                    - Institution and duration
                    - Key areas of study
                    - Academic achievements
                    - Relevant projects/research
                    """
            )),
            Map.entry("project", new TypeContent(
                    "Project",
                    """
                    - Project scope and objectives
                    - Role and responsibilities
                    - Technologies and methods used
                    - Outcomes and impact
                    - Team collaboration aspects
                    """
            )),
            Map.entry("award", new TypeContent(
                    "Award",
                    """
                    - Award title and recognition
                    - Awarding organization
                    - Achievement criteria
                    - Impact and significance
                    - Date and context
                    """
            )),
            Map.entry("certificate", new TypeContent(
                    "Certificate",
                    """
                    - Certification name and issuer
                    - Skills and knowledge validated
                    - Achievement requirements
                    - Industry relevance
                    - Date and validity period
                    """
            )),
            Map.entry("publication", new TypeContent(
                    "Publication",
                    """
                    - Publication title and type
                    - Subject matter and findings
                    - Research methodology
                    - Impact and citations
                    - Collaboration details
                    """
            )),
            Map.entry("skill_area", new TypeContent(
                    "Skill Area",
                    """
                    - Technical competencies
                    - Experience level and depth
                    - Practical applications
                    - Tools and technologies
                    - Continuous development
                    """
            )),
            Map.entry("language", new TypeContent(
                    "Language",
                    """
                    - Language proficiency level
                    - Professional usage contexts
                    - Certification if applicable
                    - Cultural understanding
                    - Communication capabilities
                    """
            )),
            Map.entry("interest", new TypeContent(
                    "Interest",
                    """
                    - Professional relevance
                    - Active engagement level
                    - Knowledge development
                    - Industry connection
                    - Practical application
                    """
            )),
            Map.entry("reference", new TypeContent(
                    "Reference",
                    """
                    - Professional relationship
                    - Duration of association
                    - Context of collaboration
                    - Areas of endorsement
                    - Relevance to role
                    """
            )),
            Map.entry("gap", new TypeContent(
                    "Career Gap",
                    """
                    - Duration and timing
                    - Reason and context
                    - Skills maintained/developed
                    - Professional development
                    - Return to work transition
                    """
            )),
            Map.entry("role", new TypeContent(
                    "Role",
                    """
                    - Position and responsibilities
                    - Team and reporting structure
                    - Key deliverables
                    - Growth and progression
                    - Impact and achievements
                    """
            )),
            Map.entry("default", new TypeContent(
                    "Unknown Topic Type",
                    """
                    - General background
                    - Key information
                    - Relevant context
                    - Important details
                    - Notable aspects
                    """
            ))
    );

    public String mapType(String type) {
        TypeContent content = TYPE_DESCRIPTIONS.getOrDefault(type, TYPE_DESCRIPTIONS.get("default"));
        
        Map<String, Object> variables = new HashMap<>();
        variables.put("typeName", content.name());
        variables.put("bulletPoints", content.bulletPoints());
        
        return new PromptTemplate(TYPE_TEMPLATE, variables).render();
    }
}