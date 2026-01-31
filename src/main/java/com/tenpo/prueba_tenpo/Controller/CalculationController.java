package com.tenpo.prueba_tenpo.Controller;

import com.tenpo.prueba_tenpo.DTO.CalculationRequestDto;
import com.tenpo.prueba_tenpo.DTO.CalculationResponseDto;
import com.tenpo.prueba_tenpo.DTO.ErrorResponseDto;
import com.tenpo.prueba_tenpo.Service.CalculationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Calculation", description = "Endpoints para realizar el cálculo con porcentaje externo")
public class CalculationController {

    private final CalculationService calculationService;

    public CalculationController(CalculationService calculationService) {
        this.calculationService = calculationService;
    }

    @Operation(
            summary = "Realiza el cálculo y aplica porcentaje",
            description = "Recibe num1 y num2, calcula la suma y aplica un porcentaje obtenido desde un servicio externo (con retry)."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CalculationResponseDto.class),
                            examples = @ExampleObject(value = """
                                {"num1":10.0,"num2":5.0,"sum":15.0,"percentageApplied":10.0,"result":16.5}
                            """))),
            @ApiResponse(responseCode = "400", description = "Request inválido",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "429", description = "Rate limit excedido",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "503", description = "Servicio externo no disponible",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @PostMapping(value = "/calculate", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CalculationResponseDto> calculate(@Valid @RequestBody CalculationRequestDto request) {
        return ResponseEntity.ok(calculationService.calculate(request));
    }
}
