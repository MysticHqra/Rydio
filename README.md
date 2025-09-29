# 🚗 Rydio - Vehicle Rental Platform

A modern, full-stack vehicle rental platform built with **Spring Boot** (backend) and **React** (frontend). Rydio provides a secure, user-friendly experience for vehicle rentals with production-ready JWT authentication, role-based access control, and comprehensive admin features.

## ✨ Features

### User Features
- 🔐 **Secure JWT Authentication** - Production-ready authentication with encrypted passwords
- 🚙 **Vehicle Browsing** - Browse available vehicles with guest access and login prompts
- 📅 **Smart Booking System** - Intuitive booking process with user authentication
- 👤 **Profile Management** - Secure user account management
- 📱 **Responsive Design** - Mobile-first design with Tailwind CSS
- 🔒 **Role-Based Access** - User and Admin role segregation

### Admin Features
- 🏢 **Protected Admin Dashboard** - Admin-only access to management features
- 🚗 **Vehicle Management** - Complete CRUD operations for vehicle inventory
- 👥 **User Administration** - User management with role controls
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
The application comes with pre-configured test accounts:

```
Admin Account:
- Email: admin@rydio.com
- Password: admin123
- Role: ADMIN

User Account:
- Email: user@rydio.com
- Password: user123
- Role: USER
```

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
GET  /api/vehicles               - Get all vehicles (public access)
GET  /api/vehicles/{id}          - Get vehicle by ID
POST /api/vehicles/search        - Search vehicles with filters
POST /api/vehicles/admin         - Create vehicle (Admin only)
PUT  /api/vehicles/admin/{id}    - Update vehicle (Admin only)
DELETE /api/vehicles/admin/{id}  - Delete vehicle (Admin only)
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
    │   │   │   └── HealthController.java    # Health check
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
- Complete JWT authentication system
- Role-based access control
- Encrypted password storage
- Protected routes and components
- Admin-only features
- Guest user experience
- Responsive design
- Error handling and user feedback

🔄 **Future Enhancements:**
- Payment gateway integration
- Real-time booking notifications
- Advanced vehicle search filters
- Mobile application
- Email verification system
- Booking conflict resolution
- Analytics dashboard

---

**Built with ❤️ by the Rydio Development Team**