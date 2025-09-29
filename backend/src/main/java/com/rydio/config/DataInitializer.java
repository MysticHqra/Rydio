package com.rydio.config;

import com.rydio.user.entity.User;
import com.rydio.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Override
    public void run(String... args) throws Exception {
        // Create test users if they don't exist
        createTestUsers();
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
}