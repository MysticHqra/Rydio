package com.rydio.recommendation.dto;

import java.util.List;

public class VehicleRecommendation {
    private Long vehicleId;
    private String brand;
    private String model;
    private String vehicleType;
    private Double matchScore; // 0.0 to 1.0
    private String reason;
    private List<String> matchedCriteria;
    private List<String> recommendedAddOns;
    private Double estimatedCost;
    private String imageUrl;
    private String location;
    private Double dailyRate;
    private Double hourlyRate;
    
    public VehicleRecommendation() {}
    
    public VehicleRecommendation(Long vehicleId, String brand, String model, String vehicleType, 
                               Double matchScore, String reason) {
        this.vehicleId = vehicleId;
        this.brand = brand;
        this.model = model;
        this.vehicleType = vehicleType;
        this.matchScore = matchScore;
        this.reason = reason;
    }

    public Long getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(Long vehicleId) {
        this.vehicleId = vehicleId;
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

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public Double getMatchScore() {
        return matchScore;
    }

    public void setMatchScore(Double matchScore) {
        this.matchScore = matchScore;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public List<String> getMatchedCriteria() {
        return matchedCriteria;
    }

    public void setMatchedCriteria(List<String> matchedCriteria) {
        this.matchedCriteria = matchedCriteria;
    }

    public List<String> getRecommendedAddOns() {
        return recommendedAddOns;
    }

    public void setRecommendedAddOns(List<String> recommendedAddOns) {
        this.recommendedAddOns = recommendedAddOns;
    }

    public Double getEstimatedCost() {
        return estimatedCost;
    }

    public void setEstimatedCost(Double estimatedCost) {
        this.estimatedCost = estimatedCost;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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
}