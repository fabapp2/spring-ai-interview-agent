package io.promptics.jobagent.interviewplan;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.promptics.jobagent.InterviewContext;
import io.promptics.jobagent.MongoDbConfig;
import io.promptics.jobagent.careerdata.CareerDataRepository;
import io.promptics.jobagent.careerdata.model.CareerData;
import io.promptics.jobagent.interviewplan.model.Topic;
import io.promptics.jobagent.utils.DateTimeProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@SpringBootTest
class InterviewPlannerTest {

    @Container
    @ServiceConnection
    final static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:%s".formatted(MongoDbConfig.MONGODB_VERSION));

    @Autowired
    private InterviewPlanner interviewPlanner;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    DateTimeProvider dateTimeProvider;

    @MockitoBean
    CareerDataRepository careerDataRepository;

    CareerData careerData;

    @BeforeEach
    void beforeEach() {
        try {
            File file = new ClassPathResource("career-data.json").getFile();
            careerData = objectMapper.readValue(file, CareerData.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("should create interview plan")
    void shouldCreateInterviewPlan() {
        given(dateTimeProvider.getDateTime()).willReturn("2025-01-13 12:33:45");
        given(careerDataRepository.findById("67e98007bd5c558ba6ad93d6")).willReturn(Optional.of(careerData));
        InterviewContext context = new InterviewContext("67e98007bd5c558ba6ad93d6", "222", "333", "Max");
        List<Topic> topics = interviewPlanner.createPlan(context);

        assertThat(topics).anySatisfy(t -> t.getReason().contains("location"));
    }

}