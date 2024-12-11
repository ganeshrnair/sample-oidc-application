package com.example.adminportal;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
class LimitsapiController {

    @GetMapping("/public")
    public String publicEndpoint() {
        return "This is a public endpoint and does not require authentication.";
    }

    @GetMapping("/secure")
    public String secureEndpoint() {
        return "This is a secure endpoint and requires authentication.";
    }

    @GetMapping("/admin")
    public String adminEndpoint() {
        return "This is an admin-only endpoint.";
    }
}
