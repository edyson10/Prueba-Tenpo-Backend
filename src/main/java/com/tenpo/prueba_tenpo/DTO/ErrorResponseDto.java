package com.tenpo.prueba_tenpo.DTO;

import java.time.OffsetDateTime;

public record ErrorResponseDto(
        OffsetDateTime timestamp,
        int status,
        String error,
        String message,
        String path
) {}
