package com.libraryManagement.demo.exception;

import lombok.Getter;

@Getter
public class ApiException extends RuntimeException{
    private final ErrorCode errorCode;
    private final MessageKey messageKey;
    private final Object[] messageTokens;

    public ApiException(ErrorCode errorCode, MessageKey messageKey, Object... messageTokens) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.messageKey = messageKey;
        this.messageTokens = messageTokens;
    }
}
