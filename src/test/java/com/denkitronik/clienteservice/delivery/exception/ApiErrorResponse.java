
package com.denkitronik.clienteservice.delivery.exception;

import java.time.LocalDateTime;

public record ApiErrorResponse(
        int status,
        String error,
        String mensaje,
        LocalDateTime timestamp
) {
    public ApiErrorResponse(int status, String error, String mensaje) {
        this(status, error, mensaje, LocalDateTime.now());
    }
}
