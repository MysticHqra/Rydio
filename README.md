# 🚗 Rydio - Vehicle Rental Platform

A modern, full-stack vehicle rental platform built with **Spring Boot** (backend) and **React** (frontend). Rydio provides a secure, user-friendly experience for vehicle rentals with production-ready JWT authentication, role-based access control, and comprehensive admin features.

## ✨ Features

### User Features
- 🔐 **Secure JWT Authentication** - Production-ready authentication with encrypted passwords
- 🚙 **Vehicle Browsing** - Browse available vehicles with guest access and login prompts
- 🎯 **Smart Recommendations** - AI-powered vehicle suggestions based on trip requirements using real vehicle data
- 📅 **Smart Booking System** - Intuitive booking process with user authentication
- 🚗 **Add Your Vehicle** - Peer-to-peer vehicle sharing platform where users can list their own vehicles
- 👤 **Comprehensive Profile Management** - Complete user profile with booking history and statistics
- 📊 **Booking History** - View past and current rentals with detailed information
- 📈 **Personal Statistics** - Track spending, favorite vehicles, and membership details
- 📱 **Responsive Design** - Mobile-first design with Tailwind CSS
- 🔒 **Role-Based Access** - User and Admin role segregation
- 🖼️ **Vehicle Images** - High-quality vehicle images for better visual experience

### Smart Recommendation Features
- 🧠 **Trip-Based Matching** - Vehicles suggested based on solo, family, business, leisure trips
- 👥 **Passenger-Aware** - Recommendations consider passenger count and seating requirements
- 🌤️ **Weather-Conscious** - Suggestions adapt to weather conditions (rainy, sunny, winter)
- 💰 **Budget-Friendly** - Recommendations stay within specified budget limits
- ⚡ **Fuel Preference** - Matches preferred fuel type (petrol, diesel, electric, hybrid)
- 🎒 **Smart Add-ons** - Contextual accessories like helmets, GPS, child seats
- 📊 **Personalized Learning** - Improves suggestions based on booking history
- 🔄 **Real-Time Data Integration** - Uses actual vehicle inventory from database
- 🚫 **Empty State Handling** - Gracefully handles scenarios when no vehicles are available

### Admin Features
- 🏢 **Comprehensive Admin Dashboard** - Complete administrative control panel
- 📊 **Real-time Analytics** - Platform statistics, revenue tracking, and performance metrics
- 👥 **Advanced User Management** - User administration with role controls and activity monitoring
- 🚗 **Vehicle Fleet Management** - Complete CRUD operations for vehicle inventory with status tracking
- � **Booking Administration** - Comprehensive booking management with status updates
- 📈 **Revenue Analytics** - Financial reporting and trend analysis
- 🔍 **Usage Statistics** - Platform utilization and popular vehicle type tracking
- 🎛️ **Multi-tab Interface** - Organized dashboard with Overview, Users, Vehicles, Bookings, and Analytics
- 📊 **API Testing Interface** - Admin-only API testing and monitoring
- 🔐 **Enhanced Security** - JWT-based admin authentication

### Security Features
- 🛡️ **JWT Token Authentication** - Secure tokens with 24-hour expiration
- 🔒 **BCrypt Password Encryption** - Industry-standard password hashing
- 🚪 **Protected Routes** - Role-based component access control
- 🔑 **Refresh Token Support** - Seamless token renewal
- 👮 **Admin-Only Features** - Restricted access to sensitive operations

## 🛠 Technology Stack

### Backend
- **Framework:** Spring Boot 3.2.0
- **Security:** Spring Security 6+ with JWT
- **Authentication:** JWT with BCrypt password encoding
- **Database:** H2 (in-memory) with JPA/Hibernate
- **Build Tool:** Maven
- **Java Version:** 17+

### Frontend
- **Framework:** React 18 with TypeScript
- **Styling:** Tailwind CSS 3+
- **State Management:** React Context API with Authentication Context
- **Routing:** React Router with Protected Routes
- **Build Tool:** Create React App
- **HTTP Client:** Axios with error handling

## 🚀 Quick Start

### Prerequisites
- Java 17 or higher
- Node.js 16 or higher
- Maven 3.6 or higher

### Backend Setup

1. **Clone the repository:**
   ```bash
   git clone https://github.com/MysticHqra/Rydio.git
   cd rydio
   ```

2. **Navigate to backend directory:**
   ```bash
   cd backend
   ```

3. **Install dependencies and start:**
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

4. **Backend will start on:** `http://localhost:8080`

### Frontend Setup

1. **Navigate to frontend directory:**
   ```bash
   cd frontend
   ```

2. **Install dependencies:**
   ```bash
   npm install
   ```

3. **Start development server:**
   ```bash
   npm start
   ```

4. **Frontend will start on:** `http://localhost:3000`

## 🔐 Authentication System

### JWT Implementation
- **Token Type:** Bearer tokens with HMAC-SHA256 signing
- **Expiration:** 24 hours for access tokens
- **Security:** BCrypt password hashing with configurable rounds
- **Refresh:** Automatic token refresh mechanism

### User Roles
- **USER:** Standard user with basic access to vehicle browsing and booking
- **ADMIN:** Full administrative access including user management and API testing

### Test Accounts
The application comes with pre-configured test accounts and sample vehicles:

**Test Users:**
```
Admin Account:
- Email: admin@rydio.com
- Password: admin123
- Role: ADMIN

Hara User:
- Email: hara@gmail.com
- Password: Hara@1234
- Role: USER

Test User:
- Email: test@example.com
- Password: password123
- Role: USER
```

**Sample Vehicles:**
- Honda Activa 6G (Scooter) - ₹600/day - Owned by Hara
- Maruti Swift (Car) - ₹1800/day - Owned by Test User
- Hero Splendor Plus (Bike) - ₹480/day - Owned by Admin
- Hyundai i20 (Premium Car) - ₹2200/day - Owned by Hara
- Ola S1 Pro (Electric Scooter) - ₹800/day - Owned by Test User

## 📚 API Documentation

### Authentication Endpoints
```
POST /api/auth/register          - User registration with encrypted passwords
POST /api/auth/login             - User login with JWT token generation
POST /api/auth/refresh-token     - Refresh JWT token
```

### User Endpoints
```
GET  /api/users/profile          - Get authenticated user profile
PUT  /api/users/profile          - Update user profile
GET  /api/users/admin/all        - Get all users (Admin only)
```

### Vehicle Endpoints
```
GET  /api/vehicles               - Get all available vehicles (public access)
GET  /api/vehicles/{id}          - Get vehicle by ID
POST /api/vehicles               - Create new vehicle (authenticated users)
PUT  /api/vehicles/{id}          - Update vehicle (vehicle owner only)
DELETE /api/vehicles/{id}        - Delete vehicle (vehicle owner only)
GET  /api/vehicles/my-vehicles   - Get current user's vehicles
GET  /api/vehicles/search        - Search vehicles with filters
```

### Booking Endpoints
```
POST /api/bookings               - Create booking (authenticated users)
GET  /api/bookings/my-bookings   - Get user's bookings
GET  /api/bookings/{id}          - Get booking by ID
PUT  /api/bookings/{id}          - Update booking status
POST /api/bookings/{id}/cancel   - Cancel booking
GET  /api/bookings/admin/all     - Get all bookings (Admin only)
```

### Smart Recommendations
```
POST /api/recommendations/smart        - Get smart vehicle recommendations
GET  /api/recommendations/quick        - Get quick recommendations
GET  /api/recommendations/add-ons      - Get recommended add-ons
GET  /api/recommendations/personalized-insight - Get personalized insights
```

### Health Check
```
GET  /api/health                 - Application health status
```

## 🏗 Project Structure

```
Rydio/
├── frontend/                    # React TypeScript frontend
│   ├── public/                  # Static assets
│   ├── src/
│   │   ├── components/          # Reusable UI components
│   │   │   ├── Header.tsx       # Navigation with role-based visibility
│   │   │   ├── VehicleCard.tsx  # Vehicle display component
│   │   │   └── ProtectedRoute.tsx # Role-based route protection
│   │   ├── pages/              # Application pages
│   │   │   ├── Home.tsx        # Landing page
│   │   │   ├── Login.tsx       # Authentication page
│   │   │   ├── Register.tsx    # User registration
│   │   │   ├── VehicleList.tsx # Vehicle browsing with guest prompts
│   │   │   ├── SmartRecommendations.tsx # AI-powered recommendations
│   │   │   ├── Profile.tsx     # User profile management with booking history
│   │   │   ├── AdminDashboard.tsx # Comprehensive admin control panel
│   │   │   ├── Booking.tsx     # Booking management
│   │   │   └── IntegrationTest.tsx # Admin-only API testing
│   │   ├── context/            # React context providers
│   │   │   ├── AuthContext.tsx # Authentication state management
│   │   │   └── AppContext.tsx  # Global application state
│   │   ├── services/           # API service functions
│   │   │   └── api.ts          # HTTP client with error handling
│   │   ├── types/              # TypeScript type definitions
│   │   └── App.tsx             # Main application component
│   ├── package.json
│   └── tailwind.config.js
│
└── backend/                     # Spring Boot backend
    ├── src/
    │   ├── main/java/com/rydio/
    │   │   ├── common/          # Shared utilities and DTOs
    │   │   │   ├── dto/         # Data transfer objects
    │   │   │   ├── entity/      # Base entity classes
    │   │   │   └── exception/   # Custom exception handling
    │   │   ├── config/          # Spring configuration
    │   │   │   ├── SecurityConfig.java      # Security configuration
    │   │   │   ├── JwtAuthenticationFilter.java # JWT filter
    │   │   │   ├── JwtUtil.java             # JWT utilities
    │   │   │   └── DataInitializer.java     # Test data initialization
    │   │   ├── controller/      # REST API controllers
    │   │   │   ├── AuthController.java      # Authentication endpoints
    │   │   │   ├── UserController.java      # User management
    │   │   │   ├── BookingController.java   # Booking operations
    │   │   │   ├── RecommendationController.java # Smart recommendations
    │   │   │   └── HealthController.java    # Health check
    │   │   ├── recommendation/  # Smart recommendation system
    │   │   │   ├── service/     # Recommendation algorithms
    │   │   │   ├── dto/         # Recommendation data objects
    │   │   │   └── controller/  # Recommendation endpoints
    │   │   ├── user/            # User domain
    │   │   │   ├── entity/      # User entity
    │   │   │   ├── repository/  # Data access layer
    │   │   │   ├── service/     # Business logic
    │   │   │   └── dto/         # User DTOs
    │   │   └── RydioBackendApplication.java # Main application class
    │   ├── main/resources/
    │   │   └── application.properties       # Application configuration
    │   └── test/                # Unit and integration tests
    └── pom.xml                 # Maven configuration
```

## 🧪 Testing

### Backend Tests
```bash
cd backend
mvn test
```

### Frontend Tests
```bash
cd frontend
npm test
```

### Manual Testing
1. **Start both backend and frontend**
2. **Test authentication flow:**
   - Register new user
   - Login with test accounts
   - Verify JWT tokens in browser dev tools
3. **Test role-based access:**
   - Login as admin to access Admin Dashboard and API Test page
   - Login as user to verify restricted access
4. **Test guest experience:**
   - Browse vehicles without authentication
   - Verify login prompts for booking actions

## 📱 Application Features

### Profile Management
The Profile page offers comprehensive user account management:

#### Personal Information Management
- **Editable Profile Fields:** First name, last name, phone number, date of birth
- **Driver License Management:** License number and expiry date tracking
- **Address Information:** Complete address management
- **Account Security:** Email verification and account status display

#### Booking History & Analytics
- **Complete Booking History:** View all past, current, and upcoming rentals
- **Booking Details:** Vehicle information, dates, amounts, and status tracking
- **Personal Statistics:** 
  - Total bookings and completed trips
  - Total amount spent on rentals
  - Favorite vehicle type analysis
  - Membership duration tracking

#### Account Status Overview
- **Verification Status:** Email verification badge
- **Account Activity:** Active/inactive status indicator
- **Role Display:** User role with appropriate styling

### Admin Dashboard
A comprehensive administrative control panel with multiple management sections:

#### Overview Tab
- **Platform Statistics:** Total users, vehicles, active bookings, and revenue
- **Growth Metrics:** New users and bookings for current month
- **Recent Activity:** Latest bookings and vehicle status updates
- **Quick Status:** At-a-glance platform health

#### User Management Tab
- **Complete User List:** All registered users with detailed information
- **User Analytics:** Total bookings and spending per user
- **Account Controls:** Activate/deactivate user accounts
- **Role Management:** User role display and management
- **Registration Tracking:** User creation dates and activity

#### Vehicle Management Tab
- **Fleet Overview:** Complete vehicle inventory with status
- **Availability Control:** Toggle vehicle availability status
- **Performance Metrics:** Booking counts and utilization rates
- **Maintenance Tracking:** Last service date monitoring
- **Revenue Analysis:** Price per day and total bookings

#### Booking Administration Tab
- **Booking Overview:** All platform bookings with full details
- **Status Management:** Update booking status (Pending, Active, Completed, Cancelled)
- **User-Vehicle Mapping:** Complete booking relationship tracking
- **Revenue Tracking:** Individual booking amounts and dates
- **Customer Information:** User details for each booking

#### Analytics Tab
- **Platform Metrics:** Vehicle utilization, average ratings, growth percentages
- **Popular Vehicle Analysis:** Usage statistics by vehicle type with visual progress bars
- **Revenue Trends:** Monthly revenue tracking and growth analysis
- **Performance Indicators:** Key business metrics and trends

### Smart Recommendations System
AI-powered vehicle suggestions with multiple recommendation types:

#### Trip-Based Recommendations
- **Solo Trips:** Scooters and bikes for individual travel
- **Family Trips:** Cars and SUVs with safety features
- **Business Trips:** Professional vehicles with comfort features
- **Adventure Trips:** Robust vehicles with special equipment

#### Smart Add-On Suggestions
- **Family-Specific:** Child safety seats, entertainment systems
- **Business-Oriented:** Wi-Fi hotspots, phone chargers, premium insurance
- **Adventure-Ready:** GPS navigation, emergency kits, all-terrain equipment

## 🧪 Testing

### Backend Tests
```bash
cd backend
mvn test
```

### Frontend Tests
```bash
cd frontend
npm test
```

### Manual Testing
1. **Start both backend and frontend**
2. **Test authentication flow:**
   - Register new user
   - Login with test accounts
   - Verify JWT tokens in browser dev tools
3. **Test role-based access:**
   - Login as admin to access API Test page
   - Login as user to verify restricted access
4. **Test guest experience:**
   - Browse vehicles without authentication
   - Verify login prompts for booking actions

## 🔧 Configuration

### Backend Configuration (`application.properties`)
```properties
# Server Configuration
server.port=8080

# Database Configuration (H2 In-Memory)
spring.datasource.url=jdbc:h2:mem:rydio_db
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=password
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console

# JPA Configuration
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

# JWT Configuration
jwt.secret=mySecretKey
jwt.expiration=86400000

# CORS Configuration
cors.allowed-origins=http://localhost:3000
```

### Frontend Configuration
The frontend uses environment variables for configuration. Create a `.env` file:
```env
REACT_APP_API_BASE_URL=http://localhost:8080/api
```

## 🚀 Deployment

### Production Considerations

#### Backend Deployment
1. **Build JAR file:**
   ```bash
   mvn clean package -DskipTests
   ```

2. **Configure production database:**
   ```properties
   # Replace H2 with production database
   spring.datasource.url=jdbc:mysql://localhost:3306/rydio_production
   spring.datasource.username=${DB_USERNAME}
   spring.datasource.password=${DB_PASSWORD}
   spring.jpa.hibernate.ddl-auto=validate
   ```

3. **Set production JWT secret:**
   ```properties
   jwt.secret=${JWT_SECRET}
   ```

4. **Run application:**
   ```bash
   java -jar target/rydio-backend-1.0.0.jar
   ```

#### Frontend Deployment
1. **Build production bundle:**
   ```bash
   npm run build
   ```

2. **Configure production API URL:**
   ```env
   REACT_APP_API_BASE_URL=https://your-api-domain.com/api
   ```

3. **Deploy static files** to CDN, nginx, or hosting service

## 🔒 Security Features

### Authentication Security
- **Password Encryption:** BCrypt with configurable salt rounds
- **JWT Security:** HMAC-SHA256 signing with secret key
- **Token Expiration:** 24-hour access tokens with refresh capability
- **CORS Protection:** Configured for frontend domain only

### Access Control
- **Protected Routes:** Frontend route protection based on user roles
- **API Security:** Backend endpoint protection with JWT validation
- **Admin Features:** Role-based access to sensitive operations
- **Guest Experience:** Graceful handling of unauthenticated users

### Security Best Practices
- **No sensitive data in JWT payload**
- **Secure password requirements** (configurable)
- **Input validation** on all endpoints
- **Error handling** without information disclosure

## 🤝 Contributing

1. **Fork the repository**
2. **Create a feature branch:** `git checkout -b feature/your-feature-name`
3. **Commit your changes:** `git commit -m 'Add some feature'`
4. **Push to the branch:** `git push origin feature/your-feature-name`
5. **Create a Pull Request**

### Development Guidelines
- Follow existing code style and patterns
- Add tests for new features
- Update documentation for API changes
- Use TypeScript strict mode for frontend
- Follow Spring Boot best practices for backend

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 👥 Development Team

### Backend Development
- **Spring Boot** REST API development
- **JWT Authentication** implementation with Spring Security
- **Database Design** with JPA/Hibernate
- **Security Configuration** and access control
- **Unit Testing** with JUnit and Mockito

### Frontend Development
- **React TypeScript** application development
- **Tailwind CSS** responsive design implementation
- **Context API** state management
- **Protected Routes** and role-based access
- **User Experience** optimization

## 🔗 Quick Links

- **Frontend Application:** http://localhost:3000
- **Backend API:** http://localhost:8080/api
- **H2 Database Console:** http://localhost:8080/h2-console
- **API Health Check:** http://localhost:8080/api/health

## 📞 Support

For support, questions, or feature requests:
1. **Create an issue** in the GitHub repository
2. **Check existing documentation** in this README
3. **Review the codebase** for implementation examples
4. **Contact the development team** for urgent matters

---

## 🎯 Current Status

✅ **Production Ready Features:**
- Complete JWT authentication system with BCrypt password encryption
- Role-based access control with User and Admin roles
- Peer-to-peer vehicle rental platform where users can list their own vehicles
- Protected routes and components with graceful guest experience
- Comprehensive admin dashboard with multi-tab interface
- Smart vehicle recommendations using real database data
- AI-powered matching algorithms for trip-based suggestions
- Personalized recommendations based on booking history and user preferences
- Complete vehicle management with CRUD operations
- User profile management with booking history and statistics
- Vehicle images with high-quality Unsplash integration
- Responsive design optimized for mobile and desktop
- Sample test data with 5 diverse vehicles and 3 test users
- Real-time data integration between frontend and backend
- Error handling and user feedback throughout the application

🔄 **Future Enhancements:**
- Payment gateway integration for secure transactions
- Real-time booking notifications and status updates
- Advanced vehicle search filters and sorting options
- Mobile application for iOS and Android
- Email verification system and password reset
- Booking conflict resolution and availability checking
- Advanced analytics dashboard with charts and graphs
- Integration with external mapping services for location-based features
- Review and rating system for vehicles and users
- Insurance and damage reporting system

---

**Built with ❤️ by the Rydio Development Team**