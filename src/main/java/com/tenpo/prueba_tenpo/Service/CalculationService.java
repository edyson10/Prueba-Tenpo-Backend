package com.tenpo.prueba_tenpo.Service;

import com.tenpo.prueba_tenpo.DTO.CalculationRequestDto;
import com.tenpo.prueba_tenpo.DTO.CalculationResponseDto;

public interface CalculationService {
    CalculationResponseDto calculate(CalculationRequestDto req);
}
