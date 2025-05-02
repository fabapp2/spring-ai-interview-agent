package io.promptics.jobagent.utils;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class NanoIdGenerator {
    public String generateId() {
        return NanoIdUtils.randomNanoId(new Random(), "0123456789abcdefghijklmnopqrstuvwxyz".toCharArray(), 8);
    }
}
