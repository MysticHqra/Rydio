package com.rydio.booking.controller;

import com.rydio.booking.dto.BookingRequestDto;
import com.rydio.booking.dto.BookingResponseDto;
import com.rydio.booking.dto.BookingUpdateDto;
import com.rydio.booking.service.BookingService;
import com.rydio.common.dto.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<ApiResponse<BookingResponseDto>> createBooking(@Valid @RequestBody BookingRequestDto requestDto) {
        try {
            BookingResponseDto booking = bookingService.createBooking(requestDto);
            return ResponseEntity.ok(ApiResponse.success("Booking created successfully", booking));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to create booking: " + e.getMessage()));
        }
    }

    @GetMapping("/my-bookings")
    public ResponseEntity<ApiResponse<List<BookingResponseDto>>> getUserBookings() {
        try {
            List<BookingResponseDto> bookings = bookingService.getUserBookings();
            return ResponseEntity.ok(ApiResponse.success("User bookings retrieved successfully", bookings));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to retrieve bookings: " + e.getMessage()));
        }
    }

    @GetMapping("/my-bookings/paged")
    public ResponseEntity<ApiResponse<Page<BookingResponseDto>>> getUserBookingsPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<BookingResponseDto> bookings = bookingService.getUserBookings(pageable);
            return ResponseEntity.ok(ApiResponse.success("User bookings retrieved successfully", bookings));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to retrieve bookings: " + e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BookingResponseDto>> getBookingById(@PathVariable Long id) {
        try {
            BookingResponseDto booking = bookingService.getBookingById(id);
            return ResponseEntity.ok(ApiResponse.success("Booking retrieved successfully", booking));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Booking not found: " + e.getMessage()));
        }
    }

    @GetMapping("/reference/{reference}")
    public ResponseEntity<ApiResponse<BookingResponseDto>> getBookingByReference(@PathVariable String reference) {
        try {
            BookingResponseDto booking = bookingService.getBookingByReference(reference);
            return ResponseEntity.ok(ApiResponse.success("Booking retrieved successfully", booking));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Booking not found: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<BookingResponseDto>> updateBooking(
            @PathVariable Long id,
            @Valid @RequestBody BookingUpdateDto updateDto) {
        try {
            BookingResponseDto booking = bookingService.updateBooking(id, updateDto);
            return ResponseEntity.ok(ApiResponse.success("Booking updated successfully", booking));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to update booking: " + e.getMessage()));
        }
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<ApiResponse<BookingResponseDto>> cancelBooking(
            @PathVariable Long id,
            @RequestParam(required = false) String reason) {
        try {
            BookingResponseDto booking = bookingService.cancelBooking(id, reason);
            return ResponseEntity.ok(ApiResponse.success("Booking cancelled successfully", booking));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to cancel booking: " + e.getMessage()));
        }
    }

    // Admin endpoints
    @GetMapping("/admin/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Page<BookingResponseDto>>> getAllBookings(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<BookingResponseDto> bookings = bookingService.getAllBookings(pageable);
            return ResponseEntity.ok(ApiResponse.success("All bookings retrieved successfully", bookings));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to retrieve bookings: " + e.getMessage()));
        }
    }

    @PostMapping("/admin/{id}/confirm")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<BookingResponseDto>> confirmBooking(@PathVariable Long id) {
        try {
            BookingResponseDto booking = bookingService.confirmBooking(id);
            return ResponseEntity.ok(ApiResponse.success("Booking confirmed successfully", booking));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to confirm booking: " + e.getMessage()));
        }
    }

    @PostMapping("/admin/{id}/activate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<BookingResponseDto>> activateBooking(@PathVariable Long id) {
        try {
            BookingResponseDto booking = bookingService.activateBooking(id);
            return ResponseEntity.ok(ApiResponse.success("Booking activated successfully", booking));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to activate booking: " + e.getMessage()));
        }
    }

    @PostMapping("/admin/{id}/complete")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<BookingResponseDto>> completeBooking(
            @PathVariable Long id,
            @RequestParam(defaultValue = "0") BigDecimal lateFee,
            @RequestParam(defaultValue = "0") BigDecimal damageCharges) {
        try {
            BookingResponseDto booking = bookingService.completeBooking(id, lateFee, damageCharges);
            return ResponseEntity.ok(ApiResponse.success("Booking completed successfully", booking));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to complete booking: " + e.getMessage()));
        }
    }
}