import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import SmartRecommendationForm from '../components/SmartRecommendationForm';
import SmartVehicleCard from '../components/SmartVehicleCard';
import { recommendationService } from '../services/recommendationService';
import { RecommendationRequest, RecommendationResponse, VehicleRecommendation } from '../types/recommendation';

const SmartRecommendations: React.FC = () => {
  const [recommendations, setRecommendations] = useState<RecommendationResponse | null>(null);
  const [quickRecs, setQuickRecs] = useState<VehicleRecommendation[]>([]);
  const [personalizedInsight, setPersonalizedInsight] = useState<string>('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const { isAuthenticated } = useAuth();
  const navigate = useNavigate();

  useEffect(() => {
    // Load quick recommendations for new users
    loadQuickRecommendations();
    
    // Load personalized insight if authenticated
    if (isAuthenticated) {
      loadPersonalizedInsight();
    }
  }, [isAuthenticated]);

  const loadQuickRecommendations = async () => {
    try {
      const response = await recommendationService.getQuickRecommendations({
        tripType: 'solo',
        passengers: 1,
        duration: 'short'
      });
      
      if (response.success) {
        setQuickRecs(response.data.slice(0, 3)); // Show top 3
      }
    } catch (err) {
      console.error('Failed to load quick recommendations:', err);
    }
  };

  const loadPersonalizedInsight = async () => {
    try {
      const response = await recommendationService.getPersonalizedInsight();
      if (response.success) {
        setPersonalizedInsight(response.data.insight);
      }
    } catch (err) {
      console.error('Failed to load personalized insight:', err);
    }
  };

  const handleRecommendationRequest = async (request: RecommendationRequest) => {
    setLoading(true);
    setError('');

    try {
      const response = await recommendationService.getSmartRecommendations(request);
      
      if (response.success) {
        setRecommendations(response.data);
      } else {
        setError(response.message || 'Failed to get recommendations');
      }
    } catch (err: any) {
      setError(err.response?.data?.message || 'Failed to get recommendations. Please try again.');
    } finally {
      setLoading(false);
    }
  };

  const handleBookVehicle = (vehicleId: number) => {
    navigate(`/booking/${vehicleId}`);
  };

  const handleViewDetails = (vehicleId: number) => {
    navigate(`/vehicles?highlight=${vehicleId}`);
  };

  return (
    <div className="min-h-screen bg-gray-50 py-8">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        {/* Header */}
        <div className="text-center mb-12">
          <h1 className="text-4xl font-bold text-gray-900 mb-4">
            Smart Vehicle Recommendations
          </h1>
          <p className="text-xl text-gray-600 max-w-3xl mx-auto">
            Tell us about your trip and we'll find the perfect vehicle for you using our intelligent matching system
          </p>
        </div>

        {/* Personalized Insight */}
        {isAuthenticated && personalizedInsight && (
          <div className="mb-8">
            <div className="bg-gradient-to-r from-indigo-500 to-purple-600 rounded-lg p-6 text-white">
              <h2 className="text-lg font-semibold mb-2">🔮 Your Personal Insight</h2>
              <p className="text-indigo-100">{personalizedInsight}</p>
            </div>
          </div>
        )}

        {/* Quick Recommendations for New Users */}
        {!recommendations && quickRecs.length > 0 && (
          <div className="mb-12">
            <h2 className="text-2xl font-bold text-gray-900 mb-6">🚀 Popular Choices</h2>
            <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
              {quickRecs.map((rec) => (
                <SmartVehicleCard
                  key={rec.vehicleId}
                  recommendation={rec}
                  onBook={handleBookVehicle}
                  onViewDetails={handleViewDetails}
                />
              ))}
            </div>
            <div className="text-center mt-6">
              <p className="text-gray-600">Want personalized recommendations? Fill out the form below!</p>
            </div>
          </div>
        )}

        <div className="grid grid-cols-1 lg:grid-cols-2 gap-8">
          {/* Left Column - Form */}
          <div>
            <SmartRecommendationForm onSubmit={handleRecommendationRequest} loading={loading} />
            
            {/* Features List */}
            <div className="mt-8 bg-white rounded-lg shadow-md p-6">
              <h3 className="text-lg font-semibold text-gray-900 mb-4">🌟 Smart Features</h3>
              <ul className="space-y-3">
                <li className="flex items-start">
                  <div className="flex-shrink-0 h-5 w-5 text-green-500 mt-0.5">✓</div>
                  <div className="ml-3">
                    <p className="text-sm text-gray-700">
                      <span className="font-medium">Trip-based matching:</span> We analyze your trip type to suggest the best vehicle category
                    </p>
                  </div>
                </li>
                <li className="flex items-start">
                  <div className="flex-shrink-0 h-5 w-5 text-green-500 mt-0.5">✓</div>
                  <div className="ml-3">
                    <p className="text-sm text-gray-700">
                      <span className="font-medium">Personalized recommendations:</span> Learn from your booking history for better suggestions
                    </p>
                  </div>
                </li>
                <li className="flex items-start">
                  <div className="flex-shrink-0 h-5 w-5 text-green-500 mt-0.5">✓</div>
                  <div className="ml-3">
                    <p className="text-sm text-gray-700">
                      <span className="font-medium">Smart add-ons:</span> Suggested accessories and extras based on your trip needs
                    </p>
                  </div>
                </li>
                <li className="flex items-start">
                  <div className="flex-shrink-0 h-5 w-5 text-green-500 mt-0.5">✓</div>
                  <div className="ml-3">
                    <p className="text-sm text-gray-700">
                      <span className="font-medium">Weather-aware:</span> Recommendations consider current weather conditions
                    </p>
                  </div>
                </li>
              </ul>
            </div>
          </div>

          {/* Right Column - Results */}
          <div>
            {loading && (
              <div className="bg-white rounded-lg shadow-md p-8 text-center">
                <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-indigo-600 mx-auto mb-4"></div>
                <p className="text-gray-600">Analyzing your requirements and finding perfect matches...</p>
              </div>
            )}

            {error && (
              <div className="bg-red-50 border border-red-200 rounded-lg p-4 mb-6">
                <div className="flex">
                  <div className="flex-shrink-0">
                    <svg className="h-5 w-5 text-red-400" viewBox="0 0 20 20" fill="currentColor">
                      <path fillRule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zM8.707 7.293a1 1 0 00-1.414 1.414L8.586 10l-1.293 1.293a1 1 0 101.414 1.414L10 11.414l1.293 1.293a1 1 0 001.414-1.414L11.414 10l1.293-1.293a1 1 0 00-1.414-1.414L10 8.586 8.707 7.293z" clipRule="evenodd" />
                    </svg>
                  </div>
                  <div className="ml-3">
                    <p className="text-sm text-red-800">{error}</p>
                  </div>
                </div>
              </div>
            )}

            {recommendations && (
              <div className="space-y-6">
                {/* Trip Analysis */}
                {recommendations.tripTypeAnalysis && (
                  <div className="bg-blue-50 border border-blue-200 rounded-lg p-4">
                    <h3 className="text-sm font-medium text-blue-900 mb-2">📊 Trip Analysis</h3>
                    <p className="text-sm text-blue-800">{recommendations.tripTypeAnalysis}</p>
                  </div>
                )}

                {/* Personalized Message */}
                {recommendations.personalizedMessage && (
                  <div className="bg-green-50 border border-green-200 rounded-lg p-4">
                    <h3 className="text-sm font-medium text-green-900 mb-2">💡 Our Recommendation</h3>
                    <p className="text-sm text-green-800">{recommendations.personalizedMessage}</p>
                  </div>
                )}

                {/* Recommended Vehicles */}
                <div>
                  <h3 className="text-lg font-semibold text-gray-900 mb-4">
                    Your Perfect Matches ({recommendations.recommendations.length})
                  </h3>
                  <div className="space-y-6">
                    {recommendations.recommendations.map((rec) => (
                      <SmartVehicleCard
                        key={rec.vehicleId}
                        recommendation={rec}
                        onBook={handleBookVehicle}
                        onViewDetails={handleViewDetails}
                      />
                    ))}
                  </div>
                </div>

                {/* Suggested Add-ons */}
                {recommendations.suggestedAddOns && recommendations.suggestedAddOns.length > 0 && (
                  <div className="bg-yellow-50 border border-yellow-200 rounded-lg p-4">
                    <h3 className="text-sm font-medium text-yellow-900 mb-3">🎒 Recommended Add-ons for Your Trip</h3>
                    <div className="flex flex-wrap gap-2">
                      {recommendations.suggestedAddOns.map((addon, index) => (
                        <span key={index} className="bg-yellow-100 text-yellow-800 px-3 py-1 rounded-full text-sm">
                          {addon}
                        </span>
                      ))}
                    </div>
                  </div>
                )}

                {/* Action Buttons */}
                <div className="flex space-x-4">
                  <button
                    onClick={() => setRecommendations(null)}
                    className="flex-1 bg-gray-100 hover:bg-gray-200 text-gray-700 py-3 px-4 rounded-md font-medium transition-colors duration-200"
                  >
                    🔄 Try Different Criteria
                  </button>
                  <button
                    onClick={() => navigate('/vehicles')}
                    className="flex-1 bg-indigo-600 hover:bg-indigo-700 text-white py-3 px-4 rounded-md font-medium transition-colors duration-200"
                  >
                    🔍 Browse All Vehicles
                  </button>
                </div>
              </div>
            )}
          </div>
        </div>
      </div>
    </div>
  );
};

export default SmartRecommendations;