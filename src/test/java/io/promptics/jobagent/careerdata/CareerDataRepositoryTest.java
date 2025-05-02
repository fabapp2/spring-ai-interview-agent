package io.promptics.jobagent.careerdata;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.promptics.jobagent.MongoDbConfig;
import io.promptics.jobagent.careerdata.model.CareerData;
import io.promptics.jobagent.careerdata.model.SectionWithId;
import io.promptics.jobagent.careerdata.model.Work;
import io.promptics.jobagent.utils.NanoIdGenerator;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
@Testcontainers
@TestClassOrder(ClassOrderer.OrderAnnotation.class)
@Import({CareerDataRepository.class, NanoIdGenerator.class})
class CareerDataRepositoryTest {

    private ObjectMapper objectMapper = new ObjectMapper();

    private static String careerDataId;

    @Container
    @ServiceConnection
    final static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:%s".formatted(MongoDbConfig.MONGODB_VERSION));

    @Autowired
    private CareerDataRepository repository;
    private CareerData careerData;

    @BeforeEach
    void beforeEach() {
        initCareerDataIfNotExists();
    }

    @Nested
    @Order(1)
    class CrudCareerDataTest {

        @Test
        @DisplayName("crud")
        void crud() throws JsonProcessingException {
            CareerData saved = initCareerData();

            assertThat(careerData).usingRecursiveComparison().ignoringFields("id").isEqualTo(saved);

            assertThatIdsWereSet(saved.getSkills());
            assertThatIdsWereSet(saved.getAwards());
            assertThatIdsWereSet(saved.getCertificates());
            assertThatIdsWereSet(saved.getEducation());
            assertThatIdsWereSet(saved.getInterests());
            assertThatIdsWereSet(saved.getLanguages());
            assertThatIdsWereSet(saved.getProjects());
            assertThatIdsWereSet(saved.getPublications());
            assertThatIdsWereSet(saved.getReferences());
            assertThatIdsWereSet(saved.getSkills());
            assertThatIdsWereSet(saved.getVolunteer());
            assertThatIdsWereSet(saved.getWork());

//        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(saved));
        }

    }

    @Order(2)
    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class SectionsCrudTest {


        @Test
        @Order(1)
        @DisplayName("add and read work section")
        void addAndReadWorkSection() throws JsonProcessingException {

            List<Work> worksBefore = repository.readSection(careerDataId, Work.class);
            assertThat(worksBefore).hasSize(3);

            Work work = objectMapper.readValue("""
                    {
                        "name": "SomeCompany",
                        "position": "Lead Developer",
                        "startDate": "2020-07",
                        "endDate": "2022-12",
                        "summary": "what a ride.",
                        "highlights": [
                          "Did a great job."
                        ]
                    }
                    """, Work.class);
            repository.addWork(careerDataId, work);
            List<Work> worksAfter = repository.readSection(careerDataId, Work.class);
            assertThat(worksAfter).hasSize(4);
            assertThatIdsWereSet(worksAfter);
        }

        @Test
        @Order(2)
        @DisplayName("update work section")
        void updateWorkSection() throws JsonProcessingException {
            List<Work> worksBefore = repository.readSection(careerDataId, Work.class);
            assertThat(worksBefore).hasSize(4);

            Work work = objectMapper.readValue("""
                    {
                        "name": "SomeCompany",
                        "position": "Lead Developer",
                        "startDate": "2020-07",
                        "endDate": "2022-12",
                        "summary": "what a ride.",
                        "highlights": [
                          "Did a great job."
                        ]
                    }
                    """, Work.class);
            repository.updateWork(careerDataId, List.of(work));
            List<Work> worksAfter = repository.readSection(careerDataId, Work.class);
            assertThat(worksAfter).hasSize(1);
            assertThatIdsWereSet(worksAfter);
        }



    }
    /**
     * Used to init carrer data when test run in isolation
     */
    private void initCareerDataIfNotExists() {
        if(careerDataId == null) {
            initCareerData();
        }
    }

    private void assertThatIdsWereSet(List<? extends SectionWithId> section) {
        section.stream()
                .forEach(s -> assertThat(s.getId()).isNotNull());
    }

    private @NotNull CareerData initCareerData() {
        careerData = readCareerData();
        CareerData saved = repository.save(careerData);
        careerDataId = saved.getId();
        return saved;
    }

    private CareerData readCareerData() {
        try {
            File file = new ClassPathResource("career-data-complete.json").getFile();
            return objectMapper.readValue(file, CareerData.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}