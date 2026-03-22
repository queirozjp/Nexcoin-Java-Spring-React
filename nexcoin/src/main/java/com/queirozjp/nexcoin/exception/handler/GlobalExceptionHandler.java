package com.queirozjp.nexcoin.exception.handler;
import com.queirozjp.nexcoin.exception.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@ControllerAdvice
@RestController
public class GlobalExceptionHandler {
    @ExceptionHandler(InsufficientBalanceException.class)
    public final ResponseEntity<ExceptionResponse>
    handleInsufficientBalanceException(InsufficientBalanceException ex,
                                       WebRequest request){
        ExceptionResponse response = new ExceptionResponse(
                new Date(),
                ex.getMessage(),
                request.getDescription(false)
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

    }
    @ExceptionHandler(SelfTransferException.class)
    public final ResponseEntity<ExceptionResponse>
    handleSelfTransferException(SelfTransferException ex,
                                       WebRequest request){
        ExceptionResponse response = new ExceptionResponse(
                new Date(),
                ex.getMessage(),
                request.getDescription(false)
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

    }
    @ExceptionHandler(ToAddressNotFoundException.class)
    public final ResponseEntity<ExceptionResponse>
    handleToAddressNotFoundException(ToAddressNotFoundException ex,
                                WebRequest request){
        ExceptionResponse response = new ExceptionResponse(
                new Date(),
                ex.getMessage(),
                request.getDescription(false)
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

    }
    @ExceptionHandler(EmailAlreadyExistsException.class)
    public final ResponseEntity<ExceptionResponse>
    handleEmailAlreadyExistsException(EmailAlreadyExistsException ex,
                                      WebRequest request){

        ExceptionResponse response = new ExceptionResponse(
                new Date(),
                ex.getMessage(),
                request.getDescription(false)
        );

        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }
    @ExceptionHandler(InvalidPasswordException.class)
    public final ResponseEntity<ExceptionResponse>
    handleInvalidPasswordException(InvalidPasswordException ex,
                                   WebRequest request){

        ExceptionResponse response = new ExceptionResponse(
                new Date(),
                ex.getMessage(),
                request.getDescription(false)
        );

        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<String> handleIllegalStateException(IllegalStateException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }


}

