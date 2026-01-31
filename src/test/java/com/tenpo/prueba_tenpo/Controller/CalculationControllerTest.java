package com.tenpo.prueba_tenpo.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tenpo.prueba_tenpo.DTO.CalculationRequestDto;
import com.tenpo.prueba_tenpo.DTO.CalculationResponseDto;
import com.tenpo.prueba_tenpo.Exception.RestExceptionHandler;
import com.tenpo.prueba_tenpo.Service.CalculationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = CalculationController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(RestExceptionHandler.class)
class CalculationControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    CalculationService calculationService;


    @Test
    void calculate_ok_200() throws Exception {

        CalculationResponseDto resp =
                new CalculationResponseDto(
                        10.0,
                        5.0,
                        15.0,
                        10.0,
                        16.5
                );

        when(calculationService.calculate(any())).thenReturn(resp);

        String json = """
                {
                  "num1": 10,
                  "num2": 5
                }
                """;

        mockMvc.perform(post("/api/v1/calculate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.sum").value(15.0))
                .andExpect(jsonPath("$.percentageApplied").value(10.0))
                .andExpect(jsonPath("$.result").value(16.5));
    }
}
