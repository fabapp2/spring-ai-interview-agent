package io.promptics.jobagent;

import org.springframework.context.annotation.Configuration;

@Configuration
public class MongoDbConfig {

    protected String getDatabaseName() {
        return "interview_plan";
    }

}
