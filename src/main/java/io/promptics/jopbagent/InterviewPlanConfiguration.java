package io.promptics.jopbagent;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.core.MongoClientFactoryBean;

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
