package com.tenpo.prueba_tenpo.Service.Impl;

import com.tenpo.prueba_tenpo.Client.PercentageClient;
import com.tenpo.prueba_tenpo.DTO.CalculationRequestDto;
import com.tenpo.prueba_tenpo.DTO.CalculationResponseDto;
import com.tenpo.prueba_tenpo.Service.CalculationService;
import org.springframework.stereotype.Service;

@Service
public class CalculationServiceImpl implements CalculationService {

    private final PercentageClient percentageClient;

    public CalculationServiceImpl(PercentageClient percentageClient) {
        this.percentageClient = percentageClient;
    }

    @Override
    public CalculationResponseDto calculate(CalculationRequestDto req) {
        double sum = req.num1() + req.num2();
        double pct = percentageClient.getPercentage();
        double result = sum + (sum * (pct / 100.0));

        return new CalculationResponseDto(
                req.num1(),
                req.num2(),
                sum,
                pct,
                result
        );
    }
}