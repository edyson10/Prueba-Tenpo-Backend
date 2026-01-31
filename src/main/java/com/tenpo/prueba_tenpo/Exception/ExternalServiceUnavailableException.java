package com.tenpo.prueba_tenpo.Exception;

public class ExternalServiceUnavailableException extends RuntimeException {
    public ExternalServiceUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }
    public ExternalServiceUnavailableException(String message) {
        super(message);
    }
}
