package com.dmh.GenerateAlias.controller.service;


import jakarta.annotation.PostConstruct;
import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class GenerateAliasService {

private List<String> words = new ArrayList<String>();
private final Random random = new Random();


   @PostConstruct
   public void loadWords() throws IOException {
       try (BufferedReader reader = new BufferedReader(new InputStreamReader(new ClassPathResource("../resources/alias_word.txt").getInputStream()))) {
           String line;
           while ((line = reader.readLine()) != null) {
               words.add(line);
           }
       }


   }

    public String generateRandomAlias() {
        if (words.size() < 3) {
            throw new IllegalStateException("Not enough words to generate an alias");
        }
        String alias = words.get(random.nextInt(words.size())) + "." +
                words.get(random.nextInt(words.size())) + "." +
                words.get(random.nextInt(words.size()));
        return alias;
    }

}
