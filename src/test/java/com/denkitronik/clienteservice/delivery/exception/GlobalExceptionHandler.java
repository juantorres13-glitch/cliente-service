
package com.denkitronik.clienteservice.delivery.exception;

import com.denkitronik.clienteservice.domain.exception.ClienteNotFoundException;
import com.denkitronik.clienteservice.domain.exception.ClienteServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ClienteNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleClienteNotFound(
            ClienteNotFoundException ex) {

        ApiErrorResponse respuesta = new ApiErrorResponse(
                404,
                "Not Found",
                ex.getMessage()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(respuesta);
    }

    @ExceptionHandler(ClienteServiceException.class)
    public ResponseEntity<ApiErrorResponse> handleClienteServiceException(
            ClienteServiceException ex) {

        ApiErrorResponse respuesta = new ApiErrorResponse(
                500,
                "Internal Server Error",
                ex.getMessage()
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(respuesta);
    }
}
