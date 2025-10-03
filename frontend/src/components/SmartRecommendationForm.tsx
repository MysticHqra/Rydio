import React, { useState } from 'react';
import { RecommendationRequest } from '../types/recommendation';

interface SmartRecommendationFormProps {
  onSubmit: (request: RecommendationRequest) => void;
  loading?: boolean;
}

const SmartRecommendationForm: React.FC<SmartRecommendationFormProps> = ({ onSubmit, loading = false }) => {
  const [formData, setFormData] = useState<RecommendationRequest>({
    tripType: 'solo',
    passengerCount: 1,
    duration: 'short',
    maxBudget: undefined,
    preferredFuelType: undefined,
    requiresLuggage: false,
    weatherCondition: 'sunny',
  });

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    onSubmit(formData);
  };

  const handleInputChange = (field: keyof RecommendationRequest, value: any) => {
    setFormData(prev => ({
      ...prev,
      [field]: value
    }));
  };

  return (
    <div className="bg-white rounded-lg shadow-md p-6">
      <h2 className="text-2xl font-bold text-gray-900 mb-6">Find Your Perfect Vehicle</h2>
      
      <form onSubmit={handleSubmit} className="space-y-6">
        {/* Trip Type */}
        <div>
          <label className="block text-sm font-medium text-gray-700 mb-2">
            What type of trip are you planning?
          </label>
          <div className="grid grid-cols-2 md:grid-cols-3 gap-3">
            {[
              { value: 'solo', label: '🚶 Solo Ride', desc: 'Just you' },
              { value: 'family', label: '👨‍👩‍👧‍👦 Family Trip', desc: 'With family' },
              { value: 'business', label: '💼 Business', desc: 'Professional' },
              { value: 'leisure', label: '🌿 Leisure', desc: 'Fun & relaxation' },
              { value: 'long_distance', label: '🛣️ Long Distance', desc: 'Highway travel' },
              { value: 'city', label: '🏙️ City Commute', desc: 'Urban travel' }
            ].map((option) => (
              <button
                key={option.value}
                type="button"
                onClick={() => handleInputChange('tripType', option.value)}
                className={`p-3 rounded-lg border-2 text-left transition-all duration-200 ${
                  formData.tripType === option.value
                    ? 'border-indigo-500 bg-indigo-50 text-indigo-900'
                    : 'border-gray-200 hover:border-gray-300 text-gray-700'
                }`}
              >
                <div className="font-medium text-sm">{option.label}</div>
                <div className="text-xs text-gray-500">{option.desc}</div>
              </button>
            ))}
          </div>
        </div>

        {/* Passenger Count */}
        <div>
          <label className="block text-sm font-medium text-gray-700 mb-2">
            How many passengers?
          </label>
          <select
            value={formData.passengerCount || 1}
            onChange={(e) => handleInputChange('passengerCount', parseInt(e.target.value))}
            className="w-full p-3 border border-gray-300 rounded-md focus:ring-indigo-500 focus:border-indigo-500"
          >
            {[1, 2, 3, 4, 5, 6, 7, 8].map(num => (
              <option key={num} value={num}>
                {num} {num === 1 ? 'person' : 'people'}
              </option>
            ))}
          </select>
        </div>

        {/* Duration */}
        <div>
          <label className="block text-sm font-medium text-gray-700 mb-2">
            How long do you need the vehicle?
          </label>
          <div className="grid grid-cols-3 gap-3">
            {[
              { value: 'short', label: 'Short', desc: '2-4 hours' },
              { value: 'medium', label: 'Medium', desc: '4-8 hours' },
              { value: 'long', label: 'Full Day', desc: '8+ hours' }
            ].map((option) => (
              <button
                key={option.value}
                type="button"
                onClick={() => handleInputChange('duration', option.value)}
                className={`p-3 rounded-lg border-2 text-center transition-all duration-200 ${
                  formData.duration === option.value
                    ? 'border-indigo-500 bg-indigo-50 text-indigo-900'
                    : 'border-gray-200 hover:border-gray-300 text-gray-700'
                }`}
              >
                <div className="font-medium text-sm">{option.label}</div>
                <div className="text-xs text-gray-500">{option.desc}</div>
              </button>
            ))}
          </div>
        </div>

        {/* Budget */}
        <div>
          <label className="block text-sm font-medium text-gray-700 mb-2">
            Budget (optional)
          </label>
          <div className="relative">
            <span className="absolute left-3 top-3 text-gray-500">₹</span>
            <input
              type="number"
              placeholder="Maximum budget"
              value={formData.maxBudget || ''}
              onChange={(e) => handleInputChange('maxBudget', e.target.value ? parseFloat(e.target.value) : undefined)}
              className="w-full pl-8 pr-3 py-3 border border-gray-300 rounded-md focus:ring-indigo-500 focus:border-indigo-500"
            />
          </div>
        </div>

        {/* Fuel Preference */}
        <div>
          <label className="block text-sm font-medium text-gray-700 mb-2">
            Fuel preference (optional)
          </label>
          <select
            value={formData.preferredFuelType || ''}
            onChange={(e) => handleInputChange('preferredFuelType', e.target.value || undefined)}
            className="w-full p-3 border border-gray-300 rounded-md focus:ring-indigo-500 focus:border-indigo-500"
          >
            <option value="">No preference</option>
            <option value="PETROL">Petrol</option>
            <option value="DIESEL">Diesel</option>
            <option value="ELECTRIC">Electric</option>
            <option value="HYBRID">Hybrid</option>
            <option value="CNG">CNG</option>
          </select>
        </div>

        {/* Weather Condition */}
        <div>
          <label className="block text-sm font-medium text-gray-700 mb-2">
            Weather condition
          </label>
          <div className="grid grid-cols-3 gap-3">
            {[
              { value: 'sunny', label: '☀️ Sunny', desc: 'Clear skies' },
              { value: 'rainy', label: '🌧️ Rainy', desc: 'Wet weather' },
              { value: 'winter', label: '❄️ Winter', desc: 'Cold weather' }
            ].map((option) => (
              <button
                key={option.value}
                type="button"
                onClick={() => handleInputChange('weatherCondition', option.value)}
                className={`p-3 rounded-lg border-2 text-center transition-all duration-200 ${
                  formData.weatherCondition === option.value
                    ? 'border-indigo-500 bg-indigo-50 text-indigo-900'
                    : 'border-gray-200 hover:border-gray-300 text-gray-700'
                }`}
              >
                <div className="font-medium text-sm">{option.label}</div>
                <div className="text-xs text-gray-500">{option.desc}</div>
              </button>
            ))}
          </div>
        </div>

        {/* Additional Options */}
        <div className="flex items-center">
          <input
            id="luggage"
            type="checkbox"
            checked={formData.requiresLuggage || false}
            onChange={(e) => handleInputChange('requiresLuggage', e.target.checked)}
            className="h-4 w-4 text-indigo-600 focus:ring-indigo-500 border-gray-300 rounded"
          />
          <label htmlFor="luggage" className="ml-2 block text-sm text-gray-700">
            I need space for luggage/cargo
          </label>
        </div>

        {/* Submit Button */}
        <button
          type="submit"
          disabled={loading}
          className={`w-full py-3 px-4 rounded-md text-white font-medium transition-colors duration-200 ${
            loading
              ? 'bg-gray-400 cursor-not-allowed'
              : 'bg-indigo-600 hover:bg-indigo-700 focus:ring-4 focus:ring-indigo-200'
          }`}
        >
          {loading ? (
            <div className="flex items-center justify-center">
              <div className="animate-spin rounded-full h-5 w-5 border-b-2 border-white mr-2"></div>
              Finding perfect vehicles...
            </div>
          ) : (
            'Get Smart Recommendations'
          )}
        </button>
      </form>
    </div>
  );
};

export default SmartRecommendationForm;