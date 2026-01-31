package com.tenpo.prueba_tenpo.DTO;

import jakarta.validation.constraints.NotNull;

public record CalculationRequestDto(
        @NotNull Double num1,
        @NotNull Double num2
) {}
