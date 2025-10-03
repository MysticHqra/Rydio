import React from 'react';
import { VehicleRecommendation } from '../types/recommendation';

interface SmartVehicleCardProps {
  recommendation: VehicleRecommendation;
  onBook?: (vehicleId: number) => void;
  onViewDetails?: (vehicleId: number) => void;
}

const SmartVehicleCard: React.FC<SmartVehicleCardProps> = ({ recommendation, onBook, onViewDetails }) => {
  const getMatchScoreColor = (score: number) => {
    if (score >= 0.8) return 'text-green-600 bg-green-100';
    if (score >= 0.6) return 'text-blue-600 bg-blue-100';
    if (score >= 0.4) return 'text-yellow-600 bg-yellow-100';
    return 'text-gray-600 bg-gray-100';
  };

  const getVehicleIcon = (type: string) => {
    switch (type) {
      case 'CAR':
        return (
          <svg className="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16" />
          </svg>
        );
      case 'BIKE':
      case 'SCOOTER':
        return (
          <svg className="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M13 10V3L4 14h7v7l9-11h-7z" />
          </svg>
        );
      case 'BICYCLE':
        return (
          <svg className="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M3.055 11H5a2 2 0 012 2v1a2 2 0 002 2 2 2 0 012-2v-1a2 2 0 012-2h1.945M8 13h8m-8-2v8m8-8v8" />
          </svg>
        );
      default:
        return (
          <svg className="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z" />
          </svg>
        );
    }
  };

  return (
    <div className="bg-white rounded-lg shadow-md overflow-hidden hover:shadow-lg transition-shadow duration-200 border border-gray-200">
      {/* Match Score Badge */}
      <div className="relative">
        <div className={`absolute top-4 right-4 px-3 py-1 rounded-full text-sm font-medium ${getMatchScoreColor(recommendation.matchScore)}`}>
          {Math.round(recommendation.matchScore * 100)}% Match
        </div>
        
        {/* Vehicle Image Placeholder */}
        <div className="h-48 bg-gradient-to-br from-gray-100 to-gray-200 flex items-center justify-center">
          {recommendation.imageUrl ? (
            <img 
              src={recommendation.imageUrl} 
              alt={`${recommendation.brand} ${recommendation.model}`}
              className="w-full h-full object-cover"
            />
          ) : (
            <div className="text-gray-400">
              {getVehicleIcon(recommendation.vehicleType)}
            </div>
          )}
        </div>
      </div>

      <div className="p-6">
        {/* Vehicle Info */}
        <div className="flex items-center justify-between mb-3">
          <h3 className="text-xl font-semibold text-gray-900">
            {recommendation.brand} {recommendation.model}
          </h3>
          <span className="bg-indigo-100 text-indigo-800 px-2 py-1 rounded-full text-xs font-medium">
            {recommendation.vehicleType}
          </span>
        </div>

        {/* Location and Pricing */}
        <div className="flex items-center justify-between mb-4">
          <p className="text-gray-600 text-sm flex items-center">
            <svg className="h-4 w-4 mr-1" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M17.657 16.657L13.414 20.9a1.998 1.998 0 01-2.827 0l-4.244-4.243a8 8 0 1111.314 0z" />
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M15 11a3 3 0 11-6 0 3 3 0 016 0z" />
            </svg>
            {recommendation.location}
          </p>
          <div className="text-right">
            <span className="text-2xl font-bold text-indigo-600">₹{recommendation.hourlyRate || Math.round(recommendation.dailyRate / 24)}</span>
            <span className="text-sm text-gray-500">/hr</span>
          </div>
        </div>

        {/* Estimated Cost */}
        {recommendation.estimatedCost && (
          <div className="mb-4 p-3 bg-green-50 rounded-lg">
            <p className="text-sm text-green-800">
              <span className="font-medium">Estimated Cost: ₹{recommendation.estimatedCost}</span>
              <span className="text-green-600 ml-2">(for your trip duration)</span>
            </p>
          </div>
        )}

        {/* Recommendation Reason */}
        <div className="mb-4">
          <p className="text-sm text-gray-700 mb-2">
            <span className="font-medium text-gray-900">Why this vehicle: </span>
            {recommendation.reason}
          </p>
        </div>

        {/* Matched Criteria */}
        {recommendation.matchedCriteria && recommendation.matchedCriteria.length > 0 && (
          <div className="mb-4">
            <h4 className="text-sm font-medium text-gray-900 mb-2">Perfect for:</h4>
            <div className="flex flex-wrap gap-1">
              {recommendation.matchedCriteria.slice(0, 3).map((criteria, index) => (
                <span key={index} className="bg-blue-50 text-blue-700 px-2 py-1 rounded text-xs">
                  {criteria}
                </span>
              ))}
              {recommendation.matchedCriteria.length > 3 && (
                <span className="bg-gray-50 text-gray-600 px-2 py-1 rounded text-xs">
                  +{recommendation.matchedCriteria.length - 3} more
                </span>
              )}
            </div>
          </div>
        )}

        {/* Recommended Add-ons */}
        {recommendation.recommendedAddOns && recommendation.recommendedAddOns.length > 0 && (
          <div className="mb-4">
            <h4 className="text-sm font-medium text-gray-900 mb-2">Recommended Add-ons:</h4>
            <div className="flex flex-wrap gap-1">
              {recommendation.recommendedAddOns.slice(0, 2).map((addon, index) => (
                <span key={index} className="bg-yellow-50 text-yellow-700 px-2 py-1 rounded text-xs">
                  {addon}
                </span>
              ))}
              {recommendation.recommendedAddOns.length > 2 && (
                <span className="bg-gray-50 text-gray-600 px-2 py-1 rounded text-xs">
                  +{recommendation.recommendedAddOns.length - 2} more
                </span>
              )}
            </div>
          </div>
        )}

        {/* Action Buttons */}
        <div className="flex space-x-3">
          <button
            onClick={() => onBook && onBook(recommendation.vehicleId)}
            className="flex-1 bg-indigo-600 hover:bg-indigo-700 text-white py-2 px-4 rounded-md text-sm font-medium transition-colors duration-200"
          >
            Book Now
          </button>
          <button
            onClick={() => onViewDetails && onViewDetails(recommendation.vehicleId)}
            className="flex-1 bg-gray-100 hover:bg-gray-200 text-gray-700 py-2 px-4 rounded-md text-sm font-medium transition-colors duration-200"
          >
            View Details
          </button>
        </div>
      </div>
    </div>
  );
};

export default SmartVehicleCard;