package com.rydio.booking.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class BookingRequestDto {

    @NotNull(message = "Vehicle ID is required")
    private Long vehicleId;

    @NotNull(message = "Start date is required")
    @Future(message = "Start date must be in the future")
    private LocalDateTime startDate;

    @NotNull(message = "End date is required")
    @Future(message = "End date must be in the future")
    private LocalDateTime endDate;

    @NotBlank(message = "Pickup location is required")
    @Size(max = 255, message = "Pickup location must not exceed 255 characters")
    private String pickupLocation;

    @NotBlank(message = "Return location is required")
    @Size(max = 255, message = "Return location must not exceed 255 characters")
    private String returnLocation;

    @DecimalMin(value = "0.0", inclusive = false, message = "Security deposit must be greater than 0")
    private BigDecimal securityDeposit;

    @Size(max = 1000, message = "Notes must not exceed 1000 characters")
    private String notes;
}