package com.rydio.controller;

import com.rydio.common.dto.ApiResponse;
import com.rydio.vehicle.dto.CreateVehicleRequest;
import com.rydio.vehicle.dto.VehicleResponse;
import com.rydio.vehicle.entity.Vehicle;
import com.rydio.vehicle.service.VehicleService;
import com.rydio.user.entity.User;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vehicles")
@CrossOrigin(origins = "http://localhost:3000")
public class VehicleController {
    
    @Autowired
    private VehicleService vehicleService;
    
    @GetMapping
    public ResponseEntity<ApiResponse<List<VehicleResponse>>> getAllVehicles() {
        List<VehicleResponse> vehicles = vehicleService.getAvailableVehicles();
        return ResponseEntity.ok(ApiResponse.success("Vehicles retrieved successfully", vehicles));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<VehicleResponse>> getVehicleById(@PathVariable Long id) {
        VehicleResponse vehicle = vehicleService.getVehicleById(id);
        return ResponseEntity.ok(ApiResponse.success("Vehicle retrieved successfully", vehicle));
    }
    
    @GetMapping("/my-vehicles")
    public ResponseEntity<ApiResponse<List<VehicleResponse>>> getMyVehicles() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) auth.getPrincipal();
        
        List<VehicleResponse> vehicles = vehicleService.getVehiclesByOwner(currentUser.getId());
        return ResponseEntity.ok(ApiResponse.success("Your vehicles retrieved successfully", vehicles));
    }
    
    @PostMapping
    public ResponseEntity<ApiResponse<VehicleResponse>> createVehicle(@Valid @RequestBody CreateVehicleRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) auth.getPrincipal();
        
        VehicleResponse vehicle = vehicleService.createVehicle(request, currentUser.getId());
        return ResponseEntity.ok(ApiResponse.success("Vehicle created successfully", vehicle));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<VehicleResponse>> updateVehicle(
            @PathVariable Long id, 
            @Valid @RequestBody CreateVehicleRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) auth.getPrincipal();
        
        VehicleResponse vehicle = vehicleService.updateVehicle(id, request, currentUser.getId());
        return ResponseEntity.ok(ApiResponse.success("Vehicle updated successfully", vehicle));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteVehicle(@PathVariable Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) auth.getPrincipal();
        
        vehicleService.deleteVehicle(id, currentUser.getId());
        return ResponseEntity.ok(ApiResponse.success("Vehicle deleted successfully", "Vehicle removed"));
    }
    
    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<VehicleResponse>> updateVehicleStatus(
            @PathVariable Long id, 
            @RequestParam Vehicle.VehicleStatus status) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) auth.getPrincipal();
        
        VehicleResponse vehicle = vehicleService.updateVehicleStatus(id, status, currentUser.getId());
        return ResponseEntity.ok(ApiResponse.success("Vehicle status updated successfully", vehicle));
    }
    
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<VehicleResponse>>> searchVehicles(
            @RequestParam(required = false) String vehicleType,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice) {
        
        Vehicle.VehicleType type = vehicleType != null ? Vehicle.VehicleType.valueOf(vehicleType.toUpperCase()) : null;
        List<VehicleResponse> vehicles = vehicleService.searchVehicles(type, location, minPrice, maxPrice);
        return ResponseEntity.ok(ApiResponse.success("Vehicles found", vehicles));
    }
}