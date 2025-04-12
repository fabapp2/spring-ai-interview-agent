package io.promptics.jobagent.interviewplan;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
@Testcontainers
class InterviewPlanRepositoryTest {

    @Container
    @ServiceConnection
    final static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:7.0");

    @Autowired
    InterviewPlanRepository repository;

    @Test
    @DisplayName("crud")
    void crud() {
        InterviewPlan entity = InterviewPlan.builder()
                .careerDataId("1234")
                .topic(InterviewTopic.builder()
                        .reference(Reference.builder()
                                .section("basics")
                                .build())
                        .thread(
                           InterviewThread.builder()
                                   .timePlanned(30)
                                   .type(InterviewThread.Type.CORE_DETAILS)
                                   .focus("User profiles, especially Linkedin")
                                   .focus("Gather missing information")
                                   .fields(List.of("profiles"))
                                   .build()
                        )
                        .thread(
                            InterviewThread.builder()
                                    .timePlanned(30)
                                    .type(InterviewThread.Type.CORE_DETAILS)
                                    .focus("User summary")
                                    .fields(List.of("summary"))
                                    .build()
                        )
                        .build())
                .topic(InterviewTopic.builder()
                        .reference(Reference.builder()
                                .section("education")
                                .description("TechU Berlin")
                                .build())
                        .thread(
                            InterviewThread.builder()
                                    .timePlanned(30)
                                    .type(InterviewThread.Type.CORE_DETAILS)
                                    .focus("Education in TechU Berlin")
                                    .field("score")
                                    .field("courses")
                                    .build()
                        )
                        .build())
                .build();
        InterviewPlan saved = repository.save(entity);
        assertThat(saved).isNotNull();
    }
}