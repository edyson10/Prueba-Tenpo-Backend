package com.tenpo.prueba_tenpo.Service.Impl;

import com.tenpo.prueba_tenpo.Entity.CallHistoryEntity;
import com.tenpo.prueba_tenpo.Repository.CallHistoryRepository;
import com.tenpo.prueba_tenpo.Service.CallHistoryService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

@Service
public class CallHistoryServiceImpl implements CallHistoryService {

    private final CallHistoryRepository repository;

    public CallHistoryServiceImpl(CallHistoryRepository repository) {
        this.repository = repository;
    }

    @Async
    @Override
    public void saveAsync(String method, String endpoint, String parameters,
                          Integer status, String responseBody, String errorMessage) {

        CallHistoryEntity e = new CallHistoryEntity();
        e.setTimestamp(OffsetDateTime.now());
        e.setMethod(method);
        e.setEndpoint(endpoint);
        e.setParameters(parameters);
        e.setHttpStatus(status);
        e.setResponseBody(responseBody);
        e.setErrorMessage(errorMessage);
        repository.save(e);
    }
}
