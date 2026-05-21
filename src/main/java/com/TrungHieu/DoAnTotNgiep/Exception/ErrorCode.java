package com.TrungHieu.DoAnTotNgiep.Exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter

public enum ErrorCode {
    // --- System & General Errors (9xxx) ---
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY(8888, "Invalid message key", HttpStatus.BAD_REQUEST),

    // --- Authentication & Authorization (1xxx) ---
    UNAUTHENTICATED(1001, "Login session expired or does not exist", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1002, "You do not have permission to access this feature", HttpStatus.FORBIDDEN),
    PASSWORD_INCORRECT(1003, "Incorrect old password", HttpStatus.BAD_REQUEST),
    TOKEN_INVALID(1004, "Invalid token", HttpStatus.UNAUTHORIZED),
    INVALID_ROLE(1011, "The user's role is incorrect for this operation", HttpStatus.BAD_REQUEST),

    // --- Data & Conflict Errors (2xxx) ---
    DATA_ALREADY_EXISTS(2001, "Data already exists in the system", HttpStatus.CONFLICT),
    DATA_NOT_FOUND(2002, "Requested data not found", HttpStatus.NOT_FOUND),
    INVALID_FORMAT(2003, "Invalid data format", HttpStatus.BAD_REQUEST),

    // --- Student & Class Business Logic (3xxx) ---
    CLASS_REQUIRED_FOR_STUDENT(3001, "Student must be assigned to a class", HttpStatus.UNPROCESSABLE_ENTITY),
    EXCEEDS_QUANTITY_LIMIT(3002, "Quantity limit exceeded", HttpStatus.BAD_REQUEST),
    GRADING_PERIOD_ENDED(3003, "Exam is expired", HttpStatus.LOCKED),

    // ---Grading Session Buisness Logic (4xxx) ---
    SESSION_ALREADY_FINALIZED(4001, "Grading session is finalized", HttpStatus.CONFLICT),

    // -- AI Service Logs
    AI_SERVICE_ERROR(5000, "Setting is not correctly", HttpStatus.BAD_REQUEST)
    ;

    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;

    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }
}
