package com.dmh.GenerateCvu.controller;


import com.dmh.GenerateCvu.service.GenerateCvuService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/Cvu")
@RequiredArgsConstructor
public class GenerateCvuController {

    @Autowired
    private GenerateCvuService generateCvuService;

    @GetMapping("/generate")
    public String generateCvu() {

        return generateCvuService.generateCvu();
    }

}
