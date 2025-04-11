package io.promptics.jopbagent;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PreProcessorTest {

    @Autowired
    PreProcessor preProcessor;

    @Test
    @DisplayName("simple prompt")
    void simplePrompt() {
        String run = preProcessor.run();
        System.out.println(run);
    }
}