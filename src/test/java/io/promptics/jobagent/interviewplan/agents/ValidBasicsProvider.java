package io.promptics.jobagent.interviewplan.agents;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.promptics.jobagent.careerdata.model.Basics;

public class ValidBasicsProvider {

    public static final String VALID_JSON = """
            {
              "name": "Max Müller",
              "email": "max@foo.com",
              "summary": "Software engineer with 2 years of experience, specializing in JavaScript and cloud technologies such as AWS and serverless architectures.",
              "id": "1133776655",
              "label": "Software Engineer specialized in Cloud and Javascript",
              "phone": "+66 2234 2233",
              "location": {
                "countryCode": "DE",
                "city": "Berlin"
              },
              "profiles": [
                {
                  "id": "1111111111",
                  "network": "Linkedin",
                  "username": "Max",
                  "url": "https://linkedin.com/in/max"
                }
              ]
            }
            """;

    public static Basics provideObject(ObjectMapper objectMapper) {
        try {
            return objectMapper.readValue(VALID_JSON, Basics.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
