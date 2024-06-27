package com.dmh.GenerateCvu.service;

import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class GenerateCvuService {
    private final Random random = new Random();

    public String generateCvu() {
        StringBuilder cvu = new StringBuilder();
        for (int i = 0; i < 22; i++) {
            cvu.append(random.nextInt(10));
        }
        return cvu.toString();
    }
}
