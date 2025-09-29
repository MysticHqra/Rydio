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
    if (error.response?.status === 401) {
      localStorage.removeItem('token');
      localStorage.removeItem('refreshToken');
      localStorage.removeItem('user');
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);

export const authService = {
  login: async (usernameOrEmail: string, password: string) => {
    const response = await api.post('/auth/login', { usernameOrEmail, password });
    if (response.data.success && response.data.data.token) {
      localStorage.setItem('token', response.data.data.token);
      localStorage.setItem('refreshToken', response.data.data.refreshToken);
      localStorage.setItem('user', JSON.stringify({
        id: response.data.data.userId,
        username: response.data.data.username,
        email: response.data.data.email,
        firstName: '', // Not provided in JWT response, will fetch separately if needed
        lastName: '', // Not provided in JWT response, will fetch separately if needed
        role: response.data.data.role
      }));
    }
    return response.data;
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
    const response = await api.post('/auth/register', userData);
    return response.data;
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
        firstName: '',
        lastName: '',
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
    const response = await api.get('/vehicles', { params: filters });
    return response.data;
  },

  getById: async (id: string) => {
    const response = await api.get(`/vehicles/${id}`);
    return response.data;
  },

  search: async (searchParams: {
    vehicleType?: string;
    location?: string;
    startDate?: string;
    endDate?: string;
    minPrice?: number;
    maxPrice?: number;
  }) => {
    const response = await api.post('/vehicles/search', searchParams);
    return response.data;
  },

  // Admin functions
  create: async (vehicleData: any) => {
    const response = await api.post('/vehicles', vehicleData);
    return response.data;
  },

  update: async (id: string, vehicleData: any) => {
    const response = await api.put(`/vehicles/${id}`, vehicleData);
    return response.data;
  },

  delete: async (id: string) => {
    const response = await api.delete(`/vehicles/${id}`);
    return response.data;
  },

  updateStatus: async (id: string, status: string) => {
    const response = await api.put(`/vehicles/${id}/status`, null, {
      params: { status }
    });
    return response.data;
  },
};

export const bookingService = {
  create: async (bookingData: {
    vehicleId: number;
    startDate: string;
    endDate: string;
    pickupLocation: string;
    returnLocation: string;
    securityDeposit?: number;
    notes?: string;
  }) => {
    const response = await api.post('/bookings', bookingData);
    return response.data;
  },

  getUserBookings: async () => {
    const response = await api.get('/bookings');
    return response.data;
  },

  getUserBookingsPaged: async (page: number = 0, size: number = 10) => {
    const response = await api.get('/bookings', {
      params: { page, size }
    });
    return response.data;
  },

  getById: async (id: string) => {
    const response = await api.get(`/bookings/${id}`);
    return response.data;
  },

  getByReference: async (reference: string) => {
    const response = await api.get(`/bookings/reference/${reference}`);
    return response.data;
  },

  update: async (id: string, updateData: {
    pickupLocation?: string;
    returnLocation?: string;
    notes?: string;
  }) => {
    const response = await api.put(`/bookings/${id}`, updateData);
    return response.data;
  },

  cancel: async (id: string, reason?: string) => {
    const response = await api.post(`/bookings/${id}/cancel`, null, {
      params: { reason }
    });
    return response.data;
  },

  // Admin functions
  getAllBookings: async (page: number = 0, size: number = 10) => {
    const response = await api.get('/bookings/admin/all', {
      params: { page, size }
    });
    return response.data;
  },

  confirm: async (id: string) => {
    const response = await api.post(`/bookings/admin/${id}/confirm`);
    return response.data;
  },

  activate: async (id: string) => {
    const response = await api.post(`/bookings/admin/${id}/activate`);
    return response.data;
  },

  complete: async (id: string, lateFee: number = 0, damageCharges: number = 0) => {
    const response = await api.post(`/bookings/admin/${id}/complete`, null, {
      params: { lateFee, damageCharges }
    });
    return response.data;
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