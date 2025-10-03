package com.rydio.vehicle.entity;

import com.rydio.common.entity.BaseEntity;
import com.rydio.user.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "vehicles")
public class Vehicle extends BaseEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    @NotBlank(message = "License plate is required")
    private String licensePlate;
    
    @Column(nullable = false)
    @NotBlank(message = "Brand is required")
    private String brand;
    
    @Column(nullable = false)
    @NotBlank(message = "Model is required")
    private String model;
    
    @Column(name = "vehicle_year", nullable = false)
    @Min(value = 1900, message = "Year must be after 1900")
    @Max(value = 2030, message = "Year cannot be in the future")
    private Integer year;
    
    @Column(nullable = false)
    @NotBlank(message = "Color is required")
    private String color;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VehicleType vehicleType;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FuelType fuelType;
    
    private String engineCapacity;
    
    private Integer seatCount;
    
    @Column(nullable = false)
    @DecimalMin(value = "0.0", inclusive = false, message = "Daily rate must be positive")
    private Double dailyRate;
    
    @DecimalMin(value = "0.0", inclusive = false, message = "Hourly rate must be positive")
    private Double hourlyRate;
    
    @DecimalMin(value = "0.0", message = "Mileage cannot be negative")
    private Double mileage;
    
    private String insuranceNumber;
    
    @Column(unique = true)
    private String registrationNumber;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VehicleStatus status = VehicleStatus.AVAILABLE;
    
    private String location;
    
    @Column(length = 1000)
    private String description;
    
    @Column(length = 500)
    private String features;
    
    private String imageUrl;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;
    
    // Constructors
    public Vehicle() {}
    
    public Vehicle(String licensePlate, String brand, String model, Integer year, String color,
                   VehicleType vehicleType, FuelType fuelType, Double dailyRate, User owner) {
        this.licensePlate = licensePlate;
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.color = color;
        this.vehicleType = vehicleType;
        this.fuelType = fuelType;
        this.dailyRate = dailyRate;
        this.owner = owner;
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
    
    public VehicleType getVehicleType() {
        return vehicleType;
    }
    
    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }
    
    public FuelType getFuelType() {
        return fuelType;
    }
    
    public void setFuelType(FuelType fuelType) {
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
    
    public VehicleStatus getStatus() {
        return status;
    }
    
    public void setStatus(VehicleStatus status) {
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
    
    public User getOwner() {
        return owner;
    }
    
    public void setOwner(User owner) {
        this.owner = owner;
    }
    
    // Enums
    public enum VehicleType {
        CAR, BIKE, SCOOTER, BICYCLE
    }
    
    public enum FuelType {
        PETROL, DIESEL, ELECTRIC, HYBRID, CNG
    }
    
    public enum VehicleStatus {
        AVAILABLE, RENTED, MAINTENANCE, INACTIVE
    }
}