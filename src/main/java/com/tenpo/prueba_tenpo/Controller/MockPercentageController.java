package com.tenpo.prueba_tenpo.Controller;

import com.tenpo.prueba_tenpo.DTO.CalculationResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/mock")
public class MockPercentageController {

    @Operation(summary = "Obtiene el porcentaje mock", description = "Simula el servicio externo que retorna un porcentaje.")
    @ApiResponse(responseCode = "200",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = CalculationResponseDto.class),
                    examples = @ExampleObject(value = """
                                {
                                    "percentage": 10.0
                                }
                            """)))
    @GetMapping(value = "/percentage", produces = MediaType.APPLICATION_JSON_VALUE)
    /*
     * Puedes simular fallo: /mock/percentage?fail=true
     */
    public ResponseEntity<Map<String, Object>> percentage(@RequestParam(defaultValue = "false") boolean fail) {
        if (fail) {
            return ResponseEntity.status(503).body(Map.of("error", "mock failure"));
        }
        return ResponseEntity.ok(Map.of("percentage", 10.0));
    }
}
