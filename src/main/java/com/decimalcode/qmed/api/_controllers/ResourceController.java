package com.decimalcode.qmed.api._controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class ResourceController {
    @GetMapping("/diagnosis")
    public String getConsultation(@RequestParam("diagnosis") String diagnosis) {
        return "getConsultationn";
    }

}
