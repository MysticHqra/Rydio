# Rydio Backend API

A comprehensive Spring Boot REST API for the Rydio vehicle rental platform with JWT authentication, role-based access control, and MySQL database integration.

## Features

### 🔐 Authentication & Security
- JWT-based authentication with refresh tokens
- BCrypt password hashing
- Role-based access control (ADMIN/USER)
- Spring Security integration
- Cross-origin resource sharing (CORS) support

### 👥 User Management
- User registration and login
- Profile management
- Admin user management
- Role management
- Account activation/deactivation

### 🚗 Vehicle Management
- Complete CRUD operations for vehicles
- Vehicle search and filtering
- Categorization (Cars, Bikes, Scooters, Bicycles)
- Status management (Available, Rented, Maintenance, Inactive)
- Image upload support

### 📅 Booking Management
- Create and manage bookings
- Booking status tracking
- Booking history
- Cancellation handling
- Late fee and damage charge calculation

### 💳 Payment Management
- Payment transaction recording
- Multiple payment methods support
- Refund processing
- Payment status tracking
- Security deposit handling

### 📁 File Management
- File upload and download APIs
- Document storage (receipts, licenses, etc.)
- Image management for vehicles
- Secure file access

## Technology Stack

- **Framework**: Spring Boot 3.2.0
- **Language**: Java 17
- **Database**: MySQL 8.0
- **Security**: Spring Security + JWT
- **Build Tool**: Maven
- **Testing**: JUnit 5 + Mockito
- **Documentation**: Built-in API endpoints

## Prerequisites

- Java 17 or higher
- Maven 3.6+
- MySQL 8.0+
- IDE (IntelliJ IDEA/Eclipse/VS Code)

## Setup Instructions

### 1. Database Setup

```sql
-- Create MySQL database
CREATE DATABASE rydio_db;

-- Create MySQL user (optional)
CREATE USER 'rydio_user'@'localhost' IDENTIFIED BY 'rydio_password';
GRANT ALL PRIVILEGES ON rydio_db.* TO 'rydio_user'@'localhost';
FLUSH PRIVILEGES;
```

### 2. Application Configuration

Update `src/main/resources/application.properties`:

```properties
# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/rydio_db?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=your_mysql_password

# JWT Configuration
jwt.secret=your-secret-key-here-make-it-long-and-secure
jwt.expiration=86400000
```

### 3. Build and Run

```bash
# Navigate to backend directory
cd backend

# Clean and install dependencies
mvn clean install

# Run the application
mvn spring-boot:run

# Or run with specific profile
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

The API will be available at: `http://localhost:8080/api`

### 4. Default Admin Account

- **Username**: admin
- **Password**: Admin123
- **Email**: admin@rydio.com

## API Endpoints

### Authentication
- `POST /api/auth/register` - User registration
- `POST /api/auth/login` - User login
- `POST /api/auth/refresh-token` - Refresh JWT token

### User Management
- `GET /api/users/profile` - Get current user profile
- `PUT /api/users/profile` - Update user profile
- `GET /api/users` - Get all users (Admin only)
- `PUT /api/users/{id}/toggle-status` - Toggle user status (Admin only)

### Vehicle Management
- `GET /api/vehicles` - Get all vehicles
- `GET /api/vehicles/{id}` - Get vehicle by ID
- `POST /api/vehicles` - Create new vehicle (Admin only)
- `PUT /api/vehicles/{id}` - Update vehicle (Admin only)
- `DELETE /api/vehicles/{id}` - Delete vehicle (Admin only)
- `GET /api/vehicles/available` - Get available vehicles
- `POST /api/vehicles/search` - Search vehicles with filters

### Booking Management
- `GET /api/bookings` - Get user bookings
- `POST /api/bookings` - Create new booking
- `PUT /api/bookings/{id}/cancel` - Cancel booking
- `GET /api/bookings/history` - Get booking history

### File Management
- `POST /api/files/upload` - Upload file
- `GET /api/files/download/{subfolder}/{filename}` - Download file
- `DELETE /api/files/delete/{subfolder}/{filename}` - Delete file (Admin only)

## Testing

```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=UserServiceTest

# Run with coverage
mvn test jacoco:report
```

## Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── com/rydio/
│   │       ├── RydioBackendApplication.java
│   │       ├── common/           # Shared components
│   │       ├── security/         # Security configuration
│   │       ├── user/            # User management
│   │       ├── vehicle/         # Vehicle management
│   │       ├── booking/         # Booking management
│   │       ├── payment/         # Payment management
│   │       └── file/            # File management
│   └── resources/
│       ├── application.properties
│       └── data.sql
└── test/
    └── java/
        └── com/rydio/           # Unit tests
```

## Security Features

- **JWT Authentication**: Stateless authentication using JSON Web Tokens
- **Password Encryption**: BCrypt hashing for secure password storage
- **Role-based Authorization**: Fine-grained access control
- **CORS Configuration**: Secure cross-origin requests
- **Input Validation**: Bean validation for request DTOs
- **Exception Handling**: Global exception handling with proper error responses

## Environment Variables

For production deployment, use environment variables:

```bash
export DB_URL=jdbc:mysql://localhost:3306/rydio_db
export DB_USERNAME=your_username
export DB_PASSWORD=your_password
export JWT_SECRET=your-production-secret
export JWT_EXPIRATION=86400000
export UPLOAD_DIR=/app/uploads
```

## Contributing

1. Fork the repository
2. Create a feature branch
3. Write tests for new features
4. Ensure all tests pass
5. Submit a pull request

## License

This project is licensed under the MIT License.

## Support

For issues and questions:
- Create an issue in the repository
- Contact the development team
- Check the API documentation at `/api` endpoint