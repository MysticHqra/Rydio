import React, { useState } from 'react';
import { vehicleService, bookingService, authService } from '../services/api';

const IntegrationTest: React.FC = () => {
  const [testResults, setTestResults] = useState<any[]>([]);
  const [loading, setLoading] = useState(false);

  const addResult = (test: string, success: boolean, data?: any, error?: any) => {
    setTestResults(prev => [...prev, {
      test,
      success,
      data: success ? data : null,
      error: success ? null : error?.message || error,
      timestamp: new Date().toLocaleTimeString()
    }]);
  };

  const runTests = async () => {
    setLoading(true);
    setTestResults([]);

    // Test 1: Get Vehicles
    try {
      const vehicles = await vehicleService.getAll();
      addResult('Get All Vehicles', true, `Found ${vehicles.data.length} vehicles`);
    } catch (error) {
      addResult('Get All Vehicles', false, null, error);
    }

    // Test 2: Get Individual Vehicle
    try {
      const vehicle = await vehicleService.getById('1');
      addResult('Get Vehicle by ID', true, `Found vehicle: ${vehicle.data.brand} ${vehicle.data.model}`);
    } catch (error) {
      addResult('Get Vehicle by ID', false, null, error);
    }

    // Test 3: Search Vehicles
    try {
      const searchResults = await vehicleService.search({ vehicleType: 'CAR' });
      addResult('Search Vehicles (CAR)', true, `Found ${searchResults.data.length} cars`);
    } catch (error) {
      addResult('Search Vehicles (CAR)', false, null, error);
    }

    // Test 4: Create Booking
    try {
      const booking = await bookingService.create({
        vehicleId: '1',
        startDate: '2025-10-01',
        endDate: '2025-10-02',
        pickupLocation: 'Test Pickup',
        dropLocation: 'Test Drop'
      });
      addResult('Create Booking', true, `Booking created: ${booking.data.id}`);
    } catch (error) {
      addResult('Create Booking', false, null, error);
    }

    // Test 5: Get User Bookings
    try {
      const bookings = await bookingService.getUserBookings();
      addResult('Get User Bookings', true, `Found ${bookings.data.length} bookings`);
    } catch (error) {
      addResult('Get User Bookings', false, null, error);
    }

    setLoading(false);
  };

  const clearResults = () => {
    setTestResults([]);
  };

  return (
    <div className="min-h-screen bg-gray-50 py-8">
      <div className="max-w-4xl mx-auto px-4">
        <div className="bg-white rounded-lg shadow-lg p-6">
          <h1 className="text-3xl font-bold text-gray-900 mb-6">
            Frontend-Backend Integration Test
          </h1>
          
          <div className="mb-6">
            <button
              onClick={runTests}
              disabled={loading}
              className="bg-blue-600 text-white px-6 py-2 rounded-md hover:bg-blue-700 disabled:opacity-50 mr-4"
            >
              {loading ? 'Running Tests...' : 'Run Integration Tests'}
            </button>
            
            <button
              onClick={clearResults}
              className="bg-gray-600 text-white px-6 py-2 rounded-md hover:bg-gray-700"
            >
              Clear Results
            </button>
          </div>

          {testResults.length > 0 && (
            <div className="space-y-4">
              <h2 className="text-xl font-semibold">Test Results:</h2>
              
              {testResults.map((result, index) => (
                <div
                  key={index}
                  className={`p-4 rounded-md border-l-4 ${
                    result.success
                      ? 'bg-green-50 border-green-500'
                      : 'bg-red-50 border-red-500'
                  }`}
                >
                  <div className="flex items-center justify-between">
                    <div className="flex items-center">
                      <span
                        className={`w-3 h-3 rounded-full mr-3 ${
                          result.success ? 'bg-green-500' : 'bg-red-500'
                        }`}
                      />
                      <span className="font-medium">{result.test}</span>
                    </div>
                    <span className="text-sm text-gray-500">
                      {result.timestamp}
                    </span>
                  </div>
                  
                  {result.data && (
                    <div className="mt-2 text-sm text-green-700">
                      ✓ {result.data}
                    </div>
                  )}
                  
                  {result.error && (
                    <div className="mt-2 text-sm text-red-700">
                      ✗ {result.error}
                    </div>
                  )}
                </div>
              ))}

              <div className="mt-6 p-4 bg-blue-50 rounded-md">
                <h3 className="font-medium text-blue-900 mb-2">Test Summary:</h3>
                <div className="text-sm text-blue-800">
                  <span className="text-green-600 font-medium">
                    {testResults.filter(r => r.success).length} Passed
                  </span>
                  {' • '}
                  <span className="text-red-600 font-medium">
                    {testResults.filter(r => !r.success).length} Failed
                  </span>
                  {' • '}
                  <span className="text-blue-600 font-medium">
                    {testResults.length} Total
                  </span>
                </div>
              </div>
            </div>
          )}

          <div className="mt-8 p-4 bg-gray-50 rounded-md">
            <h3 className="font-medium text-gray-900 mb-2">Available Test Accounts:</h3>
            <div className="text-sm text-gray-700 space-y-1">
              <div>• <code>admin</code> / <code>admin123</code> (Admin)</div>
              <div>• <code>hara</code> / <code>Hara@1234</code> (User)</div>
              <div>• <code>testuser</code> / <code>password123</code> (User)</div>
            </div>
          </div>

          <div className="mt-4 p-4 bg-yellow-50 rounded-md">
            <h3 className="font-medium text-yellow-900 mb-2">Integration Status:</h3>
            <div className="text-sm text-yellow-800 space-y-1">
              <div>✅ Authentication (login, register, logout)</div>
              <div>✅ Vehicle Service (list, search, get by ID)</div>
              <div>✅ Booking Service (create, list user bookings)</div>
              <div>✅ User Profile Service</div>
              <div>⚠️ Advanced booking management (admin features)</div>
              <div>⚠️ Payment processing</div>
              <div>⚠️ File upload services</div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default IntegrationTest;