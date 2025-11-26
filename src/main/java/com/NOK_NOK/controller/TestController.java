package com.NOK_NOK.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hello")
@Log4j2
public class TestController {

    @GetMapping
    public String test(){
        return "Test 완료";
    }
}
