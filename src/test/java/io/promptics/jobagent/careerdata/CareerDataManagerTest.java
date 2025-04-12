package io.promptics.jobagent.careerdata;

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
        String result = careerDataManager.run();
        System.out.println(result);
    }

}