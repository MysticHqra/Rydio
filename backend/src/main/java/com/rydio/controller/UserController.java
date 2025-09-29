package com.rydio.controller;

import com.rydio.common.dto.ApiResponse;
import com.rydio.user.entity.User;
import com.rydio.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getUserProfile(
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401)
                .body(new ApiResponse<>(false, "Authorization header required", null));
        }
        
        String token = authHeader.substring(7);
        
        // Extract username from token pattern (simplified for demo)
        String username = null;
        if (token.contains("admin")) {
            username = "admin";
        } else if (token.contains("hara")) {
            username = "hara";
        } else if (token.contains("testuser")) {
            username = "testuser";
        }
        
        if (username == null) {
            return ResponseEntity.status(401)
                .body(new ApiResponse<>(false, "Invalid token", null));
        }
        
        Optional<User> userOptional = userService.findByUsername(username);
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(404)
                .body(new ApiResponse<>(false, "User not found", null));
        }
        
        User user = userOptional.get();
        Map<String, Object> userProfile = new HashMap<>();
        userProfile.put("userId", user.getId());
        userProfile.put("username", user.getUsername());
        userProfile.put("email", user.getEmail());
        userProfile.put("firstName", user.getFirstName());
        userProfile.put("lastName", user.getLastName());
        userProfile.put("role", user.getRole().toString());
        userProfile.put("phoneNumber", user.getPhoneNumber());
        userProfile.put("address", user.getAddress());
        userProfile.put("dateOfBirth", user.getDateOfBirth());
        userProfile.put("driverLicenseNumber", user.getDriverLicenseNumber());
        userProfile.put("driverLicenseExpiry", user.getDriverLicenseExpiry());
        userProfile.put("emailVerified", user.isEmailVerified());
        userProfile.put("isActive", user.isActive());
        userProfile.put("createdAt", user.getCreatedAt());
        userProfile.put("updatedAt", user.getUpdatedAt());
        
        return ResponseEntity.ok(
            new ApiResponse<>(true, "Profile retrieved successfully", userProfile)
        );
    }

    @PutMapping("/profile")
    public ResponseEntity<ApiResponse<Map<String, Object>>> updateUserProfile(
            @RequestBody Map<String, Object> profileData,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401)
                .body(new ApiResponse<>(false, "Authorization header required", null));
        }
        
        String token = authHeader.substring(7);
        
        // Extract username from token pattern (simplified for demo)
        String username = null;
        if (token.contains("admin")) {
            username = "admin";
        } else if (token.contains("hara")) {
            username = "hara";
        } else if (token.contains("testuser")) {
            username = "testuser";
        }
        
        if (username == null) {
            return ResponseEntity.status(401)
                .body(new ApiResponse<>(false, "Invalid token", null));
        }
        
        Optional<User> userOptional = userService.findByUsername(username);
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(404)
                .body(new ApiResponse<>(false, "User not found", null));
        }
        
        User user = userOptional.get();
        
        // Update allowed fields
        if (profileData.containsKey("firstName")) {
            user.setFirstName((String) profileData.get("firstName"));
        }
        if (profileData.containsKey("lastName")) {
            user.setLastName((String) profileData.get("lastName"));
        }
        if (profileData.containsKey("phoneNumber")) {
            user.setPhoneNumber((String) profileData.get("phoneNumber"));
        }
        if (profileData.containsKey("address")) {
            user.setAddress((String) profileData.get("address"));
        }
        
        user = userService.save(user);
        
        Map<String, Object> updatedProfile = new HashMap<>();
        updatedProfile.put("userId", user.getId());
        updatedProfile.put("username", user.getUsername());
        updatedProfile.put("email", user.getEmail());
        updatedProfile.put("firstName", user.getFirstName());
        updatedProfile.put("lastName", user.getLastName());
        updatedProfile.put("role", user.getRole().toString());
        updatedProfile.put("phoneNumber", user.getPhoneNumber());
        updatedProfile.put("address", user.getAddress());
        updatedProfile.put("updatedAt", user.getUpdatedAt());
        
        return ResponseEntity.ok(
            new ApiResponse<>(true, "Profile updated successfully", updatedProfile)
        );
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getUserById(@PathVariable Long userId) {
        Optional<User> userOptional = userService.findById(userId);
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(404)
                .body(new ApiResponse<>(false, "User not found", null));
        }
        
        User user = userOptional.get();
        Map<String, Object> userData = new HashMap<>();
        userData.put("userId", user.getId());
        userData.put("username", user.getUsername());
        userData.put("email", user.getEmail());
        userData.put("firstName", user.getFirstName());
        userData.put("lastName", user.getLastName());
        userData.put("role", user.getRole().toString());
        userData.put("isActive", user.isActive());
        userData.put("createdAt", user.getCreatedAt());
        
        return ResponseEntity.ok(
            new ApiResponse<>(true, "User retrieved successfully", userData)
        );
    }
}