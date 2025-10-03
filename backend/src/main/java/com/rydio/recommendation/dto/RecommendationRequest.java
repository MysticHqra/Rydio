package com.rydio.recommendation.dto;

import java.time.LocalDate;

public class RecommendationRequest {
    private String tripType; // "solo", "family", "business", "leisure", "long_distance", "city"
    private Integer passengerCount;
    private String duration; // "short", "medium", "long" or hours
    private String location;
    private LocalDate startDate;
    private LocalDate endDate;
    private Double maxBudget;
    private String preferredFuelType;
    private Boolean requiresLuggage;
    private String weatherCondition; // "sunny", "rainy", "winter"
    
    public RecommendationRequest() {}
    
    public RecommendationRequest(String tripType, Integer passengerCount, String duration) {
        this.tripType = tripType;
        this.passengerCount = passengerCount;
        this.duration = duration;
    }

    public String getTripType() {
        return tripType;
    }

    public void setTripType(String tripType) {
        this.tripType = tripType;
    }

    public Integer getPassengerCount() {
        return passengerCount;
    }

    public void setPassengerCount(Integer passengerCount) {
        this.passengerCount = passengerCount;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Double getMaxBudget() {
        return maxBudget;
    }

    public void setMaxBudget(Double maxBudget) {
        this.maxBudget = maxBudget;
    }

    public String getPreferredFuelType() {
        return preferredFuelType;
    }

    public void setPreferredFuelType(String preferredFuelType) {
        this.preferredFuelType = preferredFuelType;
    }

    public Boolean getRequiresLuggage() {
        return requiresLuggage;
    }

    public void setRequiresLuggage(Boolean requiresLuggage) {
        this.requiresLuggage = requiresLuggage;
    }

    public String getWeatherCondition() {
        return weatherCondition;
    }

    public void setWeatherCondition(String weatherCondition) {
        this.weatherCondition = weatherCondition;
    }
}