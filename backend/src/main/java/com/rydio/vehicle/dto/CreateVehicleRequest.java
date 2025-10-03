package com.rydio.vehicle.dto;

import com.rydio.vehicle.entity.Vehicle;
import jakarta.validation.constraints.*;

public class CreateVehicleRequest {
    
    @NotBlank(message = "License plate is required")
    private String licensePlate;
    
    @NotBlank(message = "Brand is required")
    private String brand;
    
    @NotBlank(message = "Model is required")
    private String model;
    
    @NotNull(message = "Year is required")
    @Min(value = 1900, message = "Year must be after 1900")
    @Max(value = 2030, message = "Year cannot be in the future")
    private Integer year;
    
    @NotBlank(message = "Color is required")
    private String color;
    
    @NotNull(message = "Vehicle type is required")
    private Vehicle.VehicleType vehicleType;
    
    @NotNull(message = "Fuel type is required")
    private Vehicle.FuelType fuelType;
    
    private String engineCapacity;
    
    private Integer seatCount;
    
    @NotNull(message = "Daily rate is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Daily rate must be positive")
    private Double dailyRate;
    
    @DecimalMin(value = "0.0", inclusive = false, message = "Hourly rate must be positive")
    private Double hourlyRate;
    
    @DecimalMin(value = "0.0", message = "Mileage cannot be negative")
    private Double mileage;
    
    private String insuranceNumber;
    
    private String registrationNumber;
    
    private String location;
    
    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private String description;
    
    @Size(max = 500, message = "Features cannot exceed 500 characters")
    private String features;
    
    private String imageUrl;
    
    // Constructors
    public CreateVehicleRequest() {}
    
    // Getters and Setters
    public String getLicensePlate() {
        return licensePlate;
    }
    
    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }
    
    public String getBrand() {
        return brand;
    }
    
    public void setBrand(String brand) {
        this.brand = brand;
    }
    
    public String getModel() {
        return model;
    }
    
    public void setModel(String model) {
        this.model = model;
    }
    
    public Integer getYear() {
        return year;
    }
    
    public void setYear(Integer year) {
        this.year = year;
    }
    
    public String getColor() {
        return color;
    }
    
    public void setColor(String color) {
        this.color = color;
    }
    
    public Vehicle.VehicleType getVehicleType() {
        return vehicleType;
    }
    
    public void setVehicleType(Vehicle.VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }
    
    public Vehicle.FuelType getFuelType() {
        return fuelType;
    }
    
    public void setFuelType(Vehicle.FuelType fuelType) {
        this.fuelType = fuelType;
    }
    
    public String getEngineCapacity() {
        return engineCapacity;
    }
    
    public void setEngineCapacity(String engineCapacity) {
        this.engineCapacity = engineCapacity;
    }
    
    public Integer getSeatCount() {
        return seatCount;
    }
    
    public void setSeatCount(Integer seatCount) {
        this.seatCount = seatCount;
    }
    
    public Double getDailyRate() {
        return dailyRate;
    }
    
    public void setDailyRate(Double dailyRate) {
        this.dailyRate = dailyRate;
    }
    
    public Double getHourlyRate() {
        return hourlyRate;
    }
    
    public void setHourlyRate(Double hourlyRate) {
        this.hourlyRate = hourlyRate;
    }
    
    public Double getMileage() {
        return mileage;
    }
    
    public void setMileage(Double mileage) {
        this.mileage = mileage;
    }
    
    public String getInsuranceNumber() {
        return insuranceNumber;
    }
    
    public void setInsuranceNumber(String insuranceNumber) {
        this.insuranceNumber = insuranceNumber;
    }
    
    public String getRegistrationNumber() {
        return registrationNumber;
    }
    
    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }
    
    public String getLocation() {
        return location;
    }
    
    public void setLocation(String location) {
        this.location = location;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getFeatures() {
        return features;
    }
    
    public void setFeatures(String features) {
        this.features = features;
    }
    
    public String getImageUrl() {
        return imageUrl;
    }
    
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}