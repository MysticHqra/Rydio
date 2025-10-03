package com.rydio.recommendation.dto;

import java.util.List;

public class RecommendationResponse {
    private List<VehicleRecommendation> recommendations;
    private List<String> suggestedAddOns;
    private String personalizedMessage;
    private Double estimatedTotalCost;
    private String tripTypeAnalysis;
    
    public RecommendationResponse() {}
    
    public RecommendationResponse(List<VehicleRecommendation> recommendations, String personalizedMessage) {
        this.recommendations = recommendations;
        this.personalizedMessage = personalizedMessage;
    }

    public List<VehicleRecommendation> getRecommendations() {
        return recommendations;
    }

    public void setRecommendations(List<VehicleRecommendation> recommendations) {
        this.recommendations = recommendations;
    }

    public List<String> getSuggestedAddOns() {
        return suggestedAddOns;
    }

    public void setSuggestedAddOns(List<String> suggestedAddOns) {
        this.suggestedAddOns = suggestedAddOns;
    }

    public String getPersonalizedMessage() {
        return personalizedMessage;
    }

    public void setPersonalizedMessage(String personalizedMessage) {
        this.personalizedMessage = personalizedMessage;
    }

    public Double getEstimatedTotalCost() {
        return estimatedTotalCost;
    }

    public void setEstimatedTotalCost(Double estimatedTotalCost) {
        this.estimatedTotalCost = estimatedTotalCost;
    }

    public String getTripTypeAnalysis() {
        return tripTypeAnalysis;
    }

    public void setTripTypeAnalysis(String tripTypeAnalysis) {
        this.tripTypeAnalysis = tripTypeAnalysis;
    }
}