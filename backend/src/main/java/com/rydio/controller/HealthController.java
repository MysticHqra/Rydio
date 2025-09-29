package com.rydio.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

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
    
    @GetMapping("/vehicles")
    public Map<String, Object> getAllVehicles() {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Vehicles retrieved successfully");
        response.put("data", getVehiclesList());
        return response;
    }
    
    @GetMapping("/vehicles/{id}")
    public ResponseEntity<Map<String, Object>> getVehicleById(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        
        List<Map<String, Object>> vehicles = getVehiclesList();
        Optional<Map<String, Object>> vehicle = vehicles.stream()
            .filter(v -> v.get("id").equals(id))
            .findFirst();
            
        if (vehicle.isPresent()) {
            response.put("success", true);
            response.put("message", "Vehicle found");
            response.put("data", vehicle.get());
            return ResponseEntity.ok(response);
        } else {
            response.put("success", false);
            response.put("message", "Vehicle not found");
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/vehicles/search")
    public Map<String, Object> searchVehicles(
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String brand) {
        
        Map<String, Object> response = new HashMap<>();
        List<Map<String, Object>> allVehicles = getVehiclesList();
        
        List<Map<String, Object>> filteredVehicles = allVehicles.stream()
            .filter(vehicle -> {
                boolean matches = true;
                if (type != null && !type.isEmpty()) {
                    matches = matches && vehicle.get("vehicleType").toString().equalsIgnoreCase(type);
                }
                if (location != null && !location.isEmpty()) {
                    matches = matches && vehicle.get("location").toString().toLowerCase().contains(location.toLowerCase());
                }
                if (brand != null && !brand.isEmpty()) {
                    matches = matches && vehicle.get("brand").toString().toLowerCase().contains(brand.toLowerCase());
                }
                return matches;
            })
            .toList();
            
        response.put("success", true);
        response.put("message", "Found " + filteredVehicles.size() + " vehicles");
        response.put("data", filteredVehicles);
        return response;
    }
    
    private List<Map<String, Object>> getVehiclesList() {
        List<Map<String, Object>> vehicles = new ArrayList<>();
        
        vehicles.add(createVehicle(1L, "MH01AB1234", "Honda", "Activa 6G", 2022, "White", "SCOOTER", 
                     "PETROL", 2, 1200, 50, "AVAILABLE", "Mumbai", 
                     "Compact scooter perfect for city rides", "Bluetooth connectivity, LED headlights", 
                     "/images/honda-activa.jpg"));
        
        vehicles.add(createVehicle(2L, "MH02CD5678", "Maruti", "Swift", 2023, "Red", "CAR", 
                     "PETROL", 5, 2400, 100, "AVAILABLE", "Mumbai", 
                     "Comfortable hatchback for family trips", "AC, Power steering, ABS", 
                     "/images/maruti-swift.jpg"));
        
        vehicles.add(createVehicle(3L, "MH03EF9012", "Hero", "Splendor Plus", 2022, "Black", "BIKE", 
                     "PETROL", 2, 800, 35, "AVAILABLE", "Pune", 
                     "Reliable motorcycle for long rides", "Kick start, Electric start", 
                     "/images/hero-splendor.jpg"));
        
        vehicles.add(createVehicle(4L, "MH04GH3456", "Hyundai", "i20", 2023, "Blue", "CAR", 
                     "PETROL", 5, 2800, 120, "AVAILABLE", "Delhi", 
                     "Premium hatchback with modern features", "Touchscreen, Automatic transmission", 
                     "/images/hyundai-i20.jpg"));
        
        vehicles.add(createVehicle(5L, "MH05IJ7890", "Ola", "S1 Pro", 2023, "White", "SCOOTER", 
                     "ELECTRIC", 2, 1000, 45, "AVAILABLE", "Bangalore", 
                     "Electric scooter with smart features", "App connectivity, GPS tracking", 
                     "/images/ola-s1-pro.jpg"));
                     
        return vehicles;
    }
    
    private Map<String, Object> createVehicle(Long id, String licensePlate, String brand, String model, 
                                             int year, String color, String vehicleType, String fuelType, 
                                             int seatCount, int dailyRate, int hourlyRate, String status, 
                                             String location, String description, String features, String imageUrl) {
        Map<String, Object> vehicle = new HashMap<>();
        vehicle.put("id", id);
        vehicle.put("licensePlate", licensePlate);
        vehicle.put("brand", brand);
        vehicle.put("model", model);
        vehicle.put("year", year);
        vehicle.put("color", color);
        vehicle.put("vehicleType", vehicleType);
        vehicle.put("fuelType", fuelType);
        vehicle.put("seatCount", seatCount);
        vehicle.put("dailyRate", dailyRate);
        vehicle.put("hourlyRate", hourlyRate);
        vehicle.put("status", status);
        vehicle.put("location", location);
        vehicle.put("description", description);
        vehicle.put("features", features);
        vehicle.put("imageUrl", imageUrl);
        vehicle.put("createdAt", "2025-09-30T00:00:00Z");
        vehicle.put("updatedAt", "2025-09-30T00:00:00Z");
        return vehicle;
    }
}