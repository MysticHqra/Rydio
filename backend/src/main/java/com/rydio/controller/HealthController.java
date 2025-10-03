package com.rydio.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("")
@CrossOrigin(origins = "http://localhost:3000")
public class HealthController {

    @GetMapping("/health")
    public String health() {
        return "Rydio Backend is running!";
    }
    
    @GetMapping("/test-accounts")
    public String getTestAccounts() {
        return """
            Test Accounts Available:
            1. admin@rydio.com OR admin / admin123 (ADMIN role)
            2. hara@gmail.com OR hara / Hara@1234 (USER role)
            3. test@example.com OR testuser / password123 (USER role)
            
            You can login with either email or username!
            """;
    }
}