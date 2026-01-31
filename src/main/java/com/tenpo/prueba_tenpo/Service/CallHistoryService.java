package com.tenpo.prueba_tenpo.Service;

public interface CallHistoryService {
    void saveAsync(String method, String endpoint, String parameters,
                   Integer status, String responseBody, String errorMessage);
}
