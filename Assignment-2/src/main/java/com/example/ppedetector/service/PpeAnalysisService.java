package com.example.ppedetector.service;

import org.springframework.stereotype.Service;
import java.util.Random;

@Service
public class PpeAnalysisService {

    public boolean analyzePpePresence(String filename) {
        // Mock logic: Random true or false
        return new Random().nextBoolean();
    }
}
