package com.rydio.recommendation.controller;

import com.rydio.common.dto.ApiResponse;
import com.rydio.recommendation.dto.RecommendationRequest;
import com.rydio.recommendation.dto.RecommendationResponse;
import com.rydio.recommendation.dto.VehicleRecommendation;
import com.rydio.recommendation.service.SmartRecommendationService;
import com.rydio.recommendation.service.PersonalizedRecommendationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/recommendations")
@CrossOrigin(origins = "http://localhost:3000")
public class RecommendationController {
    
    @Autowired
    private SmartRecommendationService smartRecommendationService;
    
    @Autowired
    private PersonalizedRecommendationService personalizedRecommendationService;
    
    @GetMapping("/health")
    public ResponseEntity<ApiResponse<String>> healthCheck() {
        return ResponseEntity.ok(
            new ApiResponse<>(true, "Recommendation service is running", "OK")
        );
    }
    
    @PostMapping("/smart")
    public ResponseEntity<ApiResponse<RecommendationResponse>> getSmartRecommendations(
            @RequestBody RecommendationRequest request) {
        try {
            RecommendationResponse recommendations = smartRecommendationService.getSmartRecommendations(request);
            
            // Get current user for personalization
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated() && 
                !authentication.getName().equals("anonymousUser")) {
                
                // For demo purposes, use a mock user ID. In real implementation, 
                // extract user ID from authentication token
                Long userId = getUserIdFromAuthentication(authentication);
                
                // Apply personalization
                List<VehicleRecommendation> personalizedRecs = personalizedRecommendationService
                        .getPersonalizedRecommendations(userId, recommendations.getRecommendations());
                recommendations.setRecommendations(personalizedRecs);
                
                // Add personalized insight
                String insight = personalizedRecommendationService.generatePersonalizedInsight(userId);
                recommendations.setPersonalizedMessage(recommendations.getPersonalizedMessage() + " " + insight);
            }
            
            return ResponseEntity.ok(
                new ApiResponse<>(true, "Smart recommendations generated successfully", recommendations)
            );
        } catch (Exception e) {
            e.printStackTrace(); // This will help us see the error in logs
            return ResponseEntity.internalServerError()
                .body(new ApiResponse<>(false, "Failed to generate recommendations: " + e.getMessage(), null));
        }
    }
    
    @GetMapping("/quick")
    public ResponseEntity<ApiResponse<List<VehicleRecommendation>>> getQuickRecommendations(
            @RequestParam(required = false) String tripType,
            @RequestParam(required = false) Integer passengers,
            @RequestParam(required = false) String duration) {
        try {
            RecommendationRequest request = new RecommendationRequest();
            request.setTripType(tripType);
            request.setPassengerCount(passengers);
            request.setDuration(duration);
            
            RecommendationResponse response = smartRecommendationService.getSmartRecommendations(request);
            
            return ResponseEntity.ok(
                new ApiResponse<>(true, "Quick recommendations generated", response.getRecommendations())
            );
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(new ApiResponse<>(false, "Failed to generate quick recommendations: " + e.getMessage(), null));
        }
    }
    
    @GetMapping("/add-ons")
    public ResponseEntity<ApiResponse<List<String>>> getRecommendedAddOns(
            @RequestParam String tripType,
            @RequestParam(required = false) String vehicleType,
            @RequestParam(required = false) String weather) {
        try {
            RecommendationRequest request = new RecommendationRequest();
            request.setTripType(tripType);
            request.setWeatherCondition(weather);
            
            RecommendationResponse response = smartRecommendationService.getSmartRecommendations(request);
            
            return ResponseEntity.ok(
                new ApiResponse<>(true, "Add-on recommendations generated", response.getSuggestedAddOns())
            );
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(new ApiResponse<>(false, "Failed to generate add-on recommendations: " + e.getMessage(), null));
        }
    }
    
    @GetMapping("/personalized-insight")
    public ResponseEntity<ApiResponse<Map<String, String>>> getPersonalizedInsight() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated() || 
                authentication.getName().equals("anonymousUser")) {
                return ResponseEntity.ok(
                    new ApiResponse<>(true, "No personalized data available", 
                        Map.of("insight", "Login to get personalized recommendations based on your booking history!"))
                );
            }
            
            Long userId = getUserIdFromAuthentication(authentication);
            String insight = personalizedRecommendationService.generatePersonalizedInsight(userId);
            
            return ResponseEntity.ok(
                new ApiResponse<>(true, "Personalized insight generated", Map.of("insight", insight))
            );
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(new ApiResponse<>(false, "Failed to generate insight: " + e.getMessage(), null));
        }
    }
    
    // Helper method to extract user ID from authentication
    // In real implementation, this would extract from JWT token
    private Long getUserIdFromAuthentication(Authentication authentication) {
        String username = authentication.getName();
        // Mock logic - in real implementation, query user repository
        if ("admin@rydio.com".equals(username)) {
            return 1L;
        } else if ("user@rydio.com".equals(username)) {
            return 2L;
        }
        return 1L; // Default to user 1
    }
}