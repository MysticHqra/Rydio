package com.rydio.payment.controller;

import com.rydio.common.dto.ApiResponse;
import com.rydio.payment.dto.PaymentRequestDto;
import com.rydio.payment.dto.PaymentResponseDto;
import com.rydio.payment.entity.Payment;
import com.rydio.payment.service.PaymentService;
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
@RequestMapping("/payments")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/process")
    public ResponseEntity<ApiResponse<PaymentResponseDto>> processPayment(@Valid @RequestBody PaymentRequestDto requestDto) {
        try {
            PaymentResponseDto payment = paymentService.processPayment(requestDto);
            if (payment.getStatus() == Payment.PaymentStatus.SUCCESS) {
                return ResponseEntity.ok(ApiResponse.success("Payment processed successfully", payment));
            } else {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error("Payment failed", payment));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to process payment: " + e.getMessage()));
        }
    }

    @GetMapping("/my-payments")
    public ResponseEntity<ApiResponse<List<PaymentResponseDto>>> getUserPayments() {
        try {
            List<PaymentResponseDto> payments = paymentService.getUserPayments();
            return ResponseEntity.ok(ApiResponse.success("User payments retrieved successfully", payments));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to retrieve payments: " + e.getMessage()));
        }
    }

    @GetMapping("/my-payments/paged")
    public ResponseEntity<ApiResponse<Page<PaymentResponseDto>>> getUserPaymentsPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<PaymentResponseDto> payments = paymentService.getUserPayments(pageable);
            return ResponseEntity.ok(ApiResponse.success("User payments retrieved successfully", payments));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to retrieve payments: " + e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PaymentResponseDto>> getPaymentById(@PathVariable Long id) {
        try {
            PaymentResponseDto payment = paymentService.getPaymentById(id);
            return ResponseEntity.ok(ApiResponse.success("Payment retrieved successfully", payment));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Payment not found: " + e.getMessage()));
        }
    }

    @GetMapping("/transaction/{transactionId}")
    public ResponseEntity<ApiResponse<PaymentResponseDto>> getPaymentByTransactionId(@PathVariable String transactionId) {
        try {
            PaymentResponseDto payment = paymentService.getPaymentByTransactionId(transactionId);
            return ResponseEntity.ok(ApiResponse.success("Payment retrieved successfully", payment));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Payment not found: " + e.getMessage()));
        }
    }

    @GetMapping("/booking/{bookingId}")
    public ResponseEntity<ApiResponse<List<PaymentResponseDto>>> getBookingPayments(@PathVariable Long bookingId) {
        try {
            List<PaymentResponseDto> payments = paymentService.getBookingPayments(bookingId);
            return ResponseEntity.ok(ApiResponse.success("Booking payments retrieved successfully", payments));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to retrieve payments: " + e.getMessage()));
        }
    }

    // Admin endpoints
    @GetMapping("/admin/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Page<PaymentResponseDto>>> getAllPayments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<PaymentResponseDto> payments = paymentService.getAllPayments(pageable);
            return ResponseEntity.ok(ApiResponse.success("All payments retrieved successfully", payments));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to retrieve payments: " + e.getMessage()));
        }
    }

    @GetMapping("/admin/status/{status}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<PaymentResponseDto>>> getPaymentsByStatus(@PathVariable Payment.PaymentStatus status) {
        try {
            List<PaymentResponseDto> payments = paymentService.getPaymentsByStatus(status);
            return ResponseEntity.ok(ApiResponse.success("Payments retrieved successfully", payments));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to retrieve payments: " + e.getMessage()));
        }
    }

    @PostMapping("/admin/{id}/refund")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<PaymentResponseDto>> processRefund(
            @PathVariable Long id,
            @RequestParam BigDecimal refundAmount,
            @RequestParam(required = false) String reason) {
        try {
            PaymentResponseDto payment = paymentService.processRefund(id, refundAmount, reason);
            return ResponseEntity.ok(ApiResponse.success("Refund processed successfully", payment));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to process refund: " + e.getMessage()));
        }
    }
}