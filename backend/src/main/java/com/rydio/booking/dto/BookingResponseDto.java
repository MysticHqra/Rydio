package com.rydio.booking.dto;

import com.rydio.booking.entity.Booking;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class BookingResponseDto {
    private Long id;
    private String bookingReference;
    private Long userId;
    private String userFullName;
    private Long vehicleId;
    private String vehicleBrand;
    private String vehicleModel;
    private String licensePlate;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String pickupLocation;
    private String returnLocation;
    private BigDecimal totalAmount;
    private BigDecimal securityDeposit;
    private Booking.BookingStatus status;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime cancelledAt;
    private String cancellationReason;
    private LocalDateTime actualReturnDate;
    private BigDecimal lateFee;
    private BigDecimal damageCharges;
}