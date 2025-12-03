package com.learn.enlin.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import lombok.Getter;

@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY(1001, "Uncategorized error", HttpStatus.BAD_REQUEST),
    USER_EXISTED(1002, "User existed", HttpStatus.BAD_REQUEST),
    USERNAME_INVALID(1003, "Username must be at least {min} characters", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD(1004, "Password must be at least {min} characters", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(1005, "User not existed", HttpStatus.NOT_FOUND),
    UNAUTHENTICATED(1006, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1007, "You do not have permission", HttpStatus.FORBIDDEN),
    INVALID_DOB(1008, "Your age must be at least {min}", HttpStatus.BAD_REQUEST),
    MOVIE_NOT_EXISTED(1009, "Payment not existed", HttpStatus.NOT_FOUND),
    SEAT_NOT_EXISTED(1010, "Seat not existed", HttpStatus.NOT_FOUND),
    ROOM_NOT_FOUND(1011, "Room not found", HttpStatus.NOT_FOUND),
    SHOWTIME_NOT_EXISTED(1012, "Showtime not existed", HttpStatus.NOT_FOUND),
    BOOKING_NOT_EXISTED(1013, "Booking not existed", HttpStatus.NOT_FOUND),
    INVALID_TICKETS(1014, "Invalid tickets", HttpStatus.BAD_REQUEST),
    INVALID_ORDER_ITEMS(1015, "Invalid order items", HttpStatus.BAD_REQUEST),
    INVALID_PAYMENT_METHOD(1016, "Invalid payment method", HttpStatus.BAD_REQUEST),
    SEAT_ALREADY_BOOKED(1017, "Seat already booked", HttpStatus.BAD_REQUEST),
    COMBO_NOT_EXISTED(1018, "Combo not existed", HttpStatus.NOT_FOUND),
    PAYMENT_NOT_PENDING(1019, "Payment already completed", HttpStatus.BAD_REQUEST),
    PAYMENT_ALREADY_FAILED(1020, "Payment already failed", HttpStatus.BAD_REQUEST),
    PAYMENT_CANCELLATION_FAILED(1021, "Payment cancellation failed", HttpStatus.BAD_REQUEST),
    INVALID_PAYMENT_AMOUNT(1022, "Invalid payment amount", HttpStatus.BAD_REQUEST),
    PAYMENT_FAILED(1023, "Payment failed", HttpStatus.BAD_REQUEST),
    ;

    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;
}
