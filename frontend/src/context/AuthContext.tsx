import React, { createContext, useContext, useEffect, useState, ReactNode } from 'react';
import { authService } from '../services/api';
import { User as BackendUser, LoginRequest, RegisterRequest } from '../types/api';

interface AuthState {
  user: BackendUser | null;
  isAuthenticated: boolean;
  isLoading: boolean;
  error: string | null;
}

interface AuthContextType extends AuthState {
  login: (credentials: LoginRequest) => Promise<void>;
  register: (userData: RegisterRequest) => Promise<void>;
  logout: () => void;
  clearError: () => void;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (context === undefined) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
};

interface AuthProviderProps {
  children: ReactNode;
}

export const AuthProvider: React.FC<AuthProviderProps> = ({ children }) => {
  const [state, setState] = useState<AuthState>({
    user: null,
    isAuthenticated: false,
    isLoading: true,
    error: null,
  });

  // Check for existing authentication on mount
  useEffect(() => {
    const initAuth = () => {
      try {
        const user = authService.getCurrentUser();
        const token = localStorage.getItem('token');
        
        if (user && token) {
          setState(prev => ({
            ...prev,
            user,
            isAuthenticated: true,
            isLoading: false,
          }));
        } else {
          setState(prev => ({
            ...prev,
            isLoading: false,
          }));
        }
      } catch (error) {
        console.error('Error initializing auth:', error);
        setState(prev => ({
          ...prev,
          isLoading: false,
        }));
      }
    };

    initAuth();
  }, []);

  const login = async (credentials: LoginRequest) => {
    try {
      setState(prev => ({ ...prev, isLoading: true, error: null }));
      
      const response = await authService.login(credentials.usernameOrEmail, credentials.password);
      
      if (response.success) {
        // Get the basic user info from localStorage (from JWT response)
        let user = authService.getCurrentUser();
        
        // Try to fetch the full user profile to get firstName/lastName
        try {
          const { userService } = await import('../services/api');
          const profileResponse = await userService.getProfile();
          if (profileResponse.success) {
            user = {
              ...user,
              firstName: profileResponse.data.firstName,
              lastName: profileResponse.data.lastName,
            };
            // Update localStorage with complete user info
            localStorage.setItem('user', JSON.stringify(user));
          }
        } catch (profileError) {
          console.warn('Could not fetch user profile:', profileError);
          // Continue with basic user info from JWT
        }
        
        setState(prev => ({
          ...prev,
          user,
          isAuthenticated: true,
          isLoading: false,
          error: null,
        }));
      } else {
        throw new Error(response.message || 'Login failed');
      }
    } catch (error: any) {
      console.error('Login error:', error);
      setState(prev => ({
        ...prev,
        isLoading: false,
        error: error.message || 'Login failed',
      }));
      throw error;
    }
  };

  const register = async (userData: RegisterRequest) => {
    try {
      setState(prev => ({ ...prev, isLoading: true, error: null }));
      
      const response = await authService.register(userData);
      
      if (response.success) {
        setState(prev => ({
          ...prev,
          isLoading: false,
          error: null,
        }));
      } else {
        throw new Error(response.message || 'Registration failed');
      }
    } catch (error: any) {
      console.error('Registration error:', error);
      let errorMessage = 'Registration failed';
      
      // Parse backend validation errors
      if (error.response?.data?.message) {
        errorMessage = error.response.data.message;
      } else if (error.response?.status === 400) {
        errorMessage = 'Please check your input and try again';
      } else if (error.message) {
        errorMessage = error.message;
      }
      
      setState(prev => ({
        ...prev,
        isLoading: false,
        error: errorMessage,
      }));
      throw error;
    }
  };

  const logout = () => {
    authService.logout();
    setState({
      user: null,
      isAuthenticated: false,
      isLoading: false,
      error: null,
    });
  };

  const clearError = () => {
    setState(prev => ({ ...prev, error: null }));
  };

  const value: AuthContextType = {
    ...state,
    login,
    register,
    logout,
    clearError,
  };

  return (
    <AuthContext.Provider value={value}>
      {children}
    </AuthContext.Provider>
  );
};