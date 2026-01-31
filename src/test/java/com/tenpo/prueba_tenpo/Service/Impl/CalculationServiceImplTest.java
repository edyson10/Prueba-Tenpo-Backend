package com.tenpo.prueba_tenpo.Service.Impl;

import com.tenpo.prueba_tenpo.Entity.CallHistoryEntity;
import com.tenpo.prueba_tenpo.Repository.CallHistoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.OffsetDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

class CalculationServiceImplTest {

    private CallHistoryRepository repository;
    private CallHistoryServiceImpl service;

    @BeforeEach
    void setUp() {
        repository = mock(CallHistoryRepository.class);
        service = new CallHistoryServiceImpl(repository);
    }

    @Test
    void saveAsync_shouldBuildEntityAndPersistIt() {

        String method = "GET";
        String endpoint = "/api/v1/history";
        String parameters = "page=0&size=10";
        Integer status = 200;
        String responseBody = "{\"ok\":true}";
        String errorMessage = null;

        service.saveAsync(method, endpoint, parameters, status, responseBody, errorMessage);

        ArgumentCaptor<CallHistoryEntity> captor = ArgumentCaptor.forClass(CallHistoryEntity.class);
        verify(repository, times(1)).save(captor.capture());

        CallHistoryEntity saved = captor.getValue();

        assertThat(saved).isNotNull();
        assertThat(saved.getTimestamp()).isNotNull();

        OffsetDateTime now = OffsetDateTime.now();
        assertThat(saved.getTimestamp()).isBeforeOrEqualTo(now);
        assertThat(saved.getTimestamp()).isAfter(now.minusMinutes(1));

        assertThat(saved.getMethod()).isEqualTo(method);
        assertThat(saved.getEndpoint()).isEqualTo(endpoint);
        assertThat(saved.getParameters()).isEqualTo(parameters);
        assertThat(saved.getHttpStatus()).isEqualTo(status);
        assertThat(saved.getResponseBody()).isEqualTo(responseBody);
        assertThat(saved.getErrorMessage()).isEqualTo(errorMessage);
    }

    @Test
    void saveAsync_shouldPersistEvenWhenErrorMessageIsPresent() {

        String method = "POST";
        String endpoint = "/api/v1/calculate";
        String parameters = "{\"num1\":10,\"num2\":5}";
        Integer status = 500;
        String responseBody = "{\"message\":\"fail\"}";
        String errorMessage = "Some error";

        service.saveAsync(method, endpoint, parameters, status, responseBody, errorMessage);

        ArgumentCaptor<CallHistoryEntity> captor = ArgumentCaptor.forClass(CallHistoryEntity.class);
        verify(repository).save(captor.capture());

        CallHistoryEntity saved = captor.getValue();
        assertThat(saved.getErrorMessage()).isEqualTo(errorMessage);
        assertThat(saved.getHttpStatus()).isEqualTo(status);
    }
}