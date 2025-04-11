package io.promptics.jopbagent;

import lombok.Builder;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.expression.spel.ast.Identifier;

@Document
@Builder
public class Reference {
    // The section in Resume JSON
    private String section;
    // Describes the sub-section inside section
    private String description;
}