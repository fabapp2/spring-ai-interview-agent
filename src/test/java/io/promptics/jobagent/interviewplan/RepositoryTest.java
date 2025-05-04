package io.promptics.jobagent.interviewplan;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.ValidationMessage;
import io.promptics.jobagent.InterviewConfig;
import io.promptics.jobagent.MongoDbConfig;
import io.promptics.jobagent.interviewplan.model.Topic;
import io.promptics.jobagent.interviewplan.model.TopicThread;
import io.promptics.jobagent.utils.JsonValidator;
import org.json.JSONException;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
@Import(InterviewConfig.class)
public abstract class RepositoryTest<T, ID, R extends MongoRepository<T, ID>> {

    final static MongoDBContainer mongoDBContainer;

    static {
        mongoDBContainer = new MongoDBContainer("mongo:%s".formatted(MongoDbConfig.MONGODB_VERSION));
        mongoDBContainer.start();
    }

    @DynamicPropertySource
    static void mongoProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @Autowired
    ObjectMapper objectMapper;

    private final Class<T> entityClass;

    protected RepositoryTest(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    protected abstract MongoRepository<T, ID> getRepository();

    protected void assertPersisted(String given) throws JSONException {
        String schema = "/schemas/plan/topics-schema.json";
        if(entityClass.isAssignableFrom(TopicThread.class)) {
            schema = "/schemas/plan/threads-schema.json";
        }
        Set<ValidationMessage> validationMessages = JsonValidator.validateJson(given, schema);
        assertThat(validationMessages).isEmpty();
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
