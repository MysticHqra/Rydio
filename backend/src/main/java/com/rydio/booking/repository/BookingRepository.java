package com.rydio.booking.repository;

import com.rydio.booking.entity.Booking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByUserIdOrderByCreatedAtDesc(Long userId);

    Page<Booking> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);

    List<Booking> findByVehicleIdAndStatusIn(Long vehicleId, List<Booking.BookingStatus> statuses);

    Optional<Booking> findByBookingReference(String bookingReference);

    @Query("SELECT b FROM Booking b WHERE b.vehicle.id = :vehicleId AND " +
           "((b.startDate <= :endDate AND b.endDate >= :startDate)) AND " +
           "b.status IN ('CONFIRMED', 'ACTIVE')")
    List<Booking> findConflictingBookings(
        @Param("vehicleId") Long vehicleId,
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate
    );

    @Query("SELECT b FROM Booking b WHERE b.status = :status AND b.startDate <= :currentDate")
    List<Booking> findBookingsToActivate(
        @Param("status") Booking.BookingStatus status,
        @Param("currentDate") LocalDateTime currentDate
    );

    @Query("SELECT b FROM Booking b WHERE b.status = :status AND b.endDate <= :currentDate")
    List<Booking> findOverdueBookings(
        @Param("status") Booking.BookingStatus status,
        @Param("currentDate") LocalDateTime currentDate
    );

    @Query("SELECT COUNT(b) FROM Booking b WHERE b.user.id = :userId AND b.status = :status")
    long countByUserIdAndStatus(@Param("userId") Long userId, @Param("status") Booking.BookingStatus status);
}