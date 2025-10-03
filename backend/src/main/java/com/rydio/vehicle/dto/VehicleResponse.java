package com.rydio.vehicle.dto;

import com.rydio.vehicle.entity.Vehicle;

public class VehicleResponse {
    
    private Long id;
    private String licensePlate;
    private String brand;
    private String model;
    private Integer year;
    private String color;
    private Vehicle.VehicleType vehicleType;
    private Vehicle.FuelType fuelType;
    private String engineCapacity;
    private Integer seatCount;
    private Double dailyRate;
    private Double hourlyRate;
    private Double mileage;
    private String insuranceNumber;
    private String registrationNumber;
    private Vehicle.VehicleStatus status;
    private String location;
    private String description;
    private String features;
    private String imageUrl;
    private String ownerName;
    private Long ownerId;
    private String createdAt;
    private String updatedAt;
    
    // Constructors
    public VehicleResponse() {}
    
    public VehicleResponse(Vehicle vehicle) {
        this.id = vehicle.getId();
        this.licensePlate = vehicle.getLicensePlate();
        this.brand = vehicle.getBrand();
        this.model = vehicle.getModel();
        this.year = vehicle.getYear();
        this.color = vehicle.getColor();
        this.vehicleType = vehicle.getVehicleType();
        this.fuelType = vehicle.getFuelType();
        this.engineCapacity = vehicle.getEngineCapacity();
        this.seatCount = vehicle.getSeatCount();
        this.dailyRate = vehicle.getDailyRate();
        this.hourlyRate = vehicle.getHourlyRate();
        this.mileage = vehicle.getMileage();
        this.insuranceNumber = vehicle.getInsuranceNumber();
        this.registrationNumber = vehicle.getRegistrationNumber();
        this.status = vehicle.getStatus();
        this.location = vehicle.getLocation();
        this.description = vehicle.getDescription();
        this.features = vehicle.getFeatures();
        this.imageUrl = vehicle.getImageUrl();
        if (vehicle.getOwner() != null) {
            this.ownerId = vehicle.getOwner().getId();
            this.ownerName = vehicle.getOwner().getFirstName() + " " + vehicle.getOwner().getLastName();
        }
        if (vehicle.getCreatedAt() != null) {
            this.createdAt = vehicle.getCreatedAt().toString();
        }
        if (vehicle.getUpdatedAt() != null) {
            this.updatedAt = vehicle.getUpdatedAt().toString();
        }
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
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
    
    public Vehicle.VehicleStatus getStatus() {
        return status;
    }
    
    public void setStatus(Vehicle.VehicleStatus status) {
        this.status = status;
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
    
    public String getOwnerName() {
        return ownerName;
    }
    
    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }
    
    public Long getOwnerId() {
        return ownerId;
    }
    
    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }
    
    public String getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
    
    public String getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}