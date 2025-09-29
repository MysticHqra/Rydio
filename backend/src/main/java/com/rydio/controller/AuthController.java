package com.rydio.controller;

import com.rydio.common.dto.ApiResponse;
import com.rydio.user.entity.User;
import com.rydio.user.service.UserService;
import com.rydio.config.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Map<String, Object>>> register(@RequestBody Map<String, Object> registerData) {
        try {
            System.out.println("Registration request received: " + registerData);
            
            String firstName = (String) registerData.get("firstName");
            String lastName = (String) registerData.get("lastName");
            String email = (String) registerData.get("email");
            String username = (String) registerData.get("username");
            String password = (String) registerData.get("password");
            String phoneNumber = (String) registerData.get("phoneNumber");
            String address = (String) registerData.get("address");
            
            // Validate required fields
            if (firstName == null || firstName.trim().isEmpty() || 
                lastName == null || lastName.trim().isEmpty() || 
                email == null || email.trim().isEmpty() ||
                username == null || username.trim().isEmpty() ||
                password == null || password.trim().isEmpty()) {
                System.out.println("Registration validation failed");
                return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, "All required fields must be provided", null));
            }
            
            // Check if user already exists
            if (userService.existsByEmail(email.trim())) {
                return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, "Email already exists", null));
            }
            
            if (userService.existsByUsername(username.trim())) {
                return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, "Username already exists", null));
            }
            
            // Create new user
            User newUser = userService.createUser(
                username.trim(),
                email.trim(),
                passwordEncoder.encode(password.trim()), // Hash the password
                firstName.trim(),
                lastName.trim(),
                User.Role.USER
            );
            
            if (phoneNumber != null && !phoneNumber.trim().isEmpty()) {
                newUser.setPhoneNumber(phoneNumber.trim());
            }
            if (address != null && !address.trim().isEmpty()) {
                newUser.setAddress(address.trim());
            }
            
            newUser = userService.save(newUser);
            
            // Generate JWT tokens
            String token = jwtUtil.generateToken(newUser);
            String refreshToken = jwtUtil.generateRefreshToken(newUser);
            
            // Return user data (without password)
            Map<String, Object> userData = new HashMap<>();
            userData.put("userId", newUser.getId());
            userData.put("firstName", newUser.getFirstName());
            userData.put("lastName", newUser.getLastName());
            userData.put("email", newUser.getEmail());
            userData.put("username", newUser.getUsername());
            userData.put("role", newUser.getRole().toString());
            userData.put("token", token);
            userData.put("refreshToken", refreshToken);
            
            System.out.println("Registration successful for: " + email);
            return ResponseEntity.ok(
                new ApiResponse<>(true, "User registered successfully", userData)
            );
            
        } catch (Exception e) {
            System.out.println("Registration error: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                .body(new ApiResponse<>(false, "Registration failed: " + e.getMessage(), null));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<Map<String, Object>>> login(@RequestBody Map<String, Object> loginData) {
        try {
            System.out.println("Login request received: " + loginData);
            
            String email = (String) loginData.get("email");
            String usernameOrEmail = (String) loginData.get("usernameOrEmail");
            String password = (String) loginData.get("password");
            
            // Use email if provided, otherwise use usernameOrEmail
            String loginIdentifier = email != null ? email : usernameOrEmail;
            
            // Validate required fields
            if (loginIdentifier == null || loginIdentifier.trim().isEmpty() || 
                password == null || password.trim().isEmpty()) {
                System.out.println("Validation failed - identifier: " + loginIdentifier + ", password: " + (password != null ? "[PROVIDED]" : "[NULL]"));
                return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, "Email/Username and password are required", null));
            }
            
            System.out.println("Attempting login for: " + loginIdentifier);
            
            // Find user by username or email
            Optional<User> userOptional = userService.findByUsernameOrEmail(loginIdentifier.trim());
            
            if (userOptional.isEmpty()) {
                System.out.println("User not found: " + loginIdentifier);
                return ResponseEntity.status(401)
                    .body(new ApiResponse<>(false, "Invalid credentials", null));
            }
            
            User user = userOptional.get();
            
            // Check password using password encoder
            if (!passwordEncoder.matches(password.trim(), user.getPassword())) {
                System.out.println("Invalid password for user: " + loginIdentifier);
                return ResponseEntity.status(401)
                    .body(new ApiResponse<>(false, "Invalid credentials", null));
            }
            
            // Check if user is active
            if (!user.isActive()) {
                return ResponseEntity.status(401)
                    .body(new ApiResponse<>(false, "Account is disabled", null));
            }
            
            // Generate JWT tokens
            String token = jwtUtil.generateToken(user);
            String refreshToken = jwtUtil.generateRefreshToken(user);
            
            // Create response data
            Map<String, Object> userData = new HashMap<>();
            userData.put("userId", user.getId());
            userData.put("username", user.getUsername());
            userData.put("firstName", user.getFirstName());
            userData.put("lastName", user.getLastName());
            userData.put("email", user.getEmail());
            userData.put("role", user.getRole().toString());
            userData.put("token", token);
            userData.put("refreshToken", refreshToken);
            
            System.out.println("Login successful for: " + loginIdentifier);
            return ResponseEntity.ok(
                new ApiResponse<>(true, "Login successful", userData)
            );
            
        } catch (Exception e) {
            System.out.println("Login error: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                .body(new ApiResponse<>(false, "Login failed: " + e.getMessage(), null));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<String>> logout() {
        // In real app, invalidate token
        return ResponseEntity.ok(
            new ApiResponse<>(true, "Logged out successfully", null)
        );
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getCurrentUser(
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        
        // Extract user ID from token (simplified for demo)
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            
            // Extract username from token pattern
            String username = null;
            if (token.contains("admin")) {
                username = "admin";
            } else if (token.contains("hara")) {
                username = "hara";
            } else if (token.contains("testuser")) {
                username = "testuser";
            }
            
            if (username != null) {
                Optional<User> userOptional = userService.findByUsername(username);
                if (userOptional.isPresent()) {
                    User user = userOptional.get();
                    Map<String, Object> userData = new HashMap<>();
                    userData.put("id", user.getId());
                    userData.put("firstName", user.getFirstName());
                    userData.put("lastName", user.getLastName());
                    userData.put("email", user.getEmail());
                    userData.put("username", user.getUsername());
                    userData.put("role", user.getRole().toString());
                    
                    return ResponseEntity.ok(
                        new ApiResponse<>(true, "User data retrieved", userData)
                    );
                }
            }
        }
        
        return ResponseEntity.status(401)
            .body(new ApiResponse<>(false, "Unauthorized", null));
    }
}