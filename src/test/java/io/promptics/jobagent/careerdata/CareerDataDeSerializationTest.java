package io.promptics.jobagent.careerdata;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.*;
import io.promptics.jobagent.careerdata.model.CareerData;
import org.intellij.lang.annotations.Language;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class CareerDataDeSerializationTest {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("should deserialize JSON with id as additionalFields")
    void shouldDeSerializeJsonWithIdAsAdditionalFields() throws IOException {
        CareerData cd = objectMapper.readValue(new ClassPathResource("career-data.json").getFile(), CareerData.class);
        assertThat(cd.getWork().get(0).getId()).isEqualTo("111111");

        String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(cd);
//        assertThat(json).isEqualTo(JSON);

        Set<ValidationMessage> validate = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V7).getSchema(new ClassPathResource("resume-schema.json").getInputStream()).validate(json, InputFormat.JSON);
        assertThat(validate.size()).isEqualTo(0);
        System.out.println(validate);

        cd.getWork().get(1).setId("222222");

        String json2 = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(cd);
//        System.out.println(json2);
    }

    @Language("json")
    private final String JSON = """
            {
              "basics": {
                "name": "Max Wurst",
                "label": "Senior Developer",
                "email": "max.wurst@techgiant.com",
                "phone": "+49 123 456 7890",
                "summary": "Sarcastic yet oddly effective Senior Developer with 8 years of experience in writing code that occasionally works. Known for turning coffee into questionable software solutions and debugging like it's an Olympic sport. Currently pretending to be productive at TechGiant while secretly plotting world domination through perfectly indented code.",
                "location": null,
                "profiles": [
                  {
                    "network": "LinkedIn",
                    "username": "maxwurst",
                    "url": "https://linkedin.com/in/maxwurst"
                  }
                ]
              },
              "education": [
                {
                  "institution": "TechU Berlin",
                  "area": "Computer Science",
                  "studyType": "Master's",
                  "startDate": "2015-09",
                  "endDate": "2017-06",
                  "score": "3.8",
                  "courses": [
                    "Thesis: 'The Correlation Between Coffee Consumption and Code Quality'",
                    "Thesis: 'The Quantum Entanglement of Spaghetti Code and Developer Sanity'"
                  ]
                }
              ],
              "skills": [
                {
                  "name": "Programming Languages",
                  "keywords": [
                    "Java",
                    "Python",
                    "C++",
                    "Stack Overflow",
                    "JavaScript",
                    "Sarcasm",
                    "whatever was trendy last week"
                  ]
                },
                {
                  "name": "Frameworks",
                  "keywords": [
                    "Spring",
                    "Django",
                    "React",
                    "Angular",
                    "Angular (but only the versions no one uses anymore)"
                  ]
                },
                {
                  "name": "Database",
                  "keywords": [
                    "SQL",
                    "NoSQL",
                    "YesSQL",
                    "MySQL",
                    "MongoDB",
                    "Excel",
                    "ExcelSQL"
                  ]
                },
                {
                  "name": "Version Control",
                  "keywords": [
                    "Git",
                    "prayer when merging"
                  ]
                },
                {
                  "name": "DevOps",
                  "keywords": [
                    "Docker",
                    "Kubernetes",
                    "CI/CD",
                    "Jenkins",
                    "automagic"
                  ]
                },
                {
                  "name": "Soft Skills",
                  "keywords": [
                    "translating 'tech speak' to 'human speak'",
                    "Turning coffee into code",
                    "translating technical jargon into almost-understandable English",
                    "Coffee Brewing",
                    "Rubber Duck Debugging"
                  ]
                },
                {
                  "name": "Cloud Platforms",
                  "keywords": [
                    "AWS",
                    "Azure",
                    "GCP",
                    "mostly for storing cat pictures"
                  ]
                }
              ],
              "work": [
                {
                  "name": "StartupX",
                  "position": "Junior Developer",
                  "startDate": "2017-07",
                  "endDate": "2019-12",
                  "highlights": [
                    "Wrote groundbreaking 'Hello World' programs in 17 different languages",
                    "Debugged senior developers' code while they were on coffee breaks",
                    "Implemented a revolutionary system for organizing the office snack drawer",
                    "Mastered the art of looking busy during pointless meetings",
                    "Survived multiple pivots and learned to code faster than the business model could change",
                    "Mastered the art of translating 'I don't know' into 'Let me research that and get back to you'",
                    "Contributed to open-source projects, mostly by opening issues and forgetting about them"
                  ],
                  "id": "111111"
                },
                {
                  "name": "MiniCorp",
                  "position": "Software Engineer",
                  "startDate": "2020-01",
                  "endDate": "2022-06",
                  "highlights": [
                    "Architected scalable solutions while simultaneously scaling the office coffee consumption",
                    "Debugged legacy code older than some team members",
                    "Implemented agile methodologies, because who doesn't love a good sprint... away from deadlines",
                    "Mentored junior developers in the ancient art of Stack Overflow navigation"
                  ]
                },
                {
                  "name": "TechGiant",
                  "position": "Senior Developer",
                  "startDate": "2024-01",
                  "endDate": "2024-12",
                  "highlights": [
                    "Lead a team of developers who miraculously haven't quit yet",
                    "Architect scalable solutions that only crash during demos",
                    "Implement cutting-edge technologies nobody understands but everyone pretends to",
                    "Mentor junior developers in the art of Stack Overflow copying",
                    "Key Achievement: Reduced system downtime by 30% by turning it off and on again"
                  ]
                }
              ],
              "certificates": [
                {
                  "name": "Certified Sarcasm Practitioner (CSP)"
                },
                {
                  "name": "Master of Caffeination (MC)"
                },
                {
                  "name": "Certified Scrum Master"
                },
                {
                  "name": "AWS Certified Solutions Architect"
                }
              ],
              "languages": [
                {
                  "language": "English",
                  "fluency": "Native speaker"
                },
                {
                  "language": "German",
                  "fluency": "Fluent"
                }
              ]
            }
            """;
}
