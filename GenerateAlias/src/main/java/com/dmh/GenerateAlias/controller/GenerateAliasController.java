package com.dmh.GenerateAlias.controller;


import com.dmh.GenerateAlias.service.GenerateAliasService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/generate-alias")
@RequiredArgsConstructor
public class GenerateAliasController {

   @Autowired
    private final GenerateAliasService generateAliasService;

    @GetMapping
    public ResponseEntity<String> generateAlias() {
        String alias = generateAliasService.generateRandomAlias();
        return ResponseEntity.ok(alias);
    }
}
