package io.promptics.jobagent;

import org.springframework.context.annotation.Configuration;

@Configuration
public class MongoDbConfig {

    public static String MONGODB_VERSION = "8.0.4";

    protected String getDatabaseName() {
        return "interview_plan";
    }

}
