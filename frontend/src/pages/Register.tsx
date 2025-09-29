import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

const Register: React.FC = () => {
  const [formData, setFormData] = useState({
    firstName: '',
    lastName: '',
    username: '',
    email: '',
    password: '',
    confirmPassword: '',
    phoneNumber: '',
  });
  const [passwordErrors, setPasswordErrors] = useState<string[]>([]);
  const [showPasswordRequirements, setShowPasswordRequirements] = useState(false);
  const { register, isLoading, error, clearError } = useAuth();
  const navigate = useNavigate();

  // Password validation function
  const validatePassword = (password: string): string[] => {
    const errors: string[] = [];
    if (password.length < 6) {
      errors.push('Password must be at least 6 characters long');
    }
    if (password.length > 100) {
      errors.push('Password must not exceed 100 characters');
    }
    if (!/(?=.*[a-z])/.test(password)) {
      errors.push('Password must contain at least one lowercase letter');
    }
    if (!/(?=.*[A-Z])/.test(password)) {
      errors.push('Password must contain at least one uppercase letter');
    }
    if (!/(?=.*\d)/.test(password)) {
      errors.push('Password must contain at least one digit');
    }
    return errors;
  };

  // Phone number validation function
  const validatePhoneNumber = (phone: string): boolean => {
    if (!phone.trim()) return true; // Optional field
    return /^\+?[1-9]\d{1,14}$/.test(phone);
  };

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setFormData({
      ...formData,
      [name]: value,
    });

    // Validate password in real-time
    if (name === 'password') {
      const errors = validatePassword(value);
      setPasswordErrors(errors);
    }
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    clearError();

    // Validate password
    const passwordValidationErrors = validatePassword(formData.password);
    if (passwordValidationErrors.length > 0) {
      setPasswordErrors(passwordValidationErrors);
      return;
    }

    // Check password confirmation
    if (formData.password !== formData.confirmPassword) {
      alert('Passwords do not match');
      return;
    }

    // Check required fields
    if (!formData.firstName.trim() || !formData.lastName.trim() || !formData.username.trim() || !formData.email.trim()) {
      alert('Please fill in all required fields');
      return;
    }

    // Validate phone number if provided
    if (formData.phoneNumber.trim() && !validatePhoneNumber(formData.phoneNumber)) {
      alert('Please enter a valid phone number (e.g., +1234567890 or 9876543210)');
      return;
    }

    try {
      const registrationData: any = {
        firstName: formData.firstName.trim(),
        lastName: formData.lastName.trim(),
        username: formData.username.trim(),
        email: formData.email.trim(),
        password: formData.password,
      };
      
      // Only include phone number if it's provided and valid
      if (formData.phoneNumber.trim()) {
        registrationData.phoneNumber = formData.phoneNumber.trim();
      }
      
      await register(registrationData);
      navigate('/login', { 
        state: { message: 'Registration successful! Please login.' } 
      });
    } catch (err) {
      // Error is handled by AuthContext
      console.error('Registration failed:', err);
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-gray-50 py-12 px-4 sm:px-6 lg:px-8">
      <div className="max-w-md w-full space-y-8">
        <div>
          <h2 className="mt-6 text-center text-3xl font-extrabold text-gray-900">
            Create your account
          </h2>
          <p className="mt-2 text-center text-sm text-gray-600">
            Or{' '}
            <Link
              to="/login"
              className="font-medium text-indigo-600 hover:text-indigo-500"
            >
              sign in to existing account
            </Link>
          </p>
        </div>
        
        <form className="mt-8 space-y-6" onSubmit={handleSubmit}>
          <div className="space-y-4">
            <div>
              <label htmlFor="firstName" className="block text-sm font-medium text-gray-700">
                First Name
              </label>
              <input
                id="firstName"
                name="firstName"
                type="text"
                required
                className="mt-1 appearance-none relative block w-full px-3 py-2 border border-gray-300 placeholder-gray-500 text-gray-900 rounded-md focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm"
                placeholder="Enter your first name"
                value={formData.firstName}
                onChange={handleChange}
              />
            </div>
            
            <div>
              <label htmlFor="lastName" className="block text-sm font-medium text-gray-700">
                Last Name
              </label>
              <input
                id="lastName"
                name="lastName"
                type="text"
                required
                className="mt-1 appearance-none relative block w-full px-3 py-2 border border-gray-300 placeholder-gray-500 text-gray-900 rounded-md focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm"
                placeholder="Enter your last name"
                value={formData.lastName}
                onChange={handleChange}
              />
            </div>
            
            <div>
              <label htmlFor="username" className="block text-sm font-medium text-gray-700">
                Username
              </label>
              <input
                id="username"
                name="username"
                type="text"
                required
                className="mt-1 appearance-none relative block w-full px-3 py-2 border border-gray-300 placeholder-gray-500 text-gray-900 rounded-md focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm"
                placeholder="Choose a username"
                value={formData.username}
                onChange={handleChange}
              />
            </div>
            
            <div>
              <label htmlFor="email" className="block text-sm font-medium text-gray-700">
                Email Address
              </label>
              <input
                id="email"
                name="email"
                type="email"
                autoComplete="email"
                required
                className="mt-1 appearance-none relative block w-full px-3 py-2 border border-gray-300 placeholder-gray-500 text-gray-900 rounded-md focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm"
                placeholder="Enter your email"
                value={formData.email}
                onChange={handleChange}
              />
            </div>
            
            <div>
              <label htmlFor="phoneNumber" className="block text-sm font-medium text-gray-700">
                Phone Number (Optional)
              </label>
              <input
                id="phoneNumber"
                name="phoneNumber"
                type="tel"
                className="mt-1 appearance-none relative block w-full px-3 py-2 border border-gray-300 placeholder-gray-500 text-gray-900 rounded-md focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm"
                placeholder="e.g., +1234567890 or 9876543210"
                value={formData.phoneNumber}
                onChange={handleChange}
              />
              <p className="mt-1 text-xs text-gray-500">
                Format: Country code followed by number (e.g., +91987654321) or local number starting with non-zero digit
              </p>
            </div>
            
            <div>
              <label htmlFor="password" className="block text-sm font-medium text-gray-700">
                Password
              </label>
              <input
                id="password"
                name="password"
                type="password"
                autoComplete="new-password"
                required
                className={`mt-1 appearance-none relative block w-full px-3 py-2 border rounded-md focus:outline-none focus:ring-2 sm:text-sm ${
                  passwordErrors.length > 0 && formData.password
                    ? 'border-red-300 focus:ring-red-500 focus:border-red-500'
                    : 'border-gray-300 focus:ring-indigo-500 focus:border-indigo-500'
                } placeholder-gray-500 text-gray-900`}
                placeholder="Enter your password"
                value={formData.password}
                onChange={handleChange}
                onFocus={() => setShowPasswordRequirements(true)}
                onBlur={() => setShowPasswordRequirements(false)}
              />
              
              {/* Password Requirements */}
              {(showPasswordRequirements || passwordErrors.length > 0) && (
                <div className="mt-2 p-3 bg-gray-50 rounded-md">
                  <p className="text-xs font-medium text-gray-700 mb-1">Password Requirements:</p>
                  <ul className="text-xs space-y-1">
                    <li className={`${formData.password.length >= 6 ? 'text-green-600' : 'text-gray-500'}`}>
                      ✓ At least 6 characters long
                    </li>
                    <li className={`${/(?=.*[a-z])/.test(formData.password) ? 'text-green-600' : 'text-gray-500'}`}>
                      ✓ One lowercase letter
                    </li>
                    <li className={`${/(?=.*[A-Z])/.test(formData.password) ? 'text-green-600' : 'text-gray-500'}`}>
                      ✓ One uppercase letter
                    </li>
                    <li className={`${/(?=.*\d)/.test(formData.password) ? 'text-green-600' : 'text-gray-500'}`}>
                      ✓ One digit
                    </li>
                  </ul>
                </div>
              )}
              
              {/* Password Error Messages */}
              {passwordErrors.length > 0 && formData.password && (
                <div className="mt-1">
                  {passwordErrors.map((error, index) => (
                    <p key={index} className="text-sm text-red-600">{error}</p>
                  ))}
                </div>
              )}
            </div>
            
            <div>
              <label htmlFor="confirmPassword" className="block text-sm font-medium text-gray-700">
                Confirm Password
              </label>
              <input
                id="confirmPassword"
                name="confirmPassword"
                type="password"
                autoComplete="new-password"
                required
                className="mt-1 appearance-none relative block w-full px-3 py-2 border border-gray-300 placeholder-gray-500 text-gray-900 rounded-md focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm"
                placeholder="Confirm your password"
                value={formData.confirmPassword}
                onChange={handleChange}
              />
            </div>
          </div>

          {error && (
            <div className="text-red-600 text-sm text-center">{error}</div>
          )}

          <div>
            <button
              type="submit"
              disabled={isLoading}
              className="group relative w-full flex justify-center py-2 px-4 border border-transparent text-sm font-medium rounded-md text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 disabled:opacity-50"
            >
              {isLoading ? 'Creating account...' : 'Create account'}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default Register;