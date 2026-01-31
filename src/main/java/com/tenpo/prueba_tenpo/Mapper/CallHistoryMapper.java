package com.tenpo.prueba_tenpo.Mapper;

import com.tenpo.prueba_tenpo.DTO.CallHistoryDto;
import com.tenpo.prueba_tenpo.Entity.CallHistoryEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CallHistoryMapper {
    CallHistoryDto toDto(CallHistoryEntity entity);
}
