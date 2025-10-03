import api from './api';
import { RecommendationRequest, RecommendationResponse, VehicleRecommendation } from '../types/recommendation';
import { ApiResponse } from '../types/api';

export const recommendationService = {
  getSmartRecommendations: async (request: RecommendationRequest): Promise<ApiResponse<RecommendationResponse>> => {
    try {
      console.log('Making recommendation request to:', api.defaults.baseURL + '/recommendations/smart');
      console.log('Request data:', request);
      const response = await api.post('/recommendations/smart', request);
      console.log('Recommendation response:', response.data);
      return response.data;
    } catch (error) {
      console.error('Recommendation service error:', error);
      throw error;
    }
  },

  getQuickRecommendations: async (params: {
    tripType?: string;
    passengers?: number;
    duration?: string;
  }): Promise<ApiResponse<VehicleRecommendation[]>> => {
    try {
      const response = await api.get('/recommendations/quick', { params });
      return response.data;
    } catch (error) {
      console.error('Quick recommendations error:', error);
      throw error;
    }
  },

  getRecommendedAddOns: async (params: {
    tripType: string;
    vehicleType?: string;
    weather?: string;
  }): Promise<ApiResponse<string[]>> => {
    try {
      const response = await api.get('/recommendations/add-ons', { params });
      return response.data;
    } catch (error) {
      console.error('Add-ons recommendation error:', error);
      throw error;
    }
  },

  getPersonalizedInsight: async (): Promise<ApiResponse<{ insight: string }>> => {
    try {
      const response = await api.get('/recommendations/personalized-insight');
      return response.data;
    } catch (error) {
      console.error('Personalized insight error:', error);
      throw error;
    }
  },
};

export default recommendationService;