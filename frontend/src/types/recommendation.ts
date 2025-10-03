// Smart Recommendation types
export interface RecommendationRequest {
  tripType?: 'solo' | 'family' | 'business' | 'leisure' | 'long_distance' | 'city';
  passengerCount?: number;
  duration?: 'short' | 'medium' | 'long' | string;
  location?: string;
  startDate?: string;
  endDate?: string;
  maxBudget?: number;
  preferredFuelType?: 'PETROL' | 'DIESEL' | 'ELECTRIC' | 'HYBRID' | 'CNG';
  requiresLuggage?: boolean;
  weatherCondition?: 'sunny' | 'rainy' | 'winter';
}

export interface VehicleRecommendation {
  vehicleId: number;
  brand: string;
  model: string;
  vehicleType: string;
  matchScore: number;
  reason: string;
  matchedCriteria: string[];
  recommendedAddOns: string[];
  estimatedCost?: number;
  imageUrl?: string;
  location?: string;
  dailyRate: number;
  hourlyRate?: number;
}

export interface RecommendationResponse {
  recommendations: VehicleRecommendation[];
  suggestedAddOns: string[];
  personalizedMessage: string;
  estimatedTotalCost?: number;
  tripTypeAnalysis: string;
}