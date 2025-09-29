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
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);

export const authService = {
  login: async (email: string, password: string) => {
    const response = await api.post('/auth/login', { email, password });
    if (response.data.token) {
      localStorage.setItem('token', response.data.token);
    }
    return response.data;
  },

  register: async (userData: { name: string; email: string; password: string }) => {
    const response = await api.post('/auth/register', userData);
    return response.data;
  },

  logout: () => {
    localStorage.removeItem('token');
  },

  getCurrentUser: async () => {
    const response = await api.get('/auth/me');
    return response.data;
  },
};

export const vehicleService = {
  getAll: async () => {
    const response = await api.get('/vehicles');
    return response.data;
  },

  getById: async (id: string) => {
    const response = await api.get(`/vehicles/${id}`);
    return response.data;
  },

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
};

export const bookingService = {
  create: async (bookingData: any) => {
    const response = await api.post('/bookings', bookingData);
    return response.data;
  },

  getUserBookings: async () => {
    const response = await api.get('/bookings/user');
    return response.data;
  },

  getAllBookings: async () => {
    const response = await api.get('/bookings');
    return response.data;
  },

  updateStatus: async (id: string, status: string) => {
    const response = await api.put(`/bookings/${id}/status`, { status });
    return response.data;
  },
};

export default api;