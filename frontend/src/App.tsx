import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import { AuthProvider } from './context/AuthContext';
import Header from './components/Header';
import Home from './pages/Home';
import Login from './pages/Login';
import Register from './pages/Register';
import VehicleList from './pages/VehicleList';
import Booking from './pages/Booking';

function App() {
  return (
    <AuthProvider>
      <Router>
        <div className="min-h-screen bg-gray-50">
          <Header />
          <Routes>
            <Route path="/" element={<Home />} />
            <Route path="/login" element={<Login />} />
            <Route path="/register" element={<Register />} />
            <Route path="/vehicles" element={<VehicleList />} />
            <Route path="/booking/:vehicleId" element={<Booking />} />
          </Routes>
        </div>
      </Router>
    </AuthProvider>
  );
}

export default App;
