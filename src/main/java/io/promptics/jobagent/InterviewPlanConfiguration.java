package io.promptics.jobagent;

import org.springframework.context.annotation.Configuration;

@Configuration
public class InterviewPlanConfiguration {

    protected String getDatabaseName() {
        return "interview_plan";
    }

//    public @Bean com.mongodb.client.MongoClient mongoClient() {
//        return com.mongodb.client.MongoClients.create("mongodb://localhost:27017");
//    }

//    public @Bean MongoClientFactoryBean mongo() {
//        MongoClientFactoryBean mongo = new MongoClientFactoryBean();
//        mongo.setHost("localhost");
//        m
//        return mongo;
//    }
}
