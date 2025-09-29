package com.rydio.booking.service;

import com.rydio.booking.dto.BookingRequestDto;
import com.rydio.booking.dto.BookingResponseDto;
import com.rydio.booking.dto.BookingUpdateDto;
import com.rydio.booking.entity.Booking;
import com.rydio.booking.repository.BookingRepository;
import com.rydio.user.entity.User;
import com.rydio.user.repository.UserRepository;
import com.rydio.vehicle.entity.Vehicle;
import com.rydio.vehicle.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final VehicleRepository vehicleRepository;

    public BookingResponseDto createBooking(BookingRequestDto requestDto) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Vehicle vehicle = vehicleRepository.findById(requestDto.getVehicleId())
                .orElseThrow(() -> new RuntimeException("Vehicle not found"));

        // Check if vehicle is available
        if (vehicle.getStatus() != Vehicle.VehicleStatus.AVAILABLE) {
            throw new RuntimeException("Vehicle is not available");
        }

        // Check for conflicting bookings
        List<Booking> conflicts = bookingRepository.findConflictingBookings(
                vehicle.getId(), requestDto.getStartDate(), requestDto.getEndDate()
        );
        if (!conflicts.isEmpty()) {
            throw new RuntimeException("Vehicle is already booked for the selected dates");
        }

        // Validate dates
        if (requestDto.getStartDate().isAfter(requestDto.getEndDate())) {
            throw new RuntimeException("Start date must be before end date");
        }

        if (requestDto.getStartDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Start date must be in the future");
        }

        // Calculate total amount
        BigDecimal totalAmount = calculateTotalAmount(vehicle, requestDto.getStartDate(), requestDto.getEndDate());

        Booking booking = new Booking();
        booking.setBookingReference(generateBookingReference());
        booking.setUser(user);
        booking.setVehicle(vehicle);
        booking.setStartDate(requestDto.getStartDate());
        booking.setEndDate(requestDto.getEndDate());
        booking.setPickupLocation(requestDto.getPickupLocation());
        booking.setReturnLocation(requestDto.getReturnLocation());
        booking.setTotalAmount(totalAmount);
        booking.setSecurityDeposit(requestDto.getSecurityDeposit() != null ? 
                requestDto.getSecurityDeposit() : vehicle.getDailyRate());
        booking.setNotes(requestDto.getNotes());
        booking.setStatus(Booking.BookingStatus.PENDING);

        booking = bookingRepository.save(booking);
        log.info("Created booking with reference: {} for user: {}", booking.getBookingReference(), username);

        return mapToResponseDto(booking);
    }

    @Transactional(readOnly = true)
    public List<BookingResponseDto> getUserBookings() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Booking> bookings = bookingRepository.findByUserIdOrderByCreatedAtDesc(user.getId());
        return bookings.stream().map(this::mapToResponseDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<BookingResponseDto> getUserBookings(Pageable pageable) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Page<Booking> bookings = bookingRepository.findByUserIdOrderByCreatedAtDesc(user.getId(), pageable);
        return bookings.map(this::mapToResponseDto);
    }

    @Transactional(readOnly = true)
    public BookingResponseDto getBookingById(Long id) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        // Check if user owns this booking or is admin
        if (!booking.getUser().getId().equals(user.getId()) && 
            !user.getRole().equals(User.Role.ADMIN)) {
            throw new RuntimeException("Access denied");
        }

        return mapToResponseDto(booking);
    }

    @Transactional(readOnly = true)
    public BookingResponseDto getBookingByReference(String reference) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Booking booking = bookingRepository.findByBookingReference(reference)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        // Check if user owns this booking or is admin
        if (!booking.getUser().getId().equals(user.getId()) && 
            !user.getRole().equals(User.Role.ADMIN)) {
            throw new RuntimeException("Access denied");
        }

        return mapToResponseDto(booking);
    }

    public BookingResponseDto updateBooking(Long id, BookingUpdateDto updateDto) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        // Check if user owns this booking
        if (!booking.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Access denied");
        }

        // Only allow updates for pending bookings
        if (booking.getStatus() != Booking.BookingStatus.PENDING) {
            throw new RuntimeException("Can only update pending bookings");
        }

        if (updateDto.getPickupLocation() != null) {
            booking.setPickupLocation(updateDto.getPickupLocation());
        }
        if (updateDto.getReturnLocation() != null) {
            booking.setReturnLocation(updateDto.getReturnLocation());
        }
        if (updateDto.getNotes() != null) {
            booking.setNotes(updateDto.getNotes());
        }

        booking = bookingRepository.save(booking);
        log.info("Updated booking: {} by user: {}", booking.getBookingReference(), username);

        return mapToResponseDto(booking);
    }

    public BookingResponseDto cancelBooking(Long id, String reason) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        // Check if user owns this booking
        if (!booking.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Access denied");
        }

        // Only allow cancellation for pending or confirmed bookings
        if (booking.getStatus() != Booking.BookingStatus.PENDING && 
            booking.getStatus() != Booking.BookingStatus.CONFIRMED) {
            throw new RuntimeException("Cannot cancel this booking");
        }

        booking.setStatus(Booking.BookingStatus.CANCELLED);
        booking.setCancelledAt(LocalDateTime.now());
        booking.setCancellationReason(reason);

        booking = bookingRepository.save(booking);
        log.info("Cancelled booking: {} by user: {}", booking.getBookingReference(), username);

        return mapToResponseDto(booking);
    }

    // Admin methods
    @Transactional(readOnly = true)
    public Page<BookingResponseDto> getAllBookings(Pageable pageable) {
        Page<Booking> bookings = bookingRepository.findAll(pageable);
        return bookings.map(this::mapToResponseDto);
    }

    public BookingResponseDto confirmBooking(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        if (booking.getStatus() != Booking.BookingStatus.PENDING) {
            throw new RuntimeException("Can only confirm pending bookings");
        }

        booking.setStatus(Booking.BookingStatus.CONFIRMED);
        booking = bookingRepository.save(booking);

        log.info("Confirmed booking: {}", booking.getBookingReference());
        return mapToResponseDto(booking);
    }

    public BookingResponseDto activateBooking(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        if (booking.getStatus() != Booking.BookingStatus.CONFIRMED) {
            throw new RuntimeException("Can only activate confirmed bookings");
        }

        booking.setStatus(Booking.BookingStatus.ACTIVE);
        booking.getVehicle().setStatus(Vehicle.VehicleStatus.RENTED);
        
        booking = bookingRepository.save(booking);
        vehicleRepository.save(booking.getVehicle());

        log.info("Activated booking: {}", booking.getBookingReference());
        return mapToResponseDto(booking);
    }

    public BookingResponseDto completeBooking(Long id, BigDecimal lateFee, BigDecimal damageCharges) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        if (booking.getStatus() != Booking.BookingStatus.ACTIVE) {
            throw new RuntimeException("Can only complete active bookings");
        }

        booking.setStatus(Booking.BookingStatus.COMPLETED);
        booking.setActualReturnDate(LocalDateTime.now());
        booking.setLateFee(lateFee);
        booking.setDamageCharges(damageCharges);
        booking.getVehicle().setStatus(Vehicle.VehicleStatus.AVAILABLE);

        booking = bookingRepository.save(booking);
        vehicleRepository.save(booking.getVehicle());

        log.info("Completed booking: {}", booking.getBookingReference());
        return mapToResponseDto(booking);
    }

    private BigDecimal calculateTotalAmount(Vehicle vehicle, LocalDateTime startDate, LocalDateTime endDate) {
        Duration duration = Duration.between(startDate, endDate);
        long days = duration.toDays();
        if (days == 0) days = 1; // Minimum 1 day

        return vehicle.getDailyRate().multiply(BigDecimal.valueOf(days));
    }

    private String generateBookingReference() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        return "BK" + timestamp;
    }

    private BookingResponseDto mapToResponseDto(Booking booking) {
        BookingResponseDto dto = new BookingResponseDto();
        dto.setId(booking.getId());
        dto.setBookingReference(booking.getBookingReference());
        dto.setUserId(booking.getUser().getId());
        dto.setUserFullName(booking.getUser().getFirstName() + " " + booking.getUser().getLastName());
        dto.setVehicleId(booking.getVehicle().getId());
        dto.setVehicleBrand(booking.getVehicle().getBrand());
        dto.setVehicleModel(booking.getVehicle().getModel());
        dto.setLicensePlate(booking.getVehicle().getLicensePlate());
        dto.setStartDate(booking.getStartDate());
        dto.setEndDate(booking.getEndDate());
        dto.setPickupLocation(booking.getPickupLocation());
        dto.setReturnLocation(booking.getReturnLocation());
        dto.setTotalAmount(booking.getTotalAmount());
        dto.setSecurityDeposit(booking.getSecurityDeposit());
        dto.setStatus(booking.getStatus());
        dto.setNotes(booking.getNotes());
        dto.setCreatedAt(booking.getCreatedAt());
        dto.setCancelledAt(booking.getCancelledAt());
        dto.setCancellationReason(booking.getCancellationReason());
        dto.setActualReturnDate(booking.getActualReturnDate());
        dto.setLateFee(booking.getLateFee());
        dto.setDamageCharges(booking.getDamageCharges());
        return dto;
    }
}