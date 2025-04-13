package io.promptics.jobagent.careerdata;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CareerDataManagerTest {

    @Autowired
    private CareerDataManager careerDataManager;

    @Test
    @DisplayName("get career data by id")
    void getCareerDataById() {
        String result = careerDataManager.run("""
                                    Get data for id "123abc
                                    """);
        System.out.println(result);
    }

    @Test
    @DisplayName("modify email in basis")
    void modifyEmailInBasis() {
        String result = careerDataManager.run("Change email from max.wurst@techgiant.com to max@wurst.com for id 12423");
    }
}