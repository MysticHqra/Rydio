package com.rydio.recommendation.service;

import com.rydio.recommendation.dto.VehicleRecommendation;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PersonalizedRecommendationService {
    
    // Mock user booking history - in real implementation, this would come from database
    private Map<Long, List<Map<String, Object>>> getUserBookingHistory(Long userId) {
        Map<Long, List<Map<String, Object>>> userHistories = new HashMap<>();
        
        // Mock data for user preferences
        List<Map<String, Object>> user1History = Arrays.asList(
            createBookingHistory("SCOOTER", "Honda", "Activa", "solo", 3),
            createBookingHistory("BIKE", "Hero", "Splendor", "city", 2),
            createBookingHistory("SCOOTER", "Ola", "S1 Pro", "leisure", 1)
        );
        
        List<Map<String, Object>> user2History = Arrays.asList(
            createBookingHistory("CAR", "Maruti", "Swift", "family", 2),
            createBookingHistory("CAR", "Hyundai", "i20", "business", 1),
            createBookingHistory("CAR", "Toyota", "Innova", "family", 3)
        );
        
        userHistories.put(1L, user1History);
        userHistories.put(2L, user2History);
        
        return userHistories;
    }
    
    private Map<String, Object> createBookingHistory(String vehicleType, String brand, String model, 
                                                   String tripType, int bookingCount) {
        Map<String, Object> history = new HashMap<>();
        history.put("vehicleType", vehicleType);
        history.put("brand", brand);
        history.put("model", model);
        history.put("tripType", tripType);
        history.put("bookingCount", bookingCount);
        return history;
    }
    
    public List<VehicleRecommendation> getPersonalizedRecommendations(Long userId, 
                                                                    List<VehicleRecommendation> baseRecommendations) {
        Map<Long, List<Map<String, Object>>> allHistories = getUserBookingHistory(userId);
        List<Map<String, Object>> userHistory = allHistories.getOrDefault(userId, new ArrayList<>());
        
        if (userHistory.isEmpty()) {
            return baseRecommendations; // No personalization if no history
        }
        
        // Analyze user preferences
        Map<String, Integer> vehicleTypePreferences = new HashMap<>();
        Map<String, Integer> brandPreferences = new HashMap<>();
        Map<String, Integer> tripTypePreferences = new HashMap<>();
        
        for (Map<String, Object> booking : userHistory) {
            String vehicleType = booking.get("vehicleType").toString();
            String brand = booking.get("brand").toString();
            String tripType = booking.get("tripType").toString();
            int count = ((Number) booking.get("bookingCount")).intValue();
            
            vehicleTypePreferences.merge(vehicleType, count, Integer::sum);
            brandPreferences.merge(brand, count, Integer::sum);
            tripTypePreferences.merge(tripType, count, Integer::sum);
        }
        
        // Apply personalization boost to recommendations
        for (VehicleRecommendation recommendation : baseRecommendations) {
            double personalizedScore = recommendation.getMatchScore();
            List<String> personalizedCriteria = new ArrayList<>(recommendation.getMatchedCriteria());
            
            // Boost for preferred vehicle type
            String vehicleType = recommendation.getVehicleType();
            if (vehicleTypePreferences.containsKey(vehicleType)) {
                int preference = vehicleTypePreferences.get(vehicleType);
                double boost = Math.min(0.2, preference * 0.05); // Max 0.2 boost
                personalizedScore += boost;
                personalizedCriteria.add("Matches your preferred vehicle type");
            }
            
            // Boost for preferred brand
            String brand = recommendation.getBrand();
            if (brandPreferences.containsKey(brand)) {
                int preference = brandPreferences.get(brand);
                double boost = Math.min(0.15, preference * 0.05); // Max 0.15 boost
                personalizedScore += boost;
                personalizedCriteria.add("From your preferred brand");
            }
            
            // Update recommendation with personalized data
            recommendation.setMatchScore(Math.min(personalizedScore, 1.0)); // Cap at 1.0
            recommendation.setMatchedCriteria(personalizedCriteria);
            
            // Add personalized reason
            String currentReason = recommendation.getReason();
            if (vehicleTypePreferences.containsKey(vehicleType) || brandPreferences.containsKey(brand)) {
                recommendation.setReason(currentReason + " Based on your booking history, this matches your preferences.");
            }
        }
        
        // Re-sort by updated match scores
        baseRecommendations.sort((a, b) -> Double.compare(b.getMatchScore(), a.getMatchScore()));
        
        return baseRecommendations;
    }
    
    public String generatePersonalizedInsight(Long userId) {
        Map<Long, List<Map<String, Object>>> allHistories = getUserBookingHistory(userId);
        List<Map<String, Object>> userHistory = allHistories.getOrDefault(userId, new ArrayList<>());
        
        if (userHistory.isEmpty()) {
            return "Start booking with us to get personalized recommendations based on your preferences!";
        }
        
        // Find most preferred vehicle type
        Map<String, Integer> vehicleTypeCount = new HashMap<>();
        for (Map<String, Object> booking : userHistory) {
            String vehicleType = booking.get("vehicleType").toString();
            int count = ((Number) booking.get("bookingCount")).intValue();
            vehicleTypeCount.merge(vehicleType, count, Integer::sum);
        }
        
        String preferredType = vehicleTypeCount.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("UNKNOWN");
        
        switch (preferredType) {
            case "SCOOTER":
                return "You seem to love scooters! They're perfect for city commuting and solo rides. We've prioritized similar options for you.";
            case "CAR":
                return "Cars are your go-to choice! Great for comfort and longer trips. We've highlighted premium car options that match your style.";
            case "BIKE":
                return "Bikes are your preferred mode of transport! Economical and efficient for daily use. Here are some great bike recommendations.";
            case "BICYCLE":
                return "You're eco-conscious and love bicycles! Perfect for short distances and staying fit. We've found some great cycling options.";
            default:
                return "Based on your diverse booking history, we've curated a mix of vehicle options that suit your varied needs.";
        }
    }
}