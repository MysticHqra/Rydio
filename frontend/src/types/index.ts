// Re-export API types for convenience
export * from './api';
export * from './recommendation';

// Legacy types (kept for backward compatibility)
export interface User {
  id: string;
  name: string;
  email: string;
  role: 'user' | 'admin';
  documents?: string[];
}

export interface Vehicle {
  id: string;
  type: '2W' | '4W';
  model: string;
  brand: string;
  availability: boolean;
  pricePerHour: number;
  image?: string;
  location: string;
}

export interface Booking {
  id: string;
  userId: string;
  vehicleId: string;
  startTime: Date;
  endTime: Date;
  totalPrice: number;
  status: 'pending' | 'confirmed' | 'active' | 'completed' | 'cancelled';
}

export interface Payment {
  id: string;
  bookingId: string;
  method: string;
  transactionId: string;
  status: 'pending' | 'completed' | 'failed';
}