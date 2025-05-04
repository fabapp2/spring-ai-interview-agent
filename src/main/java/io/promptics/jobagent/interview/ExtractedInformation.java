package io.promptics.jobagent.interview;

import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class ExtractedInformation {
    private String section;
    private List<String> informations;
}
