package com.libraryManagement.demo.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class ApiExceptionDTO{
    private Integer errorCode;
    private HttpStatus httpStatus;
    private String message;
    private String detailedMessage;

    public ApiExceptionDTO(ErrorCode errorCode, String detailedMessage) {
        this.errorCode=errorCode.getErrorCode();
        this.httpStatus=errorCode.getHttpStatus();
        this.message=errorCode.getMessage();
        this.detailedMessage=detailedMessage;
    }
}
