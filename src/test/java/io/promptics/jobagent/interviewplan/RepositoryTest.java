package io.promptics.jobagent.interviewplan;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.promptics.jobagent.CareerDataInterviewConfig;
import io.promptics.jobagent.MongoDbConfig;
import org.json.JSONException;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
@Testcontainers
@Import(CareerDataInterviewConfig.class)
public abstract class RepositoryTest<T, ID, R extends MongoRepository<T, ID>> {

    @Container
    @ServiceConnection
    final static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:%s".formatted(MongoDbConfig.MONGODB_VERSION));

    @Autowired
    ObjectMapper objectMapper;

    private final Class<T> entityClass;

    protected RepositoryTest(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    protected abstract MongoRepository<T, ID> getRepository();

    protected void assertPersisted(String given) throws JSONException {
        T topic = deserialize(given, entityClass);
        T saved = getRepository().save(topic);
        String json = serialize(saved);
        assertIdWasSet(json);
        JSONAssert.assertEquals(given, json, JSONCompareMode.LENIENT);
    }

    private void assertIdWasSet(String json) {
        try {
            JsonNode jsonNode = objectMapper.readTree(json);
            assertThat(jsonNode.get("id")).isNotNull();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    protected String serialize(T saved) {
        try {
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(saved);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    protected T deserialize(String json, Class<T> topicClass) {
        try {
            return objectMapper.readValue(json, topicClass);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
