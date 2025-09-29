package com.rydio.payment.dto;

import com.rydio.payment.entity.Payment;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PaymentResponseDto {
    private Long id;
    private String transactionId;
    private Long userId;
    private String userFullName;
    private Long bookingId;
    private String bookingReference;
    private BigDecimal amount;
    private Payment.PaymentType paymentType;
    private Payment.PaymentMethod paymentMethod;
    private Payment.PaymentStatus status;
    private LocalDateTime paymentDate;
    private String gatewayReference;
    private BigDecimal refundAmount;
    private LocalDateTime refundDate;
    private String notes;
    private LocalDateTime createdAt;
}