package com.example.springdemo;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/moment")
public class MomentTestController {

    @GetMapping("/getDiscoverFeedListBackupData")
    public Object getDiscoverFeedListBackupData(Integer pageSize) {
        return "/moment/getDiscoverFeedListBackupData";
    }
    @PostMapping("/delActivityAwardInfoCache")
    public String delActivityAwardInfoCache(Long activityId) {
        return "/moment/delActivityAwardInfoCache";
    }
    @RequestMapping("/getByPositionCodeNew")
    public String getByPositionCode(int positionCode, String source) {
        return "/moment/getByPositionCodeNew";
    }

}

