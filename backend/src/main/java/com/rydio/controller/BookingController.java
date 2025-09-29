package com.rydio.controller;

import com.rydio.common.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/bookings")
@CrossOrigin(origins = "http://localhost:3000")
public class BookingController {

    @PostMapping("")
    public ResponseEntity<ApiResponse<Map<String, Object>>> createBooking(@RequestBody Map<String, Object> bookingData) {
        try {
            // Extract booking data
            String vehicleId = (String) bookingData.get("vehicleId");
            String startDate = (String) bookingData.get("startDate");
            String endDate = (String) bookingData.get("endDate");
            String pickupLocation = (String) bookingData.get("pickupLocation");
            String dropLocation = (String) bookingData.get("dropLocation");
            
            // Validate required fields
            if (vehicleId == null || startDate == null || endDate == null || 
                pickupLocation == null || dropLocation == null) {
                return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, "All booking fields are required", null));
            }
            
            // Mock booking creation
            Map<String, Object> booking = new HashMap<>();
            booking.put("id", "booking_" + System.currentTimeMillis());
            booking.put("vehicleId", vehicleId);
            booking.put("startDate", startDate);
            booking.put("endDate", endDate);
            booking.put("pickupLocation", pickupLocation);
            booking.put("dropLocation", dropLocation);
            booking.put("status", "CONFIRMED");
            booking.put("totalAmount", calculateMockAmount(vehicleId, startDate, endDate));
            booking.put("bookingDate", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            
            return ResponseEntity.ok(
                new ApiResponse<>(true, "Booking created successfully", booking)
            );
            
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(new ApiResponse<>(false, "Booking failed: " + e.getMessage(), null));
        }
    }

    @GetMapping("")
    public ResponseEntity<ApiResponse<String>> getUserBookings() {
        // Mock user bookings - in real app, get from database based on user
        String mockBookings = """
            [
                {
                    "id": "booking_1",
                    "vehicleId": "1",
                    "vehicleBrand": "Honda",
                    "vehicleModel": "Activa 6G",
                    "startDate": "2025-10-01",
                    "endDate": "2025-10-02",
                    "status": "CONFIRMED",
                    "totalAmount": 1200,
                    "pickupLocation": "Mumbai Central",
                    "dropLocation": "Mumbai Airport"
                }
            ]
            """;
        
        return ResponseEntity.ok(
            new ApiResponse<>(true, "Bookings retrieved successfully", mockBookings)
        );
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getBookingDetails(@PathVariable String bookingId) {
        // Mock booking details
        Map<String, Object> booking = new HashMap<>();
        booking.put("id", bookingId);
        booking.put("vehicleId", "1");
        booking.put("vehicleBrand", "Honda");
        booking.put("vehicleModel", "Activa 6G");
        booking.put("startDate", "2025-10-01");
        booking.put("endDate", "2025-10-02");
        booking.put("status", "CONFIRMED");
        booking.put("totalAmount", 1200);
        booking.put("pickupLocation", "Mumbai Central");
        booking.put("dropLocation", "Mumbai Airport");
        
        return ResponseEntity.ok(
            new ApiResponse<>(true, "Booking details retrieved", booking)
        );
    }

    private double calculateMockAmount(String vehicleId, String startDate, String endDate) {
        // Mock calculation - in real app, calculate based on vehicle rate and duration
        return 1200.0; // Fixed amount for demo
    }
}