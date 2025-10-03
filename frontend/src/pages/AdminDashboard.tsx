import React, { useState, useEffect } from 'react';
import { useAuth } from '../context/AuthContext';
import { User } from '../types/api';

interface DashboardStats {
  totalUsers: number;
  totalVehicles: number;
  activeBookings: number;
  totalRevenue: number;
  newUsersThisMonth: number;
  bookingsThisMonth: number;
}

interface BookingData {
  id: number;
  userId: number;
  userName: string;
  userEmail: string;
  vehicleId: number;
  vehicleBrand: string;
  vehicleModel: string;
  startDate: string;
  endDate: string;
  totalAmount: number;
  status: 'ACTIVE' | 'COMPLETED' | 'CANCELLED' | 'PENDING';
  createdAt: string;
}

interface VehicleData {
  id: number;
  brand: string;
  model: string;
  vehicleType: string;
  pricePerDay: number;
  isAvailable: boolean;
  totalBookings: number;
  lastServiceDate?: string;
}

interface UserData {
  id: number;
  username: string;
  email: string;
  firstName: string;
  lastName: string;
  role: 'USER' | 'ADMIN';
  isActive: boolean;
  emailVerified: boolean;
  createdAt: string;
  totalBookings: number;
  totalSpent: number;
}

const AdminDashboard: React.FC = () => {
  const { user } = useAuth();
  const [activeTab, setActiveTab] = useState<'overview' | 'users' | 'vehicles' | 'bookings' | 'analytics'>('overview');
  const [dashboardStats, setDashboardStats] = useState<DashboardStats | null>(null);
  const [users, setUsers] = useState<UserData[]>([]);
  const [vehicles, setVehicles] = useState<VehicleData[]>([]);
  const [bookings, setBookings] = useState<BookingData[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    if (user && user.role === 'ADMIN') {
      fetchDashboardData();
    }
  }, [user]);

  // Check admin access
  if (!user || user.role !== 'ADMIN') {
    return (
      <div className="min-h-screen flex items-center justify-center">
        <div className="text-center">
          <h2 className="text-2xl font-bold text-red-600">Access Denied</h2>
          <p className="text-gray-600 mt-2">You don't have permission to access the admin dashboard.</p>
        </div>
      </div>
    );
  }

  const fetchDashboardData = async () => {
    setLoading(true);
    try {
      // Mock data - replace with actual API calls
      await Promise.all([
        fetchDashboardStats(),
        fetchUsers(),
        fetchVehicles(),
        fetchBookings()
      ]);
    } catch (err) {
      setError('Failed to fetch dashboard data');
    } finally {
      setLoading(false);
    }
  };

  const fetchDashboardStats = async () => {
    // Mock data
    const mockStats: DashboardStats = {
      totalUsers: 1247,
      totalVehicles: 85,
      activeBookings: 23,
      totalRevenue: 2456780,
      newUsersThisMonth: 156,
      bookingsThisMonth: 342
    };
    setDashboardStats(mockStats);
  };

  const fetchUsers = async () => {
    // Mock data
    const mockUsers: UserData[] = [
      {
        id: 1,
        username: 'hara',
        email: 'hara@gmail.com',
        firstName: 'Hara',
        lastName: 'User',
        role: 'USER',
        isActive: true,
        emailVerified: true,
        createdAt: '2025-08-15T10:30:00Z',
        totalBookings: 5,
        totalSpent: 12500
      },
      {
        id: 2,
        username: 'testuser',
        email: 'test@example.com',
        firstName: 'Test',
        lastName: 'User',
        role: 'USER',
        isActive: true,
        emailVerified: false,
        createdAt: '2025-09-01T14:20:00Z',
        totalBookings: 2,
        totalSpent: 3600
      },
      {
        id: 3,
        username: 'admin',
        email: 'admin@rydio.com',
        firstName: 'Admin',
        lastName: 'User',
        role: 'ADMIN',
        isActive: true,
        emailVerified: true,
        createdAt: '2025-01-01T00:00:00Z',
        totalBookings: 0,
        totalSpent: 0
      }
    ];
    setUsers(mockUsers);
  };

  const fetchVehicles = async () => {
    // Mock data
    const mockVehicles: VehicleData[] = [
      {
        id: 1,
        brand: 'Honda',
        model: 'Activa 6G',
        vehicleType: 'SCOOTER',
        pricePerDay: 300,
        isAvailable: true,
        totalBookings: 45,
        lastServiceDate: '2025-09-15'
      },
      {
        id: 2,
        brand: 'Yamaha',
        model: 'FZ-S',
        vehicleType: 'BIKE',
        pricePerDay: 500,
        isAvailable: false,
        totalBookings: 32,
        lastServiceDate: '2025-09-20'
      },
      {
        id: 3,
        brand: 'Maruti',
        model: 'Swift',
        vehicleType: 'CAR',
        pricePerDay: 1200,
        isAvailable: true,
        totalBookings: 28,
        lastServiceDate: '2025-09-10'
      },
      {
        id: 4,
        brand: 'Hyundai',
        model: 'Creta',
        vehicleType: 'SUV',
        pricePerDay: 2000,
        isAvailable: true,
        totalBookings: 18,
        lastServiceDate: '2025-09-25'
      }
    ];
    setVehicles(mockVehicles);
  };

  const fetchBookings = async () => {
    // Mock data
    const mockBookings: BookingData[] = [
      {
        id: 1,
        userId: 1,
        userName: 'Hara User',
        userEmail: 'hara@gmail.com',
        vehicleId: 1,
        vehicleBrand: 'Honda',
        vehicleModel: 'Activa 6G',
        startDate: '2025-10-01',
        endDate: '2025-10-03',
        totalAmount: 900,
        status: 'ACTIVE',
        createdAt: '2025-09-28T10:30:00Z'
      },
      {
        id: 2,
        userId: 2,
        userName: 'Test User',
        userEmail: 'test@example.com',
        vehicleId: 3,
        vehicleBrand: 'Maruti',
        vehicleModel: 'Swift',
        startDate: '2025-10-05',
        endDate: '2025-10-07',
        totalAmount: 2400,
        status: 'PENDING',
        createdAt: '2025-10-01T14:20:00Z'
      },
      {
        id: 3,
        userId: 1,
        userName: 'Hara User',
        userEmail: 'hara@gmail.com',
        vehicleId: 4,
        vehicleBrand: 'Hyundai',
        vehicleModel: 'Creta',
        startDate: '2025-09-20',
        endDate: '2025-09-22',
        totalAmount: 4000,
        status: 'COMPLETED',
        createdAt: '2025-09-18T16:45:00Z'
      }
    ];
    setBookings(mockBookings);
  };

  const toggleUserStatus = async (userId: number) => {
    // Mock toggle - replace with actual API call
    setUsers(prev => prev.map(user => 
      user.id === userId ? { ...user, isActive: !user.isActive } : user
    ));
  };

  const toggleVehicleAvailability = async (vehicleId: number) => {
    // Mock toggle - replace with actual API call
    setVehicles(prev => prev.map(vehicle => 
      vehicle.id === vehicleId ? { ...vehicle, isAvailable: !vehicle.isAvailable } : vehicle
    ));
  };

  const updateBookingStatus = async (bookingId: number, newStatus: string) => {
    // Mock update - replace with actual API call
    setBookings(prev => prev.map(booking => 
      booking.id === bookingId ? { ...booking, status: newStatus as any } : booking
    ));
  };

  const formatCurrency = (amount: number) => {
    return new Intl.NumberFormat('en-IN', {
      style: 'currency',
      currency: 'INR'
    }).format(amount);
  };

  const formatDate = (dateString: string) => {
    return new Date(dateString).toLocaleDateString('en-US', {
      year: 'numeric',
      month: 'short',
      day: 'numeric'
    });
  };

  const getStatusBadge = (status: string) => {
    const badges = {
      ACTIVE: 'bg-green-100 text-green-800',
      COMPLETED: 'bg-blue-100 text-blue-800',
      CANCELLED: 'bg-red-100 text-red-800',
      PENDING: 'bg-yellow-100 text-yellow-800'
    };
    return badges[status as keyof typeof badges] || 'bg-gray-100 text-gray-800';
  };

  const StatCard: React.FC<{ title: string; value: string | number; subtitle?: string; color?: string }> = ({
    title, value, subtitle, color = 'indigo'
  }) => (
    <div className="bg-white rounded-lg shadow p-6">
      <div className="flex items-center">
        <div className="flex-1">
          <p className="text-sm font-medium text-gray-600">{title}</p>
          <p className={`text-2xl font-semibold text-${color}-600`}>{value}</p>
          {subtitle && <p className="text-sm text-gray-500">{subtitle}</p>}
        </div>
      </div>
    </div>
  );

  const TabButton: React.FC<{ id: string; label: string; isActive: boolean }> = ({ id, label, isActive }) => (
    <button
      onClick={() => setActiveTab(id as any)}
      className={`px-4 py-2 text-sm font-medium rounded-md ${
        isActive
          ? 'bg-indigo-100 text-indigo-700'
          : 'text-gray-500 hover:text-gray-700 hover:bg-gray-100'
      }`}
    >
      {label}
    </button>
  );

  return (
    <div className="min-h-screen bg-gray-50">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        {/* Header */}
        <div className="mb-8">
          <h1 className="text-3xl font-bold text-gray-900">Admin Dashboard</h1>
          <p className="text-gray-600 mt-2">Manage your Rydio platform</p>
        </div>

        {error && (
          <div className="mb-6 bg-red-50 border border-red-200 text-red-700 px-4 py-3 rounded-md">
            {error}
          </div>
        )}

        {/* Navigation Tabs */}
        <div className="mb-8">
          <nav className="flex space-x-2">
            <TabButton id="overview" label="Overview" isActive={activeTab === 'overview'} />
            <TabButton id="users" label="Users" isActive={activeTab === 'users'} />
            <TabButton id="vehicles" label="Vehicles" isActive={activeTab === 'vehicles'} />
            <TabButton id="bookings" label="Bookings" isActive={activeTab === 'bookings'} />
            <TabButton id="analytics" label="Analytics" isActive={activeTab === 'analytics'} />
          </nav>
        </div>

        {/* Overview Tab */}
        {activeTab === 'overview' && dashboardStats && (
          <div className="space-y-8">
            {/* Stats Grid */}
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
              <StatCard title="Total Users" value={dashboardStats.totalUsers} subtitle="Registered users" />
              <StatCard title="Total Vehicles" value={dashboardStats.totalVehicles} subtitle="In fleet" color="green" />
              <StatCard title="Active Bookings" value={dashboardStats.activeBookings} subtitle="Currently rented" color="blue" />
              <StatCard title="Total Revenue" value={formatCurrency(dashboardStats.totalRevenue)} subtitle="All time" color="purple" />
              <StatCard title="New Users" value={dashboardStats.newUsersThisMonth} subtitle="This month" color="yellow" />
              <StatCard title="Bookings" value={dashboardStats.bookingsThisMonth} subtitle="This month" color="red" />
            </div>

            {/* Recent Activity */}
            <div className="grid grid-cols-1 lg:grid-cols-2 gap-8">
              {/* Recent Bookings */}
              <div className="bg-white rounded-lg shadow">
                <div className="px-6 py-4 border-b border-gray-200">
                  <h3 className="text-lg font-semibold text-gray-900">Recent Bookings</h3>
                </div>
                <div className="px-6 py-4">
                  <div className="space-y-4">
                    {bookings.slice(0, 5).map((booking) => (
                      <div key={booking.id} className="flex items-center justify-between">
                        <div>
                          <p className="text-sm font-medium text-gray-900">{booking.userName}</p>
                          <p className="text-sm text-gray-500">{booking.vehicleBrand} {booking.vehicleModel}</p>
                        </div>
                        <span className={`inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium ${getStatusBadge(booking.status)}`}>
                          {booking.status}
                        </span>
                      </div>
                    ))}
                  </div>
                </div>
              </div>

              {/* Vehicle Status */}
              <div className="bg-white rounded-lg shadow">
                <div className="px-6 py-4 border-b border-gray-200">
                  <h3 className="text-lg font-semibold text-gray-900">Vehicle Status</h3>
                </div>
                <div className="px-6 py-4">
                  <div className="space-y-4">
                    {vehicles.slice(0, 5).map((vehicle) => (
                      <div key={vehicle.id} className="flex items-center justify-between">
                        <div>
                          <p className="text-sm font-medium text-gray-900">{vehicle.brand} {vehicle.model}</p>
                          <p className="text-sm text-gray-500">{formatCurrency(vehicle.pricePerDay)}/day</p>
                        </div>
                        <span className={`inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium ${
                          vehicle.isAvailable ? 'bg-green-100 text-green-800' : 'bg-red-100 text-red-800'
                        }`}>
                          {vehicle.isAvailable ? 'Available' : 'Rented'}
                        </span>
                      </div>
                    ))}
                  </div>
                </div>
              </div>
            </div>
          </div>
        )}

        {/* Users Tab */}
        {activeTab === 'users' && (
          <div className="bg-white rounded-lg shadow">
            <div className="px-6 py-4 border-b border-gray-200">
              <h3 className="text-lg font-semibold text-gray-900">User Management</h3>
            </div>
            <div className="overflow-x-auto">
              <table className="min-w-full divide-y divide-gray-200">
                <thead className="bg-gray-50">
                  <tr>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">User</th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Role</th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Status</th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Bookings</th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Total Spent</th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Actions</th>
                  </tr>
                </thead>
                <tbody className="divide-y divide-gray-200">
                  {users.map((userData) => (
                    <tr key={userData.id} className="hover:bg-gray-50">
                      <td className="px-6 py-4 whitespace-nowrap">
                        <div>
                          <div className="text-sm font-medium text-gray-900">
                            {userData.firstName} {userData.lastName}
                          </div>
                          <div className="text-sm text-gray-500">{userData.email}</div>
                        </div>
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap">
                        <span className={`inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium ${
                          userData.role === 'ADMIN' ? 'bg-purple-100 text-purple-800' : 'bg-blue-100 text-blue-800'
                        }`}>
                          {userData.role}
                        </span>
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap">
                        <span className={`inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium ${
                          userData.isActive ? 'bg-green-100 text-green-800' : 'bg-red-100 text-red-800'
                        }`}>
                          {userData.isActive ? 'Active' : 'Inactive'}
                        </span>
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                        {userData.totalBookings}
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                        {formatCurrency(userData.totalSpent)}
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                        <button
                          onClick={() => toggleUserStatus(userData.id)}
                          className={`${
                            userData.isActive ? 'text-red-600 hover:text-red-500' : 'text-green-600 hover:text-green-500'
                          } font-medium`}
                        >
                          {userData.isActive ? 'Deactivate' : 'Activate'}
                        </button>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          </div>
        )}

        {/* Vehicles Tab */}
        {activeTab === 'vehicles' && (
          <div className="bg-white rounded-lg shadow">
            <div className="px-6 py-4 border-b border-gray-200">
              <h3 className="text-lg font-semibold text-gray-900">Vehicle Management</h3>
            </div>
            <div className="overflow-x-auto">
              <table className="min-w-full divide-y divide-gray-200">
                <thead className="bg-gray-50">
                  <tr>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Vehicle</th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Type</th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Price/Day</th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Status</th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Bookings</th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Last Service</th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Actions</th>
                  </tr>
                </thead>
                <tbody className="divide-y divide-gray-200">
                  {vehicles.map((vehicle) => (
                    <tr key={vehicle.id} className="hover:bg-gray-50">
                      <td className="px-6 py-4 whitespace-nowrap">
                        <div className="text-sm font-medium text-gray-900">
                          {vehicle.brand} {vehicle.model}
                        </div>
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap">
                        <span className="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium bg-gray-100 text-gray-800">
                          {vehicle.vehicleType}
                        </span>
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                        {formatCurrency(vehicle.pricePerDay)}
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap">
                        <span className={`inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium ${
                          vehicle.isAvailable ? 'bg-green-100 text-green-800' : 'bg-red-100 text-red-800'
                        }`}>
                          {vehicle.isAvailable ? 'Available' : 'Rented'}
                        </span>
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                        {vehicle.totalBookings}
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                        {vehicle.lastServiceDate ? formatDate(vehicle.lastServiceDate) : 'N/A'}
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                        <button
                          onClick={() => toggleVehicleAvailability(vehicle.id)}
                          className="text-indigo-600 hover:text-indigo-500 font-medium"
                        >
                          Toggle Status
                        </button>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          </div>
        )}

        {/* Bookings Tab */}
        {activeTab === 'bookings' && (
          <div className="bg-white rounded-lg shadow">
            <div className="px-6 py-4 border-b border-gray-200">
              <h3 className="text-lg font-semibold text-gray-900">Booking Management</h3>
            </div>
            <div className="overflow-x-auto">
              <table className="min-w-full divide-y divide-gray-200">
                <thead className="bg-gray-50">
                  <tr>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">User</th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Vehicle</th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Duration</th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Amount</th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Status</th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Actions</th>
                  </tr>
                </thead>
                <tbody className="divide-y divide-gray-200">
                  {bookings.map((booking) => (
                    <tr key={booking.id} className="hover:bg-gray-50">
                      <td className="px-6 py-4 whitespace-nowrap">
                        <div>
                          <div className="text-sm font-medium text-gray-900">{booking.userName}</div>
                          <div className="text-sm text-gray-500">{booking.userEmail}</div>
                        </div>
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap">
                        <div className="text-sm font-medium text-gray-900">
                          {booking.vehicleBrand} {booking.vehicleModel}
                        </div>
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap">
                        <div className="text-sm text-gray-900">
                          {formatDate(booking.startDate)} - {formatDate(booking.endDate)}
                        </div>
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                        {formatCurrency(booking.totalAmount)}
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap">
                        <span className={`inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium ${getStatusBadge(booking.status)}`}>
                          {booking.status}
                        </span>
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                        <select
                          value={booking.status}
                          onChange={(e) => updateBookingStatus(booking.id, e.target.value)}
                          className="text-sm border border-gray-300 rounded-md px-2 py-1"
                        >
                          <option value="PENDING">Pending</option>
                          <option value="ACTIVE">Active</option>
                          <option value="COMPLETED">Completed</option>
                          <option value="CANCELLED">Cancelled</option>
                        </select>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          </div>
        )}

        {/* Analytics Tab */}
        {activeTab === 'analytics' && (
          <div className="space-y-8">
            <div className="bg-white rounded-lg shadow p-6">
              <h3 className="text-lg font-semibold text-gray-900 mb-4">Platform Analytics</h3>
              <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
                <div className="text-center">
                  <div className="text-3xl font-bold text-indigo-600">92%</div>
                  <div className="text-sm text-gray-600">Vehicle Utilization</div>
                </div>
                <div className="text-center">
                  <div className="text-3xl font-bold text-green-600">4.8</div>
                  <div className="text-sm text-gray-600">Average Rating</div>
                </div>
                <div className="text-center">
                  <div className="text-3xl font-bold text-blue-600">156</div>
                  <div className="text-sm text-gray-600">New Users This Month</div>
                </div>
                <div className="text-center">
                  <div className="text-3xl font-bold text-purple-600">28%</div>
                  <div className="text-sm text-gray-600">Revenue Growth</div>
                </div>
              </div>
            </div>

            <div className="grid grid-cols-1 lg:grid-cols-2 gap-8">
              <div className="bg-white rounded-lg shadow p-6">
                <h4 className="text-lg font-semibold text-gray-900 mb-4">Popular Vehicle Types</h4>
                <div className="space-y-4">
                  <div className="flex justify-between items-center">
                    <span className="text-sm text-gray-600">Scooters</span>
                    <div className="flex items-center space-x-2">
                      <div className="w-32 bg-gray-200 rounded-full h-2">
                        <div className="bg-indigo-600 h-2 rounded-full" style={{ width: '65%' }}></div>
                      </div>
                      <span className="text-sm font-medium">65%</span>
                    </div>
                  </div>
                  <div className="flex justify-between items-center">
                    <span className="text-sm text-gray-600">Cars</span>
                    <div className="flex items-center space-x-2">
                      <div className="w-32 bg-gray-200 rounded-full h-2">
                        <div className="bg-green-600 h-2 rounded-full" style={{ width: '45%' }}></div>
                      </div>
                      <span className="text-sm font-medium">45%</span>
                    </div>
                  </div>
                  <div className="flex justify-between items-center">
                    <span className="text-sm text-gray-600">SUVs</span>
                    <div className="flex items-center space-x-2">
                      <div className="w-32 bg-gray-200 rounded-full h-2">
                        <div className="bg-blue-600 h-2 rounded-full" style={{ width: '25%' }}></div>
                      </div>
                      <span className="text-sm font-medium">25%</span>
                    </div>
                  </div>
                  <div className="flex justify-between items-center">
                    <span className="text-sm text-gray-600">Bikes</span>
                    <div className="flex items-center space-x-2">
                      <div className="w-32 bg-gray-200 rounded-full h-2">
                        <div className="bg-purple-600 h-2 rounded-full" style={{ width: '35%' }}></div>
                      </div>
                      <span className="text-sm font-medium">35%</span>
                    </div>
                  </div>
                </div>
              </div>

              <div className="bg-white rounded-lg shadow p-6">
                <h4 className="text-lg font-semibold text-gray-900 mb-4">Monthly Revenue Trend</h4>
                <div className="space-y-4">
                  <div className="flex justify-between items-center">
                    <span className="text-sm text-gray-600">September 2025</span>
                    <span className="text-sm font-medium">{formatCurrency(345000)}</span>
                  </div>
                  <div className="flex justify-between items-center">
                    <span className="text-sm text-gray-600">August 2025</span>
                    <span className="text-sm font-medium">{formatCurrency(298000)}</span>
                  </div>
                  <div className="flex justify-between items-center">
                    <span className="text-sm text-gray-600">July 2025</span>
                    <span className="text-sm font-medium">{formatCurrency(276000)}</span>
                  </div>
                  <div className="flex justify-between items-center">
                    <span className="text-sm text-gray-600">June 2025</span>
                    <span className="text-sm font-medium">{formatCurrency(251000)}</span>
                  </div>
                </div>
              </div>
            </div>
          </div>
        )}
      </div>
    </div>
  );
};

export default AdminDashboard;