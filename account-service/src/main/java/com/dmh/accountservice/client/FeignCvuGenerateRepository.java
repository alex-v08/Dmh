package com.dmh.accountservice.client;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "GenerateCvu", url = "localhost:8088")
public interface FeignCvuGenerateRepository {
    @RequestMapping(method = RequestMethod.GET, value = "/Cvu/generate")
    String generateCvu();
}
