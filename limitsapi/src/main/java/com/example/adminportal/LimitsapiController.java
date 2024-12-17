package com.example.adminportal;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
class LimitsapiController {

    @GetMapping("/dailylimit")
    public String getDailyLimitForCustomers(@AuthenticationPrincipal Jwt jwt) {
        String username = jwt.getClaim("preferred_username");
        String userId = jwt.getSubject(); // The "sub" claim
        String scopes = jwt.getClaim("scp");

        return String.format("UserId: %s, Username: %s, Scopes: %s", userId, username, scopes);

    }

}
