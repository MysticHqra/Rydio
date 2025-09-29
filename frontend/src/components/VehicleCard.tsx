import React from 'react';
import { Vehicle } from '../types/api';

interface VehicleCardProps {
  vehicle: Vehicle;
  onBook?: (vehicleId: string | number) => void;
  onEdit?: (vehicle: Vehicle) => void;
  onDelete?: (vehicleId: string | number) => void;
  isAdmin?: boolean;
}

const VehicleCard: React.FC<VehicleCardProps> = ({
  vehicle,
  onBook,
  onEdit,
  onDelete,
  isAdmin = false,
}) => {
  return (
    <div className="bg-white rounded-lg shadow-md overflow-hidden hover:shadow-lg transition-shadow duration-300">
      <div className="relative">
        <img
          src={vehicle.imageUrl || '/placeholder-vehicle.jpg'}
          alt={`${vehicle.brand} ${vehicle.model}`}
          className="w-full h-48 object-cover"
        />
        <div className="absolute top-2 right-2">
          <span
            className={`px-2 py-1 rounded-full text-xs font-medium ${
              vehicle.status === 'AVAILABLE'
                ? 'bg-green-100 text-green-800'
                : 'bg-red-100 text-red-800'
            }`}
          >
            {vehicle.status === 'AVAILABLE' ? 'Available' : 'Unavailable'}
          </span>
        </div>
        <div className="absolute top-2 left-2">
          <span className="bg-blue-100 text-blue-800 px-2 py-1 rounded-full text-xs font-medium">
            {['BIKE', 'SCOOTER', 'BICYCLE'].includes(vehicle.vehicleType) ? 'Two Wheeler' : 'Four Wheeler'}
          </span>
        </div>
      </div>
      
      <div className="p-4">
        <h3 className="text-lg font-semibold text-gray-900">
          {vehicle.brand} {vehicle.model}
        </h3>
        <p className="text-gray-600 text-sm mb-2">{vehicle.location}</p>
        
        <div className="flex justify-between items-center mb-4">
          <span className="text-2xl font-bold text-indigo-600">
            ₹{vehicle.hourlyRate || Math.round(vehicle.dailyRate / 24)}/hr
          </span>
        </div>
        
        {isAdmin ? (
          <div className="flex space-x-2">
            <button
              onClick={() => onEdit && onEdit(vehicle)}
              className="flex-1 bg-yellow-600 hover:bg-yellow-700 text-white py-2 px-4 rounded-md text-sm font-medium"
            >
              Edit
            </button>
            <button
              onClick={() => onDelete && onDelete(vehicle.id)}
              className="flex-1 bg-red-600 hover:bg-red-700 text-white py-2 px-4 rounded-md text-sm font-medium"
            >
              Delete
            </button>
          </div>
        ) : (
          <button
            onClick={() => onBook && onBook(vehicle.id)}
            disabled={vehicle.status !== 'AVAILABLE'}
            className={`w-full py-2 px-4 rounded-md text-sm font-medium ${
              vehicle.status === 'AVAILABLE'
                ? 'bg-indigo-600 hover:bg-indigo-700 text-white'
                : 'bg-gray-300 text-gray-500 cursor-not-allowed'
            }`}
          >
            {vehicle.status === 'AVAILABLE' ? 'Book Now' : 'Unavailable'}
          </button>
        )}
      </div>
    </div>
  );
};

export default VehicleCard;