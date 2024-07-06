package com.libraryManagement.demo.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {

    SERVER_ERROR(500, HttpStatus.INTERNAL_SERVER_ERROR, "Server error"),
    BAD_REQUEST(400, HttpStatus.BAD_REQUEST, "Bad Request"),
    RESOURCE_NOT_FOUND(404, HttpStatus.NOT_FOUND, "Resource not found"),
    CONFLICT(1000, HttpStatus.CONFLICT, "Conflicting Request");
    private final Integer errorCode;
    private final HttpStatus httpStatus;
    private final String message;
}
