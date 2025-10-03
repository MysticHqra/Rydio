package com.rydio.config;

import com.rydio.user.entity.User;
import com.rydio.user.service.UserService;
import com.rydio.vehicle.service.VehicleService;
import com.rydio.vehicle.dto.CreateVehicleRequest;
import com.rydio.vehicle.entity.Vehicle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private VehicleService vehicleService;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Override
    public void run(String... args) throws Exception {
        // Create test users if they don't exist
        createTestUsers();
        
        // Create test vehicles if they don't exist
        createTestVehicles();
    }
    
    private void createTestUsers() {
        // Admin user
        if (!userService.existsByUsername("admin")) {
            User admin = userService.createUser(
                "admin",
                "admin@rydio.com",
                passwordEncoder.encode("admin123"), // Hash the password
                "Admin",
                "User",
                User.Role.ADMIN
            );
            admin.setPhoneNumber("+1234567890");
            admin.setAddress("123 Admin Street, Admin City");
            admin.setEmailVerified(true);
            userService.save(admin);
            System.out.println("Created admin user: admin@rydio.com / admin");
        }
        
        // Hara user
        if (!userService.existsByUsername("hara")) {
            User hara = userService.createUser(
                "hara",
                "hara@gmail.com",
                passwordEncoder.encode("Hara@1234"), // Hash the password
                "Hara",
                "Patel",
                User.Role.USER
            );
            hara.setPhoneNumber("+9876543210");
            hara.setAddress("456 User Lane, User City");
            hara.setEmailVerified(true);
            userService.save(hara);
            System.out.println("Created hara user: hara@gmail.com / hara");
        }
        
        // Test user
        if (!userService.existsByUsername("testuser")) {
            User testUser = userService.createUser(
                "testuser",
                "test@example.com",
                passwordEncoder.encode("password123"), // Hash the password
                "Test",
                "User",
                User.Role.USER
            );
            testUser.setPhoneNumber("+5555555555");
            testUser.setAddress("789 Test Avenue, Test City");
            testUser.setEmailVerified(true);
            userService.save(testUser);
            System.out.println("Created test user: test@example.com / testuser");
        }
    }
    
    private void createTestVehicles() {
        // Check if vehicles already exist to avoid duplicates
        if (vehicleService.getAllVehicles().isEmpty()) {
            
            // Get test users as vehicle owners
            User hara = userService.findByUsername("hara").orElse(null);
            User testUser = userService.findByUsername("testuser").orElse(null);
            User admin = userService.findByUsername("admin").orElse(null);
            
            if (hara != null && testUser != null && admin != null) {
                
                // Vehicle 1: Honda Activa 6G (Hara's Scooter)
                CreateVehicleRequest scooter = new CreateVehicleRequest();
                scooter.setLicensePlate("MH01AB1234");
                scooter.setBrand("Honda");
                scooter.setModel("Activa 6G");
                scooter.setYear(2023);
                scooter.setColor("White");
                scooter.setVehicleType(Vehicle.VehicleType.SCOOTER);
                scooter.setFuelType(Vehicle.FuelType.PETROL);
                scooter.setEngineCapacity("110cc");
                scooter.setSeatCount(2);
                scooter.setDailyRate(600.0);
                scooter.setHourlyRate(25.0);
                scooter.setMileage(50.0);
                scooter.setLocation("Mumbai Central");
                scooter.setDescription("Perfect scooter for city rides with excellent mileage");
                scooter.setFeatures("Bluetooth connectivity, LED headlights, Digital console");
                scooter.setImageUrl("https://imgd.aeplcdn.com/664x374/n/cw/ec/1/versions/--connected-obd-2b1737697110094.jpg?q=80");
                vehicleService.createVehicle(scooter, hara.getId());
                
                // Vehicle 2: Maruti Swift (Test User's Car)
                CreateVehicleRequest car1 = new CreateVehicleRequest();
                car1.setLicensePlate("MH02CD5678");
                car1.setBrand("Maruti");
                car1.setModel("Swift");
                car1.setYear(2024);
                car1.setColor("Red");
                car1.setVehicleType(Vehicle.VehicleType.CAR);
                car1.setFuelType(Vehicle.FuelType.PETROL);
                car1.setEngineCapacity("1200cc");
                car1.setSeatCount(5);
                car1.setDailyRate(1800.0);
                car1.setHourlyRate(75.0);
                car1.setMileage(20.0);
                car1.setLocation("Mumbai Central");
                car1.setDescription("Comfortable hatchback perfect for family trips");
                car1.setFeatures("AC, Power steering, ABS, Airbags");
                car1.setImageUrl("https://imgd.aeplcdn.com/664x374/n/cw/ec/159099/swift-exterior-right-front-three-quarter-31.jpeg?isig=0&q=80");
                vehicleService.createVehicle(car1, testUser.getId());
                
                // Vehicle 3: Hero Splendor Plus (Admin's Bike)
                CreateVehicleRequest bike = new CreateVehicleRequest();
                bike.setLicensePlate("MH03EF9012");
                bike.setBrand("Hero");
                bike.setModel("Splendor Plus");
                bike.setYear(2023);
                bike.setColor("Black");
                bike.setVehicleType(Vehicle.VehicleType.BIKE);
                bike.setFuelType(Vehicle.FuelType.PETROL);
                bike.setEngineCapacity("97cc");
                bike.setSeatCount(2);
                bike.setDailyRate(480.0);
                bike.setHourlyRate(20.0);
                bike.setMileage(65.0);
                bike.setLocation("Pune");
                bike.setDescription("Reliable motorcycle for daily commute with great mileage");
                bike.setFeatures("Kick start, Electric start, Alloy wheels");
                bike.setImageUrl("https://imgd.aeplcdn.com/664x374/n/cw/ec/1/versions/--drum-brake-obd-2b1744875559407.jpg?q=80");
                vehicleService.createVehicle(bike, admin.getId());
                
                // Vehicle 4: Hyundai i20 (Hara's Premium Car)
                CreateVehicleRequest car2 = new CreateVehicleRequest();
                car2.setLicensePlate("MH04GH3456");
                car2.setBrand("Hyundai");
                car2.setModel("i20");
                car2.setYear(2024);
                car2.setColor("Blue");
                car2.setVehicleType(Vehicle.VehicleType.CAR);
                car2.setFuelType(Vehicle.FuelType.PETROL);
                car2.setEngineCapacity("1197cc");
                car2.setSeatCount(5);
                car2.setDailyRate(2200.0);
                car2.setHourlyRate(90.0);
                car2.setMileage(18.0);
                car2.setLocation("Delhi");
                car2.setDescription("Premium hatchback with modern features and excellent comfort");
                car2.setFeatures("Touchscreen, Automatic transmission, Sunroof, Wireless charging");
                car2.setImageUrl("https://imgd.aeplcdn.com/664x374/n/cw/ec/150603/i20-exterior-right-front-three-quarter-7.jpeg?isig=0&q=80");
                vehicleService.createVehicle(car2, hara.getId());
                
                // Vehicle 5: Ola S1 Pro (Test User's Electric Scooter)
                CreateVehicleRequest electricScooter = new CreateVehicleRequest();
                electricScooter.setLicensePlate("MH05IJ7890");
                electricScooter.setBrand("Ola");
                electricScooter.setModel("S1 Pro");
                electricScooter.setYear(2024);
                electricScooter.setColor("White");
                electricScooter.setVehicleType(Vehicle.VehicleType.SCOOTER);
                electricScooter.setFuelType(Vehicle.FuelType.ELECTRIC);
                electricScooter.setEngineCapacity("3.97kWh");
                electricScooter.setSeatCount(2);
                electricScooter.setDailyRate(800.0);
                electricScooter.setHourlyRate(35.0);
                electricScooter.setMileage(120.0); // Range in km
                electricScooter.setLocation("Bangalore");
                electricScooter.setDescription("Eco-friendly electric scooter with smart features");
                electricScooter.setFeatures("App connectivity, GPS tracking, Hill hold assist");
                electricScooter.setImageUrl("https://imgd.aeplcdn.com/664x374/n/mu64bfb_1811599.jpg?q=80");
                vehicleService.createVehicle(electricScooter, testUser.getId());
                
                System.out.println("Created 5 test vehicles:");
                System.out.println("- Honda Activa 6G (Hara) - MH01AB1234");
                System.out.println("- Maruti Swift (Test User) - MH02CD5678");
                System.out.println("- Hero Splendor Plus (Admin) - MH03EF9012");
                System.out.println("- Hyundai i20 (Hara) - MH04GH3456");
                System.out.println("- Ola S1 Pro (Test User) - MH05IJ7890");
            } else {
                System.out.println("Could not create test vehicles - test users not found");
            }
        } else {
            System.out.println("Test vehicles already exist, skipping creation");
        }
    }
}