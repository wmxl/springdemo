package com.example.springdemo;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

public
@Component
@Order(Ordered.LOWEST_PRECEDENCE)
class Average implements Rating {

    @Override
    public int getRating() {
        return 3;
    }
}