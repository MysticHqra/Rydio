import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { useAppContext } from '../context/AppContext';
import { vehicleService, bookingService } from '../services/api';
import { Vehicle } from '../types';

const Booking: React.FC = () => {
  const { vehicleId } = useParams<{ vehicleId: string }>();
  const [vehicle, setVehicle] = useState<Vehicle | null>(null);
  const [loading, setLoading] = useState(true);
  const [bookingLoading, setBookingLoading] = useState(false);
  const [bookingData, setBookingData] = useState({
    startDate: '',
    startTime: '',
    endDate: '',
    endTime: '',
  });
  const [totalPrice, setTotalPrice] = useState(0);
  const [error, setError] = useState('');

  const { state } = useAppContext();
  const navigate = useNavigate();

  useEffect(() => {
    if (!state.isAuthenticated) {
      navigate('/login');
      return;
    }
    if (vehicleId) {
      fetchVehicle();
    }
  }, [vehicleId, state.isAuthenticated]);

  useEffect(() => {
    calculateTotalPrice();
  }, [bookingData, vehicle]);

  const fetchVehicle = async () => {
    try {
      const data = await vehicleService.getById(vehicleId!);
      setVehicle(data);
    } catch (error) {
      setError('Failed to fetch vehicle details');
    } finally {
      setLoading(false);
    }
  };

  const calculateTotalPrice = () => {
    if (!vehicle || !bookingData.startDate || !bookingData.endDate || 
        !bookingData.startTime || !bookingData.endTime) {
      setTotalPrice(0);
      return;
    }

    const startDateTime = new Date(`${bookingData.startDate}T${bookingData.startTime}`);
    const endDateTime = new Date(`${bookingData.endDate}T${bookingData.endTime}`);
    
    if (endDateTime <= startDateTime) {
      setTotalPrice(0);
      return;
    }

    const hours = (endDateTime.getTime() - startDateTime.getTime()) / (1000 * 60 * 60);
    setTotalPrice(Math.round(hours * vehicle.pricePerHour * 100) / 100);
  };

  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setBookingData({
      ...bookingData,
      [e.target.name]: e.target.value,
    });
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setBookingLoading(true);
    setError('');

    if (totalPrice <= 0) {
      setError('Please select valid dates and times');
      setBookingLoading(false);
      return;
    }

    try {
      const startDateTime = new Date(`${bookingData.startDate}T${bookingData.startTime}`);
      const endDateTime = new Date(`${bookingData.endDate}T${bookingData.endTime}`);

      await bookingService.create({
        vehicleId: vehicle!.id,
        startTime: startDateTime.toISOString(),
        endTime: endDateTime.toISOString(),
        totalPrice,
      });

      navigate('/profile', { 
        state: { message: 'Booking created successfully!' } 
      });
    } catch (err: any) {
      setError(err.response?.data?.message || 'Booking failed');
    } finally {
      setBookingLoading(false);
    }
  };

  if (loading) {
    return (
      <div className="min-h-screen flex items-center justify-center">
        <div className="animate-spin rounded-full h-32 w-32 border-b-2 border-primary-600"></div>
      </div>
    );
  }

  if (!vehicle) {
    return (
      <div className="min-h-screen flex items-center justify-center">
        <div className="text-center">
          <h2 className="text-2xl font-bold text-gray-900">Vehicle not found</h2>
          <button
            onClick={() => navigate('/vehicles')}
            className="mt-4 bg-primary-600 text-white px-4 py-2 rounded-md"
          >
            Back to Vehicles
          </button>
        </div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gray-50 py-8">
      <div className="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="bg-white rounded-lg shadow-lg overflow-hidden">
          <div className="md:flex">
            {/* Vehicle Details */}
            <div className="md:w-1/2 p-6">
              <img
                src={vehicle.image || '/placeholder-vehicle.jpg'}
                alt={`${vehicle.brand} ${vehicle.model}`}
                className="w-full h-64 object-cover rounded-lg mb-4"
              />
              <h1 className="text-2xl font-bold text-gray-900 mb-2">
                {vehicle.brand} {vehicle.model}
              </h1>
              <p className="text-gray-600 mb-2">{vehicle.location}</p>
              <div className="flex items-center mb-4">
                <span className="text-3xl font-bold text-primary-600">
                  ₹{vehicle.pricePerHour}/hr
                </span>
                <span className={`ml-4 px-2 py-1 rounded-full text-xs font-medium ${
                  vehicle.availability
                    ? 'bg-green-100 text-green-800'
                    : 'bg-red-100 text-red-800'
                }`}>
                  {vehicle.availability ? 'Available' : 'Unavailable'}
                </span>
              </div>
              <div className="bg-blue-50 p-4 rounded-lg">
                <h3 className="font-semibold text-blue-800 mb-2">Booking Details</h3>
                <ul className="text-blue-700 text-sm space-y-1">
                  <li>• Valid driving license required</li>
                  <li>• Fuel cost included</li>
                  <li>• 24/7 roadside assistance</li>
                  <li>• Insurance coverage provided</li>
                </ul>
              </div>
            </div>

            {/* Booking Form */}
            <div className="md:w-1/2 p-6 bg-gray-50">
              <h2 className="text-xl font-bold text-gray-900 mb-6">Book This Vehicle</h2>
              
              <form onSubmit={handleSubmit} className="space-y-4">
                <div className="grid grid-cols-2 gap-4">
                  <div>
                    <label className="block text-sm font-medium text-gray-700 mb-2">
                      Start Date
                    </label>
                    <input
                      type="date"
                      name="startDate"
                      required
                      min={new Date().toISOString().split('T')[0]}
                      value={bookingData.startDate}
                      onChange={handleInputChange}
                      className="w-full border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:ring-2 focus:ring-primary-500"
                    />
                  </div>
                  <div>
                    <label className="block text-sm font-medium text-gray-700 mb-2">
                      Start Time
                    </label>
                    <input
                      type="time"
                      name="startTime"
                      required
                      value={bookingData.startTime}
                      onChange={handleInputChange}
                      className="w-full border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:ring-2 focus:ring-primary-500"
                    />
                  </div>
                </div>

                <div className="grid grid-cols-2 gap-4">
                  <div>
                    <label className="block text-sm font-medium text-gray-700 mb-2">
                      End Date
                    </label>
                    <input
                      type="date"
                      name="endDate"
                      required
                      min={bookingData.startDate || new Date().toISOString().split('T')[0]}
                      value={bookingData.endDate}
                      onChange={handleInputChange}
                      className="w-full border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:ring-2 focus:ring-primary-500"
                    />
                  </div>
                  <div>
                    <label className="block text-sm font-medium text-gray-700 mb-2">
                      End Time
                    </label>
                    <input
                      type="time"
                      name="endTime"
                      required
                      value={bookingData.endTime}
                      onChange={handleInputChange}
                      className="w-full border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:ring-2 focus:ring-primary-500"
                    />
                  </div>
                </div>

                {/* Price Summary */}
                <div className="bg-white p-4 rounded-lg border">
                  <h3 className="font-semibold text-gray-900 mb-2">Price Summary</h3>
                  <div className="flex justify-between items-center">
                    <span className="text-gray-600">Total Amount:</span>
                    <span className="text-2xl font-bold text-primary-600">
                      ₹{totalPrice.toFixed(2)}
                    </span>
                  </div>
                  {totalPrice > 0 && (
                    <p className="text-sm text-gray-500 mt-1">
                      Rate: ₹{vehicle.pricePerHour}/hr
                    </p>
                  )}
                </div>

                {error && (
                  <div className="text-red-600 text-sm">{error}</div>
                )}

                <div className="flex space-x-4">
                  <button
                    type="button"
                    onClick={() => navigate('/vehicles')}
                    className="flex-1 border border-gray-300 text-gray-700 py-2 px-4 rounded-md hover:bg-gray-50"
                  >
                    Cancel
                  </button>
                  <button
                    type="submit"
                    disabled={bookingLoading || totalPrice <= 0 || !vehicle.availability}
                    className="flex-1 bg-primary-600 text-white py-2 px-4 rounded-md hover:bg-primary-700 disabled:opacity-50 disabled:cursor-not-allowed"
                  >
                    {bookingLoading ? 'Booking...' : `Book Now - ₹${totalPrice.toFixed(2)}`}
                  </button>
                </div>
              </form>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Booking;