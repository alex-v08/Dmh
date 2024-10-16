package com.dmh.accountservice.repository.client;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "GenerateAlias", url = "localhost:8087")

public interface FeignAliasGenerateRepository {
    @RequestMapping(method = RequestMethod.GET, value = "/generate-alias")
    String generateAlias();
}
