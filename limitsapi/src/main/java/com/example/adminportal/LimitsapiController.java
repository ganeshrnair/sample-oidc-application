package com.example.adminportal;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
class LimitsapiController {

    @GetMapping("/dailylimit")
    public String getDailyLimitForCustomers() {
        return "This is a secure endpoint and requires authentication.";
    }

}
