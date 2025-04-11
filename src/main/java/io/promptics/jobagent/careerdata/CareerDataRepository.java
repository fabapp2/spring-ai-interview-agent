package io.promptics.jobagent.careerdata;

import io.promptics.jobagent.careerdata.model.CareerData;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CareerDataRepository extends MongoRepository<CareerData, String> {
}
