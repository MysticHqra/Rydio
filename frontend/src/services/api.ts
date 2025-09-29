import axios from 'axios';

const API_BASE_URL = process.env.REACT_APP_API_BASE_URL || 'http://localhost:8080/api';

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Request interceptor to add auth token
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

// Response interceptor to handle auth errors
api.interceptors.response.use(
  (response) => response,
  (error) => {
    // Only redirect on 401 if it's not a login or register request
    if (error.response?.status === 401) {
      const isAuthRequest = error.config?.url?.includes('/auth/login') || 
                           error.config?.url?.includes('/auth/register');
      
      if (!isAuthRequest) {
        localStorage.removeItem('token');
        localStorage.removeItem('refreshToken');
        localStorage.removeItem('user');
        window.location.href = '/login';
      }
    }
    return Promise.reject(error);
  }
);

export const authService = {
  login: async (usernameOrEmail: string, password: string) => {
    try {
      const response = await api.post('/auth/login', { usernameOrEmail, password });
      if (response.data.success && response.data.data.token) {
        localStorage.setItem('token', response.data.data.token);
        localStorage.setItem('refreshToken', response.data.data.refreshToken);
        localStorage.setItem('user', JSON.stringify({
          id: response.data.data.userId,
          username: response.data.data.username,
          email: response.data.data.email,
          firstName: response.data.data.firstName || '',
          lastName: response.data.data.lastName || '',
          role: response.data.data.role
        }));
      }
      return response.data;
    } catch (error: any) {
      // Handle authentication errors with user-friendly messages
      if (error.response?.status === 401) {
        const errorMessage = error.response.data?.message || 'Invalid username or password';
        return {
          success: false,
          message: errorMessage,
          data: null
        };
      }
      // Re-throw other errors
      throw error;
    }
  },

  register: async (userData: {
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
  }) => {
    try {
      const response = await api.post('/auth/register', userData);
      return response.data;
    } catch (error: any) {
      // Handle registration errors with user-friendly messages
      if (error.response?.status === 400) {
        const errorMessage = error.response.data?.message || 'Registration failed. Please check your details.';
        return {
          success: false,
          message: errorMessage,
          data: null
        };
      }
      // Re-throw other errors
      throw error;
    }
  },

  logout: () => {
    localStorage.removeItem('token');
    localStorage.removeItem('refreshToken');
    localStorage.removeItem('user');
  },

  getCurrentUser: () => {
    const user = localStorage.getItem('user');
    return user ? JSON.parse(user) : null;
  },

  refreshToken: async () => {
    const refreshToken = localStorage.getItem('refreshToken');
    if (!refreshToken) throw new Error('No refresh token available');
    
    const response = await api.post('/auth/refresh-token', null, {
      params: { refreshToken }
    });
    
    if (response.data.success && response.data.data.token) {
      localStorage.setItem('token', response.data.data.token);
      localStorage.setItem('refreshToken', response.data.data.refreshToken);
      localStorage.setItem('user', JSON.stringify({
        id: response.data.data.userId,
        username: response.data.data.username,
        email: response.data.data.email,
        firstName: response.data.data.firstName || '',
        lastName: response.data.data.lastName || '',
        role: response.data.data.role
      }));
    }
    
    return response.data;
  },
};

export const vehicleService = {
  getAll: async (filters?: {
    vehicleType?: string;
    location?: string;
    minPrice?: number;
    maxPrice?: number;
    page?: number;
    size?: number;
  }) => {
    const response = await api.get('/vehicles');
    return response.data; // Backend now returns proper JSON object
  },

  getById: async (id: string) => {
    const response = await api.get(`/vehicles/${id}`);
    return response.data; // Backend now returns proper JSON object
  },

  search: async (searchParams: {
    vehicleType?: string;
    location?: string;
    startDate?: string;
    endDate?: string;
    minPrice?: number;
    maxPrice?: number;
  }) => {
    const params = new URLSearchParams();
    if (searchParams.vehicleType) params.append('type', searchParams.vehicleType);
    if (searchParams.location) params.append('location', searchParams.location);
    
    const response = await api.get(`/vehicles/search?${params.toString()}`);
    return response.data; // Backend now returns proper JSON object
  },

  // Admin functions - these will need to be implemented in backend later
  create: async (vehicleData: any) => {
    throw new Error('Vehicle creation not implemented yet');
  },

  update: async (id: string, vehicleData: any) => {
    throw new Error('Vehicle update not implemented yet');
  },

  delete: async (id: string) => {
    throw new Error('Vehicle deletion not implemented yet');
  },

  updateStatus: async (id: string, status: string) => {
    throw new Error('Vehicle status update not implemented yet');
  },
};

export const bookingService = {
  create: async (bookingData: {
    vehicleId: string;  // Changed from number to string to match backend
    startDate: string;
    endDate: string;
    pickupLocation: string;
    dropLocation: string;  // Changed from returnLocation to dropLocation to match backend
    securityDeposit?: number;
    notes?: string;
  }) => {
    const response = await api.post('/bookings', {
      vehicleId: bookingData.vehicleId,
      startDate: bookingData.startDate,
      endDate: bookingData.endDate,
      pickupLocation: bookingData.pickupLocation,
      dropLocation: bookingData.dropLocation || bookingData.pickupLocation // Use pickupLocation as fallback
    });
    return response.data;
  },

  getUserBookings: async () => {
    const response = await api.get('/bookings');
    return response.data; // Backend now returns proper JSON object
  },

  getUserBookingsPaged: async (page: number = 0, size: number = 10) => {
    // For now, just return all bookings - pagination can be added later
    return await bookingService.getUserBookings();
  },

  getById: async (id: string) => {
    const response = await api.get(`/bookings/${id}`);
    return response.data;
  },

  getByReference: async (reference: string) => {
    // This endpoint doesn't exist yet, so throw an error for now
    throw new Error('Get booking by reference not implemented yet');
  },

  update: async (id: string, updateData: {
    pickupLocation?: string;
    dropLocation?: string; // Changed from returnLocation
    notes?: string;
  }) => {
    // This endpoint doesn't exist yet, so throw an error for now
    throw new Error('Booking update not implemented yet');
  },

  cancel: async (id: string, reason?: string) => {
    // This endpoint doesn't exist yet, so throw an error for now
    throw new Error('Booking cancellation not implemented yet');
  },

  // Admin functions - these don't exist in backend yet
  getAllBookings: async (page: number = 0, size: number = 10) => {
    throw new Error('Admin booking management not implemented yet');
  },

  confirm: async (id: string) => {
    throw new Error('Booking confirmation not implemented yet');
  },

  activate: async (id: string) => {
    throw new Error('Booking activation not implemented yet');
  },

  complete: async (id: string, lateFee: number = 0, damageCharges: number = 0) => {
    throw new Error('Booking completion not implemented yet');
  },
};

export const paymentService = {
  processPayment: async (paymentData: {
    bookingId: number;
    amount: number;
    paymentType: string;
    paymentMethod: string;
    notes?: string;
    cardNumber?: string;
    cardHolderName?: string;
    expiryMonth?: string;
    expiryYear?: string;
    cvv?: string;
  }) => {
    const response = await api.post('/payments/process', paymentData);
    return response.data;
  },

  getUserPayments: async () => {
    const response = await api.get('/payments/my-payments');
    return response.data;
  },

  getUserPaymentsPaged: async (page: number = 0, size: number = 10) => {
    const response = await api.get('/payments/my-payments/paged', {
      params: { page, size }
    });
    return response.data;
  },

  getById: async (id: string) => {
    const response = await api.get(`/payments/${id}`);
    return response.data;
  },

  getByTransactionId: async (transactionId: string) => {
    const response = await api.get(`/payments/transaction/${transactionId}`);
    return response.data;
  },

  getBookingPayments: async (bookingId: string) => {
    const response = await api.get(`/payments/booking/${bookingId}`);
    return response.data;
  },
};

export const userService = {
  getProfile: async () => {
    const response = await api.get('/users/profile');
    return response.data;
  },

  updateProfile: async (profileData: any) => {
    const response = await api.put('/users/profile', profileData);
    return response.data;
  },
};

export const fileService = {
  upload: async (file: File, subfolder: string = 'general') => {
    const formData = new FormData();
    formData.append('file', file);
    formData.append('subfolder', subfolder);
    
    const response = await api.post('/files/upload', formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
    });
    return response.data;
  },

  getDownloadUrl: (fileName: string) => {
    return `${API_BASE_URL}/files/download/${fileName}`;
  },

  delete: async (fileName: string) => {
    const [subfolder, ...fileNameParts] = fileName.split('/');
    const actualFileName = fileNameParts.join('/');
    const response = await api.delete(`/files/delete/${subfolder}/${actualFileName}`);
    return response.data;
  },
};

export default api;