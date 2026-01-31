package com.tenpo.prueba_tenpo.Client;

import com.tenpo.prueba_tenpo.Exception.ExternalServiceUnavailableException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.net.http.HttpClient;
import java.time.Duration;
import java.util.Map;

@Component
public class HttpPercentageClient implements PercentageClient {

    private final RestClient restClient;
    private final String endpoint;

    public HttpPercentageClient(
            @Value("${percentage.base-url}") String baseUrl,
            @Value("${percentage.endpoint}") String endpoint,
            @Value("${percentage.connect-timeout-ms}") long connectTimeoutMs,
            @Value("${percentage.read-timeout-ms}") long readTimeoutMs
    ) {
        this.endpoint = endpoint;

        HttpClient httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofMillis(connectTimeoutMs))
                .build();

        JdkClientHttpRequestFactory requestFactory = new JdkClientHttpRequestFactory(httpClient);
        requestFactory.setReadTimeout(Duration.ofMillis(readTimeoutMs));

        this.restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .requestFactory(requestFactory)
                .build();
    }

    @Override
    @Retryable(
            retryFor = ExternalServiceUnavailableException.class,
            maxAttempts = 3,
            backoff = @Backoff(delay = 200, multiplier = 2.0)
    )
    public double getPercentage() {
        try {
            Map<?, ?> resp = restClient.get()
                    .uri(endpoint)
                    .retrieve()
                    .body(Map.class);

            Object value = resp.get("percentage");
            if (value == null) throw new IllegalStateException("Missing 'percentage' field");
            return Double.parseDouble(value.toString());
        } catch (Exception ex) {
            throw new ExternalServiceUnavailableException("Percentage service unavailable", ex);
        }
    }
}
