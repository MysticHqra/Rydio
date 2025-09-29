package com.rydio.vehicle.controller;

import com.rydio.common.dto.ApiResponse;
import com.rydio.vehicle.dto.VehicleDto;
import com.rydio.vehicle.dto.VehicleSearchDto;
import com.rydio.vehicle.entity.Vehicle;
import com.rydio.vehicle.service.VehicleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vehicles")
@CrossOrigin(origins = "*", maxAge = 3600)
public class VehicleController {

    @Autowired
    private VehicleService vehicleService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<VehicleDto>> createVehicle(@Valid @RequestBody VehicleDto vehicleDto) {
        VehicleDto createdVehicle = vehicleService.createVehicle(vehicleDto);
        return ResponseEntity.ok(ApiResponse.success("Vehicle created successfully", createdVehicle));
    }

    @PutMapping("/{vehicleId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<VehicleDto>> updateVehicle(
            @PathVariable Long vehicleId,
            @Valid @RequestBody VehicleDto vehicleDto) {
        VehicleDto updatedVehicle = vehicleService.updateVehicle(vehicleId, vehicleDto);
        return ResponseEntity.ok(ApiResponse.success("Vehicle updated successfully", updatedVehicle));
    }

    @GetMapping("/{vehicleId}")
    public ResponseEntity<ApiResponse<VehicleDto>> getVehicleById(@PathVariable Long vehicleId) {
        VehicleDto vehicle = vehicleService.getVehicleById(vehicleId);
        return ResponseEntity.ok(ApiResponse.success(vehicle));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<VehicleDto>>> getAllVehicles() {
        List<VehicleDto> vehicles = vehicleService.getAllVehicles();
        return ResponseEntity.ok(ApiResponse.success(vehicles));
    }

    @GetMapping("/paginated")
    public ResponseEntity<ApiResponse<Page<VehicleDto>>> getVehiclesWithPagination(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        Page<VehicleDto> vehicles = vehicleService.getVehiclesWithPagination(page, size, sortBy, sortDir);
        return ResponseEntity.ok(ApiResponse.success(vehicles));
    }

    @PostMapping("/search")
    public ResponseEntity<ApiResponse<List<VehicleDto>>> searchVehicles(@RequestBody VehicleSearchDto searchDto) {
        List<VehicleDto> vehicles = vehicleService.searchVehicles(searchDto);
        return ResponseEntity.ok(ApiResponse.success(vehicles));
    }

    @GetMapping("/available")
    public ResponseEntity<ApiResponse<List<VehicleDto>>> getAvailableVehicles() {
        List<VehicleDto> vehicles = vehicleService.getAvailableVehicles();
        return ResponseEntity.ok(ApiResponse.success(vehicles));
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<ApiResponse<List<VehicleDto>>> getVehiclesByType(@PathVariable Vehicle.VehicleType type) {
        List<VehicleDto> vehicles = vehicleService.getVehiclesByType(type);
        return ResponseEntity.ok(ApiResponse.success(vehicles));
    }

    @DeleteMapping("/{vehicleId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<String>> deleteVehicle(@PathVariable Long vehicleId) {
        vehicleService.deleteVehicle(vehicleId);
        return ResponseEntity.ok(ApiResponse.success("Vehicle deleted successfully"));
    }

    @PutMapping("/{vehicleId}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<VehicleDto>> updateVehicleStatus(
            @PathVariable Long vehicleId,
            @RequestParam Vehicle.VehicleStatus status) {
        VehicleDto updatedVehicle = vehicleService.updateVehicleStatus(vehicleId, status);
        return ResponseEntity.ok(ApiResponse.success("Vehicle status updated successfully", updatedVehicle));
    }

    @GetMapping("/brands")
    public ResponseEntity<ApiResponse<List<String>>> getAllBrands() {
        List<String> brands = vehicleService.getAllBrands();
        return ResponseEntity.ok(ApiResponse.success(brands));
    }

    @GetMapping("/brands/{brand}/models")
    public ResponseEntity<ApiResponse<List<String>>> getModelsByBrand(@PathVariable String brand) {
        List<String> models = vehicleService.getModelsByBrand(brand);
        return ResponseEntity.ok(ApiResponse.success(models));
    }

    @GetMapping("/locations")
    public ResponseEntity<ApiResponse<List<String>>> getAllLocations() {
        List<String> locations = vehicleService.getAllLocations();
        return ResponseEntity.ok(ApiResponse.success(locations));
    }
}