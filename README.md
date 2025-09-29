# Rydio - Vehicle Rental Platform

A comprehensive vehicle rental platform built with React frontend and Spring Boot backend, featuring JWT authentication, role-based access control, and complete booking management system.

## 🚀 Features

### Frontend (React + TypeScript)
- **Modern React 19** with TypeScript
- **Tailwind CSS** for styling with Headless UI components
- **React Router** for navigation
- **Axios** for API communication
- **Context API** for state management
- **Responsive Design** for mobile and desktop

### Backend (Spring Boot)
- **Spring Boot 3.2** with Java 17
- **Spring Security** with JWT authentication
- **Spring Data JPA** with MySQL/H2 database
- **Maven** build system
- **Bean Validation** for input validation
- **JUnit & Mockito** for testing
- **File Upload/Download** functionality
- **RESTful API** design

### Key Modules

#### 🔐 Authentication & Authorization
- User registration and login
- JWT token-based authentication
- Role-based access control (Admin/User)
- Password hashing with BCrypt
- Token refresh mechanism

#### 👤 User Management
- User profile management
- Driver license validation
- Email verification
- Profile updates

#### 🚗 Vehicle Management
- Complete CRUD operations for vehicles
- Vehicle search and filtering
- Vehicle availability tracking
- Support for cars, bikes, scooters, bicycles
- Image upload for vehicles
- Status management (Available, Rented, Maintenance, Inactive)

#### 📅 Booking Management
- Create and manage bookings
- Booking status tracking (Pending, Confirmed, Active, Completed, Cancelled)
- Conflict detection for vehicle availability
- Security deposit handling
- Late fee and damage charge calculation
- Booking history and reports

#### 💳 Payment System (Stub)
- Payment processing simulation
- Multiple payment methods (Credit Card, UPI, Net Banking, etc.)
- Transaction history
- Refund processing
- Payment status tracking

#### 📁 File Management
- Document upload (driver license, receipts, etc.)
- Secure file storage in local directory
- File download with proper authentication
- Support for multiple file types

## 🛠 Installation & Setup

### Prerequisites
- **Node.js** (v16 or higher)
- **Java 17**
- **Maven 3.6+**
- **MySQL 8.0** (or use H2 for development)

### Backend Setup

1. **Navigate to backend directory:**
   ```bash
   cd backend
   ```

2. **Configure database in `application.properties`:**
   ```properties
   # For MySQL
   spring.datasource.url=jdbc:mysql://localhost:3306/rydio_db?createDatabaseIfNotExist=true
   spring.datasource.username=root
   spring.datasource.password=your_password
   
   # For H2 (development)
   # spring.datasource.url=jdbc:h2:mem:testdb
   # spring.h2.console.enabled=true
   ```

3. **Install dependencies and run:**
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

## 📚 API Documentation

### Authentication Endpoints
```
POST /api/auth/register          - User registration
POST /api/auth/login             - User login
POST /api/auth/refresh-token     - Refresh JWT token
```

### User Endpoints
```
GET  /api/users/profile          - Get user profile
PUT  /api/users/profile          - Update user profile
GET  /api/users/admin/all        - Get all users (Admin)
```

### Vehicle Endpoints
```
GET  /api/vehicles               - Get all vehicles with filters
GET  /api/vehicles/{id}          - Get vehicle by ID
POST /api/vehicles/search        - Search vehicles
POST /api/vehicles/admin         - Create vehicle (Admin)
PUT  /api/vehicles/admin/{id}    - Update vehicle (Admin)
DELETE /api/vehicles/admin/{id}  - Delete vehicle (Admin)
```

### Booking Endpoints
```
POST /api/bookings               - Create booking
GET  /api/bookings/my-bookings   - Get user bookings
GET  /api/bookings/{id}          - Get booking by ID
PUT  /api/bookings/{id}          - Update booking
POST /api/bookings/{id}/cancel   - Cancel booking
GET  /api/bookings/admin/all     - Get all bookings (Admin)
```

### Payment Endpoints
```
POST /api/payments/process       - Process payment
GET  /api/payments/my-payments   - Get user payments
GET  /api/payments/{id}          - Get payment by ID
GET  /api/payments/booking/{id}  - Get booking payments
```

### File Endpoints
```
POST /api/files/upload           - Upload file
GET  /api/files/download/{path}  - Download file
DELETE /api/files/delete/{path}  - Delete file (Admin)
```

## 🏗 Project Structure

```
Rydio/
├── frontend/                    # React frontend
│   ├── public/                  # Static assets
│   ├── src/
│   │   ├── components/          # Reusable components
│   │   ├── pages/              # Page components
│   │   ├── context/            # React context providers
│   │   ├── services/           # API service functions
│   │   ├── types/              # TypeScript type definitions
│   │   └── App.tsx             # Main app component
│   ├── package.json
│   └── tailwind.config.js
│
└── backend/                     # Spring Boot backend
    ├── src/
    │   ├── main/java/com/rydio/
    │   │   ├── booking/         # Booking module
    │   │   ├── common/          # Common utilities
    │   │   ├── file/            # File management
    │   │   ├── payment/         # Payment processing
    │   │   ├── security/        # Security configuration
    │   │   ├── user/            # User management
    │   │   ├── vehicle/         # Vehicle management
    │   │   └── RydioBackendApplication.java
    │   ├── main/resources/
    │   │   └── application.properties
    │   └── test/                # Unit tests
    ├── uploads/                 # File upload directory
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

## 🔧 Configuration

### Environment Variables

#### Backend (`application.properties`)
```properties
# Database
spring.datasource.url=jdbc:mysql://localhost:3306/rydio_db
spring.datasource.username=root
spring.datasource.password=password

# JWT
jwt.secret=your-secret-key-here
jwt.expiration=86400000

# File Upload
app.upload.dir=uploads/
spring.servlet.multipart.max-file-size=10MB
```

#### Frontend (`.env`)
```env
REACT_APP_API_BASE_URL=http://localhost:8080/api
```

## 🚀 Deployment

### Backend Deployment
1. Build JAR file: `mvn clean package`
2. Run JAR: `java -jar target/rydio-backend-1.0.0.jar`
3. Configure production database and environment variables

### Frontend Deployment
1. Build production bundle: `npm run build`
2. Serve static files using nginx, Apache, or CDN
3. Update API base URL for production

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## 📄 License

This project is licensed under the MIT License.

## 👥 Team

### Frontend Team
- React TypeScript development
- Tailwind CSS styling
- Component architecture
- State management

### Backend Team  
- Spring Boot REST API development
- JWT authentication implementation
- Database design and optimization
- Unit testing with JUnit

## 🔗 Quick Links

- **Frontend:** http://localhost:3000
- **Backend API:** http://localhost:8080/api
- **API Documentation:** http://localhost:8080/swagger-ui.html (if Swagger is configured)
- **H2 Console:** http://localhost:8080/h2-console (if H2 is enabled)

## 📞 Support

For support and questions, please create an issue in the repository or contact the development team.

---

**Built with ❤️ by the Rydio Team**