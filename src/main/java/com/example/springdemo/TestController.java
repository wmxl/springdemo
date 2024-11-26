package com.example.springdemo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
@Slf4j
public class TestController {

    @Autowired
    Average average;
    @Autowired
    Good good;
    @Autowired
    Excellent excellent;
    @Autowired
    private List<Rating> ratings;

    @GetMapping("/test")
    public String test() {
        log.info("test {}", new Date());
        log.info("average = " + average.getRating());
        log.info("good = " + good.getRating());
        log.info("excellent = " + excellent.getRating());

        for (Rating rating : ratings) {
            log.info("rating.getRating()" + rating.getRating());
        }

        return "test controller ";
    }


}
