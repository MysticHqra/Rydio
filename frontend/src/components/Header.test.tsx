import React from 'react';
import { render, screen } from '@testing-library/react';
import { BrowserRouter } from 'react-router-dom';
import { AppProvider } from '../context/AppContext';
import Header from '../components/Header';

const renderWithProviders = (component: React.ReactElement) => {
  return render(
    <BrowserRouter>
      <AppProvider>
        {component}
      </AppProvider>
    </BrowserRouter>
  );
};

describe('Header Component', () => {
  test('renders Rydio logo', () => {
    renderWithProviders(<Header />);
    const logoElement = screen.getByText(/Rydio/i);
    expect(logoElement).toBeInTheDocument();
  });

  test('renders navigation links', () => {
    renderWithProviders(<Header />);
    const homeLink = screen.getByText(/Home/i);
    const vehiclesLink = screen.getByText(/Vehicles/i);
    expect(homeLink).toBeInTheDocument();
    expect(vehiclesLink).toBeInTheDocument();
  });

  test('renders login and register buttons when not authenticated', () => {
    renderWithProviders(<Header />);
    const loginButton = screen.getByText(/Login/i);
    const registerButton = screen.getByText(/Register/i);
    expect(loginButton).toBeInTheDocument();
    expect(registerButton).toBeInTheDocument();
  });
});