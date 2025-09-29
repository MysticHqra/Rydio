import React, { createContext, useContext, useReducer, ReactNode } from 'react';
import { User, Vehicle, Booking } from '../types';

interface AppState {
  user: User | null;
  isAuthenticated: boolean;
  vehicles: Vehicle[];
  bookings: Booking[];
  loading: boolean;
  error: string | null;
}

type AppAction =
  | { type: 'SET_USER'; payload: User | null }
  | { type: 'SET_VEHICLES'; payload: Vehicle[] }
  | { type: 'SET_BOOKINGS'; payload: Booking[] }
  | { type: 'SET_LOADING'; payload: boolean }
  | { type: 'SET_ERROR'; payload: string | null }
  | { type: 'ADD_BOOKING'; payload: Booking }
  | { type: 'UPDATE_VEHICLE'; payload: Vehicle }
  | { type: 'LOGOUT' };

const initialState: AppState = {
  user: null,
  isAuthenticated: false,
  vehicles: [],
  bookings: [],
  loading: false,
  error: null,
};

const appReducer = (state: AppState, action: AppAction): AppState => {
  switch (action.type) {
    case 'SET_USER':
      return {
        ...state,
        user: action.payload,
        isAuthenticated: !!action.payload,
      };
    case 'SET_VEHICLES':
      return {
        ...state,
        vehicles: action.payload,
      };
    case 'SET_BOOKINGS':
      return {
        ...state,
        bookings: action.payload,
      };
    case 'SET_LOADING':
      return {
        ...state,
        loading: action.payload,
      };
    case 'SET_ERROR':
      return {
        ...state,
        error: action.payload,
      };
    case 'ADD_BOOKING':
      return {
        ...state,
        bookings: [...state.bookings, action.payload],
      };
    case 'UPDATE_VEHICLE':
      return {
        ...state,
        vehicles: state.vehicles.map(v => 
          v.id === action.payload.id ? action.payload : v
        ),
      };
    case 'LOGOUT':
      return {
        ...state,
        user: null,
        isAuthenticated: false,
        bookings: [],
      };
    default:
      return state;
  }
};

interface AppContextType {
  state: AppState;
  dispatch: React.Dispatch<AppAction>;
}

const AppContext = createContext<AppContextType | undefined>(undefined);

export const useAppContext = () => {
  const context = useContext(AppContext);
  if (context === undefined) {
    throw new Error('useAppContext must be used within an AppProvider');
  }
  return context;
};

interface AppProviderProps {
  children: ReactNode;
}

export const AppProvider: React.FC<AppProviderProps> = ({ children }) => {
  const [state, dispatch] = useReducer(appReducer, initialState);

  return (
    <AppContext.Provider value={{ state, dispatch }}>
      {children}
    </AppContext.Provider>
  );
};