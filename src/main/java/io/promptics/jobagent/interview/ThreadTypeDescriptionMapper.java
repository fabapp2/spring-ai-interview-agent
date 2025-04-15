package io.promptics.jobagent.interview;

import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ThreadTypeDescriptionMapper {
    private static final String TYPE_TEMPLATE = """
        Type: {typeName}
        {bulletPoints}
        """;

    private static final Map<String, TypeContent> TYPE_DESCRIPTIONS = Map.ofEntries(
            Map.entry("core_details", new TypeContent(
                    "Core Details",
                    """
                    - Essential information about the topic
                    - Main responsibilities and activities
                    - Key methods and approaches used
                    - Regular duties and tasks
                    - Overall context and environment
                    """
            )),
            Map.entry("achievements", new TypeContent(
                    "Achievements",
                    """
                    - Key accomplishments and results
                    - Measurable impacts and outcomes
                    - Innovation and improvements
                    - Recognition and awards
                    - Value delivered to organization
                    """
            )),
            Map.entry("responsibilities", new TypeContent(
                    "Responsibilities",
                    """
                    - Primary duties and obligations
                    - Day-to-day activities
                    - Areas of ownership
                    - Decision-making authority
                    - Accountability measures
                    """
            )),
            Map.entry("skills_used", new TypeContent(
                    "Skills Used",
                    """
                    - Technical skills applied
                    - Tools and technologies utilized
                    - Methodologies implemented
                    - Soft skills demonstrated
                    - Knowledge areas leveraged
                    """
            )),
            Map.entry("team_context", new TypeContent(
                    "Team Context",
                    """
                    - Team structure and dynamics
                    - Collaboration methods
                    - Communication approaches
                    - Leadership aspects
                    - Team achievements
                    """
            )),
            Map.entry("challenges", new TypeContent(
                    "Challenges",
                    """
                    - Major obstacles encountered
                    - Problem-solving approaches
                    - Resolution strategies
                    - Lessons learned
                    - Prevention measures
                    """
            )),
            Map.entry("transition", new TypeContent(
                    "Transition",
                    """
                    - Reasons for change
                    - Adaptation process
                    - Knowledge transfer
                    - Impact management
                    - Lessons from transition
                    """
            )),
            Map.entry("impact", new TypeContent(
                    "Impact",
                    """
                    - Business value delivered
                    - Quantifiable results
                    - Stakeholder benefits
                    - Long-term effects
                    - Success metrics
                    """
            )),
            Map.entry("learning", new TypeContent(
                    "Learning",
                    """
                    - Knowledge acquired
                    - Skills developed
                    - Growth opportunities
                    - Application of learning
                    - Development path
                    """
            )),
            Map.entry("collaboration", new TypeContent(
                    "Collaboration",
                    """
                    - Cross-team interactions
                    - Partnership approaches
                    - Stakeholder management
                    - Joint achievements
                    - Communication methods
                    """
            )),
            Map.entry("technical_depth", new TypeContent(
                    "Technical Depth",
                    """
                    - Technical decision-making
                    - Architecture considerations
                    - Implementation details
                    - Technology expertise
                    - Technical challenges solved
                    """
            )),
            Map.entry("project_specifics", new TypeContent(
                    "Project Specifics",
                    """
                    - Project objectives
                    - Implementation approach
                    - Timeline and milestones
                    - Resource management
                    - Delivery outcomes
                    """
            )),
            Map.entry("certification_details", new TypeContent(
                    "Certification Details",
                    """
                    - Certification scope
                    - Knowledge demonstrated
                    - Achievement process
                    - Practical application
                    - Ongoing relevance
                    """
            )),
            Map.entry("publication_impact", new TypeContent(
                    "Publication Impact",
                    """
                    - Research contribution
                    - Industry influence
                    - Knowledge sharing
                    - Citation impact
                    - Practical applications
                    """
            )),
            Map.entry("skill_application", new TypeContent(
                    "Skill Application",
                    """
                    - Practical usage
                    - Real-world scenarios
                    - Problem-solving examples
                    - Effectiveness measures
                    - Skill development
                    """
            )),
            Map.entry("language_usage", new TypeContent(
                    "Language Usage",
                    """
                    - Communication contexts
                    - Proficiency level
                    - Business applications
                    - Cultural aspects
                    - International collaboration
                    """
            )),
            Map.entry("interest_relevance", new TypeContent(
                    "Interest Relevance",
                    """
                    - Professional connection
                    - Skill enhancement
                    - Industry insight
                    - Personal development
                    - Career alignment
                    """
            )),
            Map.entry("default", new TypeContent(
                    "Unknown Thread Type",
                    """
                    - General discussion points
                    - Key aspects to explore
                    - Relevant details
                    - Important considerations
                    - Expected outcomes
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
