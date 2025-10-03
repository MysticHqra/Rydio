// API Response Types for Rydio Backend Integration

export interface ApiResponse<T> {
  success: boolean;
  message: string;
  data: T;
}

// User & Authentication Types
export interface User {
  id: number;
  username: string;
  email: string;
  firstName: string;
  lastName: string;
  phoneNumber?: string;
  dateOfBirth?: string;
  driverLicenseNumber?: string;
  driverLicenseExpiry?: string;
  address?: string;
  role: 'USER' | 'ADMIN';
  isActive: boolean;
  emailVerified: boolean;
  createdAt: string;
  updatedAt: string;
}

export interface LoginRequest {
  usernameOrEmail: string;
  password: string;
}

export interface LoginResponse {
  token: string;
  user: User;
}

export interface RegisterRequest {
  username: string;
  email: string;
  password: string;
  firstName: string;
  lastName: string;
  phoneNumber?: string;
  dateOfBirth?: string;
  driverLicenseNumber?: string;
  driverLicenseExpiry?: string;
  address?: string;
}

// Vehicle Types
export interface Vehicle {
  id: number;
  licensePlate: string;
  brand: string;
  model: string;
  year: number;
  color: string;
  vehicleType: 'CAR' | 'BIKE' | 'SCOOTER' | 'BICYCLE';
  fuelType: 'PETROL' | 'DIESEL' | 'ELECTRIC' | 'HYBRID' | 'CNG';
  engineCapacity?: string;
  seatCount?: number;
  dailyRate: number;
  hourlyRate?: number;
  mileage?: number;
  insuranceNumber?: string;
  registrationNumber?: string;
  status: 'AVAILABLE' | 'RENTED' | 'MAINTENANCE' | 'INACTIVE';
  location?: string;
  description?: string;
  features?: string;
  imageUrl?: string;
  createdAt: string;
  updatedAt: string;
}

export interface VehicleSearchRequest {
  vehicleType?: string;
  location?: string;
  startDate?: string;
  endDate?: string;
  minPrice?: number;
  maxPrice?: number;
}

export interface CreateVehicleRequest {
  licensePlate: string;
  brand: string;
  model: string;
  year: number;
  color: string;
  vehicleType: 'CAR' | 'BIKE' | 'SCOOTER' | 'BICYCLE';
  fuelType: 'PETROL' | 'DIESEL' | 'ELECTRIC' | 'HYBRID' | 'CNG';
  engineCapacity?: string;
  seatCount?: number;
  dailyRate: number;
  hourlyRate?: number;
  mileage?: number;
  insuranceNumber?: string;
  registrationNumber?: string;
  location?: string;
  description?: string;
  features?: string;
  imageUrl?: string;
}

// Booking Types
export interface Booking {
  id: number;
  bookingReference: string;
  vehicleId: number;
  vehicle?: Vehicle;
  userId: number;
  user?: User;
  startDate: string;
  endDate: string;
  pickupLocation: string;
  returnLocation: string;
  totalHours: number;
  totalAmount: number;
  securityDeposit?: number;
  status: 'PENDING' | 'CONFIRMED' | 'ACTIVE' | 'COMPLETED' | 'CANCELLED';
  notes?: string;
  cancellationReason?: string;
  createdAt: string;
  updatedAt: string;
}

export interface CreateBookingRequest {
  vehicleId: number;
  startDate: string;
  endDate: string;
  pickupLocation: string;
  returnLocation: string;
  securityDeposit?: number;
  notes?: string;
}

// Payment Types
export interface Payment {
  id: number;
  bookingId: number;
  booking?: Booking;
  userId: number;
  user?: User;
  amount: number;
  paymentType: 'BOOKING' | 'SECURITY_DEPOSIT' | 'LATE_FEE' | 'DAMAGE_CHARGE';
  paymentMethod: 'CREDIT_CARD' | 'DEBIT_CARD' | 'UPI' | 'NET_BANKING' | 'CASH';
  transactionId: string;
  status: 'PENDING' | 'PROCESSING' | 'COMPLETED' | 'FAILED' | 'REFUNDED';
  notes?: string;
  createdAt: string;
  updatedAt: string;
}

export interface CreatePaymentRequest {
  bookingId: number;
  amount: number;
  paymentType: 'BOOKING' | 'SECURITY_DEPOSIT' | 'LATE_FEE' | 'DAMAGE_CHARGE';
  paymentMethod: 'CREDIT_CARD' | 'DEBIT_CARD' | 'UPI' | 'NET_BANKING' | 'CASH';
  notes?: string;
  cardNumber?: string;
  cardHolderName?: string;
  expiryMonth?: string;
  expiryYear?: string;
  cvv?: string;
}

// File Upload Types
export interface FileUploadResponse {
  fileName: string;
  originalFileName: string;
  contentType: string;
  size: number;
  uploadPath: string;
}

// Common Types
export interface PaginatedResponse<T> {
  content: T[];
  page: number;
  size: number;
  totalElements: number;
  totalPages: number;
  first: boolean;
  last: boolean;
}

export interface ErrorResponse {
  success: false;
  message: string;
  data: Record<string, string> | null;
}