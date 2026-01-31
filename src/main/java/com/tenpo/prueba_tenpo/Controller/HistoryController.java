package com.tenpo.prueba_tenpo.Controller;

import com.tenpo.prueba_tenpo.DTO.CallHistoryDto;
import com.tenpo.prueba_tenpo.DTO.ErrorResponseDto;
import com.tenpo.prueba_tenpo.Mapper.CallHistoryMapper;
import com.tenpo.prueba_tenpo.Repository.CallHistoryRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/history")
@Tag(name = "History", description = "Consulta del historial de llamadas al API")
public class HistoryController {

    private final CallHistoryRepository repository;
    private final CallHistoryMapper mapper;

    public HistoryController(CallHistoryRepository repository, CallHistoryMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Operation(
            summary = "Obtiene el historial paginado",
            description = "Retorna el historial de llamadas registradas (paginado). Se recomienda ordenar por timestamp desc."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Page.class)))
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Page<CallHistoryDto> list(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toDto);
    }
}