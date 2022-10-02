package com.example.springdemo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@Slf4j
public class TestController {


    @GetMapping("/test")
    public String test() {
        log.info("test {}", new Date());
        return "test controller ";
    }


}
