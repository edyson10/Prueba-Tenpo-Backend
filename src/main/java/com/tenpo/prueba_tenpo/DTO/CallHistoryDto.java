package com.tenpo.prueba_tenpo.DTO;

import java.time.OffsetDateTime;

public record CallHistoryDto(
        Long id,
        OffsetDateTime timestamp,
        String method,
        String endpoint,
        String parameters,
        Integer httpStatus,
        String responseBody,
        String errorMessage
) {}