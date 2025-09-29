-- Database initialization script for H2/MySQL compatibility

-- Insert sample admin user (password: Admin123)
INSERT INTO users (username, email, password, first_name, last_name, role, is_active, email_verified, created_at, updated_at) 
VALUES (
    'admin', 
    'admin@rydio.com', 
    '$2a$10$N.zmdr9k7uOuvVPG.MQUzOKlmLjVBuxbj6EMekFdJoRGP5TwPVtuK', 
    'System', 
    'Administrator', 
    'ADMIN', 
    true, 
    true, 
    CURRENT_TIMESTAMP, 
    CURRENT_TIMESTAMP
);

-- Insert sample vehicles
INSERT INTO vehicles (license_plate, brand, model, vehicle_year, color, vehicle_type, fuel_type, seat_count, daily_rate, status, location, description, created_at, updated_at) VALUES
('MH01AB1234', 'Maruti', 'Swift', 2022, 'White', 'CAR', 'PETROL', 5, 1200.00, 'AVAILABLE', 'Mumbai', 'Compact car perfect for city rides', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('MH01CD5678', 'Honda', 'City', 2023, 'Silver', 'CAR', 'PETROL', 5, 1800.00, 'AVAILABLE', 'Mumbai', 'Comfortable sedan for long trips', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('MH01EF9012', 'Hero', 'Splendor', 2022, 'Black', 'BIKE', 'PETROL', 2, 300.00, 'AVAILABLE', 'Pune', 'Fuel-efficient motorcycle', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('KA01GH3456', 'TVS', 'Jupiter', 2023, 'Blue', 'SCOOTER', 'PETROL', 2, 400.00, 'AVAILABLE', 'Bangalore', 'Easy to ride scooter', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('DL01IJ7890', 'Tata', 'Nexon', 2022, 'Red', 'CAR', 'ELECTRIC', 5, 2200.00, 'AVAILABLE', 'Delhi', 'Electric SUV with modern features', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);