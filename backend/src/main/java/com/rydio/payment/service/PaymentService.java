package com.rydio.payment.service;

import com.rydio.booking.entity.Booking;
import com.rydio.booking.repository.BookingRepository;
import com.rydio.payment.dto.PaymentRequestDto;
import com.rydio.payment.dto.PaymentResponseDto;
import com.rydio.payment.entity.Payment;
import com.rydio.payment.repository.PaymentRepository;
import com.rydio.user.entity.User;
import com.rydio.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final Random random = new Random();

    public PaymentResponseDto processPayment(PaymentRequestDto requestDto) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Booking booking = bookingRepository.findById(requestDto.getBookingId())
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        // Verify the booking belongs to the user
        if (!booking.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Access denied");
        }

        // Create payment record
        Payment payment = new Payment();
        payment.setTransactionId(generateTransactionId());
        payment.setUser(user);
        payment.setBooking(booking);
        payment.setAmount(requestDto.getAmount());
        payment.setPaymentType(requestDto.getPaymentType());
        payment.setPaymentMethod(requestDto.getPaymentMethod());
        payment.setStatus(Payment.PaymentStatus.PENDING);
        payment.setNotes(requestDto.getNotes());

        // Simulate payment processing
        boolean paymentSuccess = simulatePaymentGateway(requestDto);
        
        if (paymentSuccess) {
            payment.setStatus(Payment.PaymentStatus.SUCCESS);
            payment.setPaymentDate(LocalDateTime.now());
            payment.setGatewayReference("GW" + System.currentTimeMillis());
            payment.setGatewayResponse("Payment successful");
            
            // Update booking status based on payment type
            if (requestDto.getPaymentType() == Payment.PaymentType.BOOKING_PAYMENT) {
                booking.setStatus(Booking.BookingStatus.CONFIRMED);
                bookingRepository.save(booking);
            }
        } else {
            payment.setStatus(Payment.PaymentStatus.FAILED);
            payment.setGatewayResponse("Payment failed - insufficient funds or invalid card details");
        }

        payment = paymentRepository.save(payment);
        
        log.info("Payment {} for booking {} with status: {}", 
                payment.getTransactionId(), booking.getBookingReference(), payment.getStatus());

        return mapToResponseDto(payment);
    }

    @Transactional(readOnly = true)
    public List<PaymentResponseDto> getUserPayments() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Payment> payments = paymentRepository.findByUserIdOrderByCreatedAtDesc(user.getId());
        return payments.stream().map(this::mapToResponseDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<PaymentResponseDto> getUserPayments(Pageable pageable) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Page<Payment> payments = paymentRepository.findByUserIdOrderByCreatedAtDesc(user.getId(), pageable);
        return payments.map(this::mapToResponseDto);
    }

    @Transactional(readOnly = true)
    public PaymentResponseDto getPaymentById(Long id) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        // Check if user owns this payment or is admin
        if (!payment.getUser().getId().equals(user.getId()) && 
            !user.getRole().equals(User.Role.ADMIN)) {
            throw new RuntimeException("Access denied");
        }

        return mapToResponseDto(payment);
    }

    @Transactional(readOnly = true)
    public PaymentResponseDto getPaymentByTransactionId(String transactionId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Payment payment = paymentRepository.findByTransactionId(transactionId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        // Check if user owns this payment or is admin
        if (!payment.getUser().getId().equals(user.getId()) && 
            !user.getRole().equals(User.Role.ADMIN)) {
            throw new RuntimeException("Access denied");
        }

        return mapToResponseDto(payment);
    }

    @Transactional(readOnly = true)
    public List<PaymentResponseDto> getBookingPayments(Long bookingId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        // Check if user owns this booking or is admin
        if (!booking.getUser().getId().equals(user.getId()) && 
            !user.getRole().equals(User.Role.ADMIN)) {
            throw new RuntimeException("Access denied");
        }

        List<Payment> payments = paymentRepository.findByBookingIdOrderByCreatedAtDesc(bookingId);
        return payments.stream().map(this::mapToResponseDto).collect(Collectors.toList());
    }

    public PaymentResponseDto processRefund(Long paymentId, BigDecimal refundAmount, String reason) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        if (payment.getStatus() != Payment.PaymentStatus.SUCCESS) {
            throw new RuntimeException("Can only refund successful payments");
        }

        if (refundAmount.compareTo(payment.getAmount()) > 0) {
            throw new RuntimeException("Refund amount cannot exceed original payment amount");
        }

        // Simulate refund processing
        boolean refundSuccess = simulateRefund(payment);

        if (refundSuccess) {
            payment.setRefundAmount(refundAmount);
            payment.setRefundDate(LocalDateTime.now());
            
            if (refundAmount.equals(payment.getAmount())) {
                payment.setStatus(Payment.PaymentStatus.REFUNDED);
            } else {
                payment.setStatus(Payment.PaymentStatus.PARTIAL_REFUND);
            }
            
            payment.setNotes((payment.getNotes() != null ? payment.getNotes() + "; " : "") + 
                           "Refund reason: " + reason);
        }

        payment = paymentRepository.save(payment);
        
        log.info("Refund processed for payment {} with amount: {}", 
                payment.getTransactionId(), refundAmount);

        return mapToResponseDto(payment);
    }

    // Admin methods
    @Transactional(readOnly = true)
    public Page<PaymentResponseDto> getAllPayments(Pageable pageable) {
        Page<Payment> payments = paymentRepository.findAll(pageable);
        return payments.map(this::mapToResponseDto);
    }

    @Transactional(readOnly = true)
    public List<PaymentResponseDto> getPaymentsByStatus(Payment.PaymentStatus status) {
        List<Payment> payments = paymentRepository.findByStatus(status);
        return payments.stream().map(this::mapToResponseDto).collect(Collectors.toList());
    }

    // Private helper methods
    private boolean simulatePaymentGateway(PaymentRequestDto requestDto) {
        // Simulate payment processing with random success/failure
        // In real implementation, this would integrate with actual payment gateway
        
        // Simulate network delay
        try {
            Thread.sleep(1000 + random.nextInt(2000)); // 1-3 seconds delay
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Simulate different failure scenarios
        if (requestDto.getPaymentMethod() == Payment.PaymentMethod.CREDIT_CARD) {
            // 90% success rate for credit cards
            return random.nextDouble() < 0.9;
        } else if (requestDto.getPaymentMethod() == Payment.PaymentMethod.UPI) {
            // 95% success rate for UPI
            return random.nextDouble() < 0.95;
        } else {
            // 85% success rate for other methods
            return random.nextDouble() < 0.85;
        }
    }

    private boolean simulateRefund(Payment payment) {
        // Simulate refund processing - usually has high success rate
        try {
            Thread.sleep(500 + random.nextInt(1000)); // 0.5-1.5 seconds delay
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        return random.nextDouble() < 0.98; // 98% success rate for refunds
    }

    private String generateTransactionId() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String randomNum = String.format("%04d", random.nextInt(10000));
        return "TXN" + timestamp + randomNum;
    }

    private PaymentResponseDto mapToResponseDto(Payment payment) {
        PaymentResponseDto dto = new PaymentResponseDto();
        dto.setId(payment.getId());
        dto.setTransactionId(payment.getTransactionId());
        dto.setUserId(payment.getUser().getId());
        dto.setUserFullName(payment.getUser().getFirstName() + " " + payment.getUser().getLastName());
        dto.setBookingId(payment.getBooking().getId());
        dto.setBookingReference(payment.getBooking().getBookingReference());
        dto.setAmount(payment.getAmount());
        dto.setPaymentType(payment.getPaymentType());
        dto.setPaymentMethod(payment.getPaymentMethod());
        dto.setStatus(payment.getStatus());
        dto.setPaymentDate(payment.getPaymentDate());
        dto.setGatewayReference(payment.getGatewayReference());
        dto.setRefundAmount(payment.getRefundAmount());
        dto.setRefundDate(payment.getRefundDate());
        dto.setNotes(payment.getNotes());
        dto.setCreatedAt(payment.getCreatedAt());
        return dto;
    }
}