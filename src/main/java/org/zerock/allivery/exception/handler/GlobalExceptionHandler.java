package org.zerock.allivery.exception.handler;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.zerock.allivery.exception.CQrAuthenticationFailedException;
import org.zerock.allivery.exception.CQrNotFoundException;
import org.zerock.allivery.exception.CUserNotFoundException;

@Log4j2
@RestControllerAdvice //모든 컨트롤러에서 발생하는 exception 처리
public class GlobalExceptionHandler {

    @ExceptionHandler(CQrAuthenticationFailedException.class)
    public ResponseEntity<ErrorResponse> handleCQrAuthenticationFailedException(CQrAuthenticationFailedException ex){
        ErrorResponse response = new ErrorResponse(ex.getErrorCode());
        response.setMessage(ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.valueOf(ex.getErrorCode().getStatus()));
    }

    @ExceptionHandler(CQrNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleCQrNotFoundException(CQrNotFoundException ex){
        ErrorResponse response = new ErrorResponse(ex.getErrorCode());
        response.setMessage(ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.valueOf(ex.getErrorCode().getStatus()));
    }

    @ExceptionHandler(CUserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleCUserNotFoundException(CUserNotFoundException ex){
        ErrorResponse response = new ErrorResponse(ex.getErrorCode());
        response.setMessage(ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.valueOf(ex.getErrorCode().getStatus()));
    }
}
