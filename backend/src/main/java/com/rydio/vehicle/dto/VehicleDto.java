package com.rydio.vehicle.dto;

import com.rydio.vehicle.entity.Vehicle;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class VehicleDto {
    
    private Long id;
    
    @NotBlank(message = "License plate is required")
    private String licensePlate;
    
    @NotBlank(message = "Brand is required")
    private String brand;
    
    @NotBlank(message = "Model is required")
    private String model;
    
    @NotNull(message = "Year is required")
    @Min(value = 1900, message = "Year must be greater than 1900")
    @Max(value = 2030, message = "Year must be less than 2030")
    private Integer year;
    
    @NotBlank(message = "Color is required")
    private String color;
    
    @NotNull(message = "Vehicle type is required")
    private Vehicle.VehicleType vehicleType;
    
    private Vehicle.FuelType fuelType;
    
    private String engineCapacity;
    
    @Min(value = 1, message = "Seat count must be at least 1")
    private Integer seatCount;
    
    @NotNull(message = "Daily rate is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Daily rate must be greater than 0")
    private BigDecimal dailyRate;
    
    @DecimalMin(value = "0.0", inclusive = false, message = "Hourly rate must be greater than 0")
    private BigDecimal hourlyRate;
    
    @Min(value = 0, message = "Mileage cannot be negative")
    private Integer mileage;
    
    private String insuranceNumber;
    
    private String registrationNumber;
    
    private Vehicle.VehicleStatus status;
    
    private String location;
    
    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private String description;
    
    private String features;
    
    private String imageUrl;
}