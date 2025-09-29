package com.rydio.booking.dto;

import lombok.Data;

@Data
public class BookingUpdateDto {
    private String pickupLocation;
    private String returnLocation;
    private String notes;
}