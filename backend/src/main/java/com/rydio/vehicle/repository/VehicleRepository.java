package com.rydio.vehicle.repository;

import com.rydio.vehicle.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long>, JpaSpecificationExecutor<Vehicle> {
    
    Optional<Vehicle> findByLicensePlate(String licensePlate);
    
    List<Vehicle> findByVehicleType(Vehicle.VehicleType vehicleType);
    
    List<Vehicle> findByStatus(Vehicle.VehicleStatus status);
    
    List<Vehicle> findByBrandIgnoreCase(String brand);
    
    List<Vehicle> findByModelIgnoreCase(String model);
    
    List<Vehicle> findByLocationIgnoreCase(String location);
    
    @Query("SELECT v FROM Vehicle v WHERE v.dailyRate BETWEEN :minRate AND :maxRate")
    List<Vehicle> findByDailyRateBetween(@Param("minRate") BigDecimal minRate, @Param("maxRate") BigDecimal maxRate);
    
    @Query("SELECT v FROM Vehicle v WHERE v.year BETWEEN :minYear AND :maxYear")
    List<Vehicle> findByYearBetween(@Param("minYear") Integer minYear, @Param("maxYear") Integer maxYear);
    
    @Query("SELECT v FROM Vehicle v WHERE v.seatCount >= :minSeats")
    List<Vehicle> findByMinimumSeats(@Param("minSeats") Integer minSeats);
    
    @Query("SELECT v FROM Vehicle v WHERE v.status = 'AVAILABLE' AND v.vehicleType = :type")
    List<Vehicle> findAvailableVehiclesByType(@Param("type") Vehicle.VehicleType type);
    
    @Query("SELECT DISTINCT v.brand FROM Vehicle v ORDER BY v.brand")
    List<String> findAllBrands();
    
    @Query("SELECT DISTINCT v.model FROM Vehicle v WHERE v.brand = :brand ORDER BY v.model")
    List<String> findModelsByBrand(@Param("brand") String brand);
    
    @Query("SELECT DISTINCT v.location FROM Vehicle v WHERE v.location IS NOT NULL ORDER BY v.location")
    List<String> findAllLocations();
    
    boolean existsByLicensePlate(String licensePlate);
}