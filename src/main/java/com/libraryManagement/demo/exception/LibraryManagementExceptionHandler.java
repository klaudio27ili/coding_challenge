package com.libraryManagement.demo.exception;

import com.libraryManagement.demo.services.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@RequiredArgsConstructor
public class LibraryManagementExceptionHandler {
    private final MessageService messageService;

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<ApiExceptionDTO> handleException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiExceptionDTO(ErrorCode.SERVER_ERROR, e.getMessage()));
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiExceptionDTO> handleApiException(ApiException e){ return respond(e);}

    private ResponseEntity<ApiExceptionDTO> respond(ApiException e) {
        return ResponseEntity
                .status(e.getErrorCode().getHttpStatus())
                .body(new ApiExceptionDTO(e.getErrorCode(), messageService.get(e.getMessageKey().getKey(), e.getMessageTokens())));
    }
}
