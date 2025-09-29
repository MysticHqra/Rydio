import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { vehicleService } from '../services/api';
import VehicleCard from '../components/VehicleCard';
import { Vehicle } from '../types/api';

const VehicleList: React.FC = () => {
  const [vehicles, setVehicles] = useState<Vehicle[]>([]);
  const [filteredVehicles, setFilteredVehicles] = useState<Vehicle[]>([]);
  const [loading, setLoading] = useState(true);
  const [filters, setFilters] = useState({
    type: 'all',
    availability: 'all',
    priceRange: 'all',
    location: '',
  });

  const { isAuthenticated } = useAuth();
  const navigate = useNavigate();

  useEffect(() => {
    fetchVehicles();
  }, []);

  useEffect(() => {
    applyFilters();
  }, [vehicles, filters]);

  const fetchVehicles = async () => {
    try {
      const response = await vehicleService.getAll();
      // Handle API response structure - data might be in response.data
      const data = response.data || response;
      setVehicles(Array.isArray(data) ? data : []);
    } catch (error) {
      console.error('Failed to fetch vehicles:', error);
      setVehicles([]);
    } finally {
      setLoading(false);
    }
  };

  const applyFilters = () => {
    let filtered = vehicles;

    if (filters.type !== 'all') {
      // Map filter values to backend vehicleType values
      const typeMapping = {
        '2W': ['BIKE', 'SCOOTER', 'BICYCLE'],
        '4W': ['CAR']
      };
      const allowedTypes = typeMapping[filters.type as keyof typeof typeMapping] || [];
      filtered = filtered.filter(vehicle => allowedTypes.includes(vehicle.vehicleType));
    }

    if (filters.availability !== 'all') {
      const isAvailable = filters.availability === 'available';
      filtered = filtered.filter(vehicle => 
        isAvailable ? vehicle.status === 'AVAILABLE' : vehicle.status !== 'AVAILABLE'
      );
    }

    if (filters.priceRange !== 'all') {
      const [min, max] = filters.priceRange.split('-').map(Number);
      if (max) {
        filtered = filtered.filter(vehicle => {
          const rate = vehicle.hourlyRate || vehicle.dailyRate / 24;
          return rate >= min && rate <= max;
        });
      } else {
        filtered = filtered.filter(vehicle => {
          const rate = vehicle.hourlyRate || vehicle.dailyRate / 24;
          return rate >= min;
        });
      }
    }

    if (filters.location) {
      filtered = filtered.filter(vehicle => 
        vehicle.location?.toLowerCase().includes(filters.location.toLowerCase())
      );
    }

    setFilteredVehicles(filtered);
  };

  const handleFilterChange = (key: string, value: string) => {
    setFilters(prev => ({ ...prev, [key]: value }));
  };

  const handleBookVehicle = (vehicleId: string | number) => {
    if (!isAuthenticated) {
      navigate('/login');
      return;
    }
    navigate(`/booking/${vehicleId}`);
  };

  if (loading) {
    return (
      <div className="min-h-screen flex items-center justify-center">
        <div className="animate-spin rounded-full h-32 w-32 border-b-2 border-primary-600"></div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gray-50 py-8">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="mb-8">
          <h1 className="text-3xl font-bold text-gray-900">Available Vehicles</h1>
          <p className="mt-2 text-gray-600">Find the perfect vehicle for your journey</p>
        </div>

        {/* Filters */}
        <div className="bg-white p-6 rounded-lg shadow-sm mb-8">
          <h2 className="text-lg font-semibold mb-4">Filters</h2>
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4">
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">
                Vehicle Type
              </label>
              <select
                value={filters.type}
                onChange={(e) => handleFilterChange('type', e.target.value)}
                className="w-full border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:ring-2 focus:ring-primary-500"
              >
                <option value="all">All Types</option>
                <option value="2W">Two Wheeler</option>
                <option value="4W">Four Wheeler</option>
              </select>
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">
                Availability
              </label>
              <select
                value={filters.availability}
                onChange={(e) => handleFilterChange('availability', e.target.value)}
                className="w-full border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:ring-2 focus:ring-primary-500"
              >
                <option value="all">All</option>
                <option value="available">Available</option>
                <option value="unavailable">Unavailable</option>
              </select>
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">
                Price Range
              </label>
              <select
                value={filters.priceRange}
                onChange={(e) => handleFilterChange('priceRange', e.target.value)}
                className="w-full border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:ring-2 focus:ring-primary-500"
              >
                <option value="all">All Prices</option>
                <option value="0-100">₹0 - ₹100</option>
                <option value="100-300">₹100 - ₹300</option>
                <option value="300-500">₹300 - ₹500</option>
                <option value="500">₹500+</option>
              </select>
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">
                Location
              </label>
              <input
                type="text"
                placeholder="Search by location"
                value={filters.location}
                onChange={(e) => handleFilterChange('location', e.target.value)}
                className="w-full border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:ring-2 focus:ring-primary-500"
              />
            </div>
          </div>
        </div>

        {/* Results */}
        <div className="mb-4">
          <p className="text-gray-600">
            Showing {filteredVehicles.length} of {vehicles.length} vehicles
          </p>
        </div>

        {/* Vehicle Grid */}
        {filteredVehicles.length === 0 ? (
          <div className="text-center py-12">
            <p className="text-gray-500 text-lg">No vehicles found matching your criteria.</p>
          </div>
        ) : (
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
            {filteredVehicles.map((vehicle) => (
              <VehicleCard
                key={vehicle.id}
                vehicle={vehicle}
                onBook={handleBookVehicle}
              />
            ))}
          </div>
        )}
      </div>
    </div>
  );
};

export default VehicleList;