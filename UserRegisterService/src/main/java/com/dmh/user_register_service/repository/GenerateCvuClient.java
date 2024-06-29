package com.dmh.user_register_service.repository;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "GenerateCvu", url = "localhost:8089")
public interface GenerateCvuClient {

    @RequestMapping(method = RequestMethod.GET, value = "/Cvu/generate")
    String generateCvu();
}
