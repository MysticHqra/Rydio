package com.rydio.recommendation.service;

import com.rydio.recommendation.dto.RecommendationRequest;
import com.rydio.recommendation.dto.RecommendationResponse;
import com.rydio.recommendation.dto.VehicleRecommendation;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SmartRecommendationService {
    
    // Mock vehicle data - in real implementation, this would come from database
    private List<Map<String, Object>> getMockVehicles() {
        List<Map<String, Object>> vehicles = new ArrayList<>();
        
        vehicles.add(createVehicle(1L, "Honda", "Activa 6G", "SCOOTER", "PETROL", 4, 600.0, 25.0,
                "Mumbai Central", "Perfect for solo city rides"));
        vehicles.add(createVehicle(2L, "Maruti", "Swift", "CAR", "PETROL", 4, 1800.0, 75.0,
                "Mumbai Central", "Comfortable sedan for small families"));
        vehicles.add(createVehicle(3L, "Hero", "Splendor Plus", "BIKE", "PETROL", 2, 480.0, 20.0,
                "Mumbai Central", "Economical bike for daily commute"));
        vehicles.add(createVehicle(4L, "Hyundai", "i20", "CAR", "PETROL", 5, 2200.0, 90.0,
                "Mumbai Central", "Premium hatchback with modern features"));
        vehicles.add(createVehicle(5L, "Ola", "S1 Pro", "SCOOTER", "ELECTRIC", 2, 800.0, 35.0,
                "Mumbai Central", "Eco-friendly electric scooter"));
        vehicles.add(createVehicle(6L, "Mahindra", "Thar", "CAR", "DIESEL", 4, 3500.0, 145.0,
                "Mumbai Central", "Rugged SUV for adventure trips"));
        vehicles.add(createVehicle(7L, "Trek", "City Bike", "BICYCLE", "NONE", 1, 200.0, 10.0,
                "Mumbai Central", "Eco-friendly option for short distances"));
        vehicles.add(createVehicle(8L, "Toyota", "Innova", "CAR", "DIESEL", 7, 2800.0, 115.0,
                "Mumbai Central", "Spacious MPV for large families"));
        
        return vehicles;
    }
    
    private Map<String, Object> createVehicle(Long id, String brand, String model, String type, 
                                            String fuel, int seats, double dailyRate, double hourlyRate,
                                            String location, String description) {
        Map<String, Object> vehicle = new HashMap<>();
        vehicle.put("id", id);
        vehicle.put("brand", brand);
        vehicle.put("model", model);
        vehicle.put("vehicleType", type);
        vehicle.put("fuelType", fuel);
        vehicle.put("seatCount", seats);
        vehicle.put("dailyRate", dailyRate);
        vehicle.put("hourlyRate", hourlyRate);
        vehicle.put("location", location);
        vehicle.put("description", description);
        vehicle.put("status", "AVAILABLE");
        return vehicle;
    }
    
    public RecommendationResponse getSmartRecommendations(RecommendationRequest request) {
        List<Map<String, Object>> vehicles = getMockVehicles();
        List<VehicleRecommendation> recommendations = new ArrayList<>();
        
        for (Map<String, Object> vehicle : vehicles) {
            VehicleRecommendation recommendation = analyzeVehicleMatch(vehicle, request);
            if (recommendation.getMatchScore() > 0.3) { // Only include vehicles with decent match
                recommendations.add(recommendation);
            }
        }
        
        // Sort by match score (highest first)
        recommendations.sort((a, b) -> Double.compare(b.getMatchScore(), a.getMatchScore()));
        
        // Limit to top 5 recommendations
        recommendations = recommendations.stream().limit(5).collect(Collectors.toList());
        
        RecommendationResponse response = new RecommendationResponse();
        response.setRecommendations(recommendations);
        response.setSuggestedAddOns(generateAddOnRecommendations(request));
        response.setPersonalizedMessage(generatePersonalizedMessage(request, recommendations));
        response.setTripTypeAnalysis(analyzeTripType(request));
        
        return response;
    }
    
    private VehicleRecommendation analyzeVehicleMatch(Map<String, Object> vehicle, RecommendationRequest request) {
        double score = 0.0;
        List<String> matchedCriteria = new ArrayList<>();
        StringBuilder reasonBuilder = new StringBuilder();
        
        String vehicleType = vehicle.get("vehicleType").toString();
        String fuelType = vehicle.get("fuelType").toString();
        int seatCount = ((Number) vehicle.get("seatCount")).intValue();
        double dailyRate = ((Number) vehicle.get("dailyRate")).doubleValue();
        double hourlyRate = ((Number) vehicle.get("hourlyRate")).doubleValue();
        
        // Trip type matching
        if (request.getTripType() != null) {
            switch (request.getTripType().toLowerCase()) {
                case "solo":
                    if (vehicleType.equals("BIKE") || vehicleType.equals("SCOOTER")) {
                        score += 0.4;
                        matchedCriteria.add("Perfect for solo rides");
                        reasonBuilder.append("Ideal for solo travel. ");
                    }
                    break;
                case "family":
                    if (vehicleType.equals("CAR") && seatCount >= 4) {
                        score += 0.4;
                        matchedCriteria.add("Family-friendly vehicle");
                        reasonBuilder.append("Great for family trips. ");
                    }
                    break;
                case "business":
                    if (vehicleType.equals("CAR") && !vehicle.get("brand").toString().equals("Trek")) {
                        score += 0.3;
                        matchedCriteria.add("Professional appearance");
                        reasonBuilder.append("Professional and reliable. ");
                    }
                    break;
                case "leisure":
                    if (vehicleType.equals("BICYCLE") || fuelType.equals("ELECTRIC")) {
                        score += 0.3;
                        matchedCriteria.add("Eco-friendly option");
                        reasonBuilder.append("Perfect for leisure activities. ");
                    }
                    break;
                case "long_distance":
                    if (vehicleType.equals("CAR") && fuelType.equals("DIESEL")) {
                        score += 0.4;
                        matchedCriteria.add("Fuel efficient for long trips");
                        reasonBuilder.append("Excellent for long-distance travel. ");
                    }
                    break;
                case "city":
                    if (vehicleType.equals("SCOOTER") || vehicleType.equals("BIKE")) {
                        score += 0.3;
                        matchedCriteria.add("Perfect for city navigation");
                        reasonBuilder.append("Easy to navigate in city traffic. ");
                    }
                    break;
            }
        }
        
        // Passenger count matching
        if (request.getPassengerCount() != null) {
            if (seatCount >= request.getPassengerCount()) {
                score += 0.2;
                matchedCriteria.add("Adequate seating capacity");
            } else {
                score -= 0.2; // Penalty for insufficient capacity
            }
        }
        
        // Budget considerations
        if (request.getMaxBudget() != null) {
            double rate = "short".equals(request.getDuration()) ? hourlyRate * 4 : dailyRate;
            if (rate <= request.getMaxBudget()) {
                score += 0.2;
                matchedCriteria.add("Within budget");
            } else {
                score -= 0.1;
            }
        }
        
        // Fuel type preference
        if (request.getPreferredFuelType() != null) {
            if (fuelType.equalsIgnoreCase(request.getPreferredFuelType())) {
                score += 0.1;
                matchedCriteria.add("Preferred fuel type");
            }
        }
        
        // Weather considerations
        if (request.getWeatherCondition() != null) {
            switch (request.getWeatherCondition().toLowerCase()) {
                case "rainy":
                    if (vehicleType.equals("CAR")) {
                        score += 0.1;
                        matchedCriteria.add("Weather protection");
                        reasonBuilder.append("Provides protection from rain. ");
                    }
                    break;
                case "sunny":
                    if (vehicleType.equals("BICYCLE") || vehicleType.equals("SCOOTER")) {
                        score += 0.05;
                        matchedCriteria.add("Great for good weather");
                    }
                    break;
            }
        }
        
        // Duration considerations
        if (request.getDuration() != null) {
            switch (request.getDuration().toLowerCase()) {
                case "short":
                    if (vehicleType.equals("BICYCLE") || vehicleType.equals("SCOOTER")) {
                        score += 0.1;
                        matchedCriteria.add("Perfect for short trips");
                    }
                    break;
                case "long":
                    if (vehicleType.equals("CAR")) {
                        score += 0.15;
                        matchedCriteria.add("Comfortable for long duration");
                    }
                    break;
            }
        }
        
        // Create recommendation
        VehicleRecommendation recommendation = new VehicleRecommendation();
        recommendation.setVehicleId((Long) vehicle.get("id"));
        recommendation.setBrand(vehicle.get("brand").toString());
        recommendation.setModel(vehicle.get("model").toString());
        recommendation.setVehicleType(vehicleType);
        recommendation.setMatchScore(Math.min(score, 1.0)); // Cap at 1.0
        recommendation.setReason(reasonBuilder.toString());
        recommendation.setMatchedCriteria(matchedCriteria);
        recommendation.setDailyRate(dailyRate);
        recommendation.setHourlyRate(hourlyRate);
        recommendation.setLocation(vehicle.get("location").toString());
        recommendation.setRecommendedAddOns(generateVehicleSpecificAddOns(vehicleType, request));
        
        // Calculate estimated cost
        if (request.getDuration() != null) {
            double estimatedCost = calculateEstimatedCost(request.getDuration(), dailyRate, hourlyRate);
            recommendation.setEstimatedCost(estimatedCost);
        }
        
        return recommendation;
    }
    
    private List<String> generateAddOnRecommendations(RecommendationRequest request) {
        List<String> addOns = new ArrayList<>();
        
        if (request.getTripType() != null) {
            switch (request.getTripType().toLowerCase()) {
                case "family":
                    addOns.add("Child Safety Seats");
                    addOns.add("Extra Insurance Coverage");
                    addOns.add("Emergency Roadside Assistance");
                    break;
                case "business":
                    addOns.add("GPS Navigation System");
                    addOns.add("Mobile Charger");
                    addOns.add("Professional Cleaning");
                    break;
                case "long_distance":
                    addOns.add("GPS Navigation System");
                    addOns.add("Emergency Kit");
                    addOns.add("Extra Fuel Tank");
                    addOns.add("Roadside Assistance");
                    break;
                case "leisure":
                    addOns.add("Bluetooth Speaker");
                    addOns.add("Picnic Kit");
                    addOns.add("Camera Mount");
                    break;
                case "solo":
                    addOns.add("Helmet (for 2-wheelers)");
                    addOns.add("Mobile Mount");
                    addOns.add("Basic Insurance");
                    break;
            }
        }
        
        if (request.getWeatherCondition() != null) {
            switch (request.getWeatherCondition().toLowerCase()) {
                case "rainy":
                    addOns.add("Raincoat/Umbrella");
                    addOns.add("Waterproof Seat Covers");
                    break;
                case "winter":
                    addOns.add("Seat Warmers");
                    addOns.add("Winter Emergency Kit");
                    break;
            }
        }
        
        return addOns.stream().distinct().collect(Collectors.toList());
    }
    
    private List<String> generateVehicleSpecificAddOns(String vehicleType, RecommendationRequest request) {
        List<String> addOns = new ArrayList<>();
        
        switch (vehicleType) {
            case "BIKE":
            case "SCOOTER":
                addOns.add("Helmet");
                addOns.add("Mobile Mount");
                addOns.add("Storage Box");
                break;
            case "CAR":
                addOns.add("GPS Navigation");
                addOns.add("Mobile Charger");
                addOns.add("First Aid Kit");
                if (request.getPassengerCount() != null && request.getPassengerCount() > 2) {
                    addOns.add("Child Safety Seats");
                }
                break;
            case "BICYCLE":
                addOns.add("Safety Helmet");
                addOns.add("Water Bottle Holder");
                addOns.add("Lock and Chain");
                break;
        }
        
        return addOns;
    }
    
    private String generatePersonalizedMessage(RecommendationRequest request, List<VehicleRecommendation> recommendations) {
        if (recommendations.isEmpty()) {
            return "We couldn't find vehicles matching your specific criteria, but here are some great options to consider!";
        }
        
        StringBuilder message = new StringBuilder();
        String tripType = request.getTripType();
        
        if (tripType != null) {
            switch (tripType.toLowerCase()) {
                case "solo":
                    message.append("Perfect vehicles for your solo adventure! ");
                    break;
                case "family":
                    message.append("Great family-friendly options that ensure comfort and safety for everyone! ");
                    break;
                case "business":
                    message.append("Professional vehicles that will make the right impression for your business needs! ");
                    break;
                case "leisure":
                    message.append("Eco-friendly and fun options for your leisure activities! ");
                    break;
                case "long_distance":
                    message.append("Reliable and fuel-efficient vehicles perfect for your long journey! ");
                    break;
                default:
                    message.append("We've found some great vehicle options for your trip! ");
            }
        }
        
        if (!recommendations.isEmpty()) {
            VehicleRecommendation topChoice = recommendations.get(0);
            message.append(String.format("Our top recommendation is the %s %s with a %.0f%% match to your needs.",
                    topChoice.getBrand(), topChoice.getModel(), topChoice.getMatchScore() * 100));
        }
        
        return message.toString();
    }
    
    private String analyzeTripType(RecommendationRequest request) {
        if (request.getTripType() == null) {
            return "Based on your requirements, we've analyzed various vehicle options for you.";
        }
        
        switch (request.getTripType().toLowerCase()) {
            case "solo":
                return "Solo trips are best with 2-wheelers for flexibility and cost-effectiveness in urban areas.";
            case "family":
                return "Family trips require vehicles with adequate seating, safety features, and comfort for all passengers.";
            case "business":
                return "Business travel demands reliable, professional-looking vehicles that ensure punctuality and comfort.";
            case "leisure":
                return "Leisure activities are perfect with eco-friendly options that enhance your experience with nature.";
            case "long_distance":
                return "Long-distance travel requires fuel-efficient vehicles with comfort features for extended journeys.";
            case "city":
                return "City travel is optimized with compact vehicles that can navigate traffic and tight parking spaces.";
            default:
                return "We've analyzed your trip requirements to suggest the most suitable vehicles.";
        }
    }
    
    private double calculateEstimatedCost(String duration, double dailyRate, double hourlyRate) {
        switch (duration.toLowerCase()) {
            case "short":
                return hourlyRate * 4; // 4 hours
            case "medium":
                return hourlyRate * 8; // 8 hours
            case "long":
                return dailyRate; // Full day
            default:
                // Try to parse as hours
                try {
                    int hours = Integer.parseInt(duration);
                    return hourlyRate * hours;
                } catch (NumberFormatException e) {
                    return dailyRate; // Default to daily rate
                }
        }
    }
}