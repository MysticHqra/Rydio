package com.rydio.vehicle.repository;

import com.rydio.vehicle.entity.Vehicle;
import com.rydio.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    
    List<Vehicle> findByStatus(Vehicle.VehicleStatus status);
    
    List<Vehicle> findByVehicleType(Vehicle.VehicleType vehicleType);
    
    List<Vehicle> findByOwner(User owner);
    
    @Query("SELECT v FROM Vehicle v WHERE v.owner.id = :ownerId")
    List<Vehicle> findByOwnerId(@Param("ownerId") Long ownerId);
    
    @Query("SELECT v FROM Vehicle v WHERE v.status = :status AND v.vehicleType = :type")
    List<Vehicle> findByStatusAndVehicleType(@Param("status") Vehicle.VehicleStatus status, 
                                           @Param("type") Vehicle.VehicleType type);
    
    @Query("SELECT v FROM Vehicle v WHERE v.location LIKE %:location%")
    List<Vehicle> findByLocationContaining(@Param("location") String location);
    
    @Query("SELECT v FROM Vehicle v WHERE v.dailyRate BETWEEN :minPrice AND :maxPrice")
    List<Vehicle> findByDailyRateBetween(@Param("minPrice") Double minPrice, @Param("maxPrice") Double maxPrice);
    
    @Query("SELECT v FROM Vehicle v WHERE " +
           "(:vehicleType IS NULL OR v.vehicleType = :vehicleType) AND " +
           "(:location IS NULL OR v.location LIKE %:location%) AND " +
           "(:minPrice IS NULL OR v.dailyRate >= :minPrice) AND " +
           "(:maxPrice IS NULL OR v.dailyRate <= :maxPrice) AND " +
           "v.status = 'AVAILABLE'")
    List<Vehicle> findVehiclesWithFilters(@Param("vehicleType") Vehicle.VehicleType vehicleType,
                                        @Param("location") String location,
                                        @Param("minPrice") Double minPrice,
                                        @Param("maxPrice") Double maxPrice);
    
    Optional<Vehicle> findByLicensePlate(String licensePlate);
    
    Optional<Vehicle> findByRegistrationNumber(String registrationNumber);
    
    @Query("SELECT COUNT(v) FROM Vehicle v WHERE v.owner.id = :ownerId")
    Long countByOwnerId(@Param("ownerId") Long ownerId);
    
    @Query("SELECT COUNT(v) FROM Vehicle v WHERE v.status = :status")
    Long countByStatus(@Param("status") Vehicle.VehicleStatus status);
}