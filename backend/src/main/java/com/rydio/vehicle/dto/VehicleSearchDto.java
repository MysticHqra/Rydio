package com.rydio.vehicle.dto;

import com.rydio.vehicle.entity.Vehicle;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class VehicleSearchDto {
    private Vehicle.VehicleType vehicleType;
    private String brand;
    private String model;
    private Integer minYear;
    private Integer maxYear;
    private String fuelType;
    private BigDecimal minDailyRate;
    private BigDecimal maxDailyRate;
    private String location;
    private Vehicle.VehicleStatus status;
    private Integer minSeatCount;
    private Integer maxSeatCount;
}