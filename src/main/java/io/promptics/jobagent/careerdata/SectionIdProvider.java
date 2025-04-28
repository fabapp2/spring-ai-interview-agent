package io.promptics.jobagent.careerdata;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class SectionIdProvider {
    public String getId() {
        return NanoIdUtils.randomNanoId(new Random(), "0123456789abcdefghijklmnopqrstuvwxyz".toCharArray(), 8);
    }
}
