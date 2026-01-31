package com.tenpo.prueba_tenpo.Filter;

import com.tenpo.prueba_tenpo.Service.CallHistoryService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
public class CallHistoryLoggingFilter extends OncePerRequestFilter {

    private final CallHistoryService historyService;

    public CallHistoryLoggingFilter(CallHistoryService historyService) {
        this.historyService = historyService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String endpoint = request.getRequestURI();

        if (shouldSkip(endpoint)) {
            filterChain.doFilter(request, response);
            return;
        }

        ContentCachingRequestWrapper req = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper res = new ContentCachingResponseWrapper(response);

        String errorMessage = null;

        try {
            filterChain.doFilter(req, res);
        } catch (Exception ex) {
            errorMessage = ex.getMessage();
            throw ex;
        } finally {
            String method = request.getMethod();
            String parameters = buildParams(request, req);
            Integer status = res.getStatus();
            String responseBody = extractResponseBody(res);

            historyService.saveAsync(method, endpoint, parameters, status, responseBody, errorMessage);

            res.copyBodyToResponse();
        }
    }

    private boolean shouldSkip(String endpoint) {
        if (endpoint == null) return false;

        if (endpoint.startsWith("/swagger-ui")) return true;
        if (endpoint.startsWith("/v3/api-docs")) return true;
        if (endpoint.equals("/swagger-ui.html")) return true;

        if (endpoint.startsWith("/webjars")) return true;
        if (endpoint.startsWith("/favicon")) return true;

        if (endpoint.startsWith("/api/v1/history")) return true;

        return false;
    }

    private String buildParams(HttpServletRequest request, ContentCachingRequestWrapper req) {
        String query = request.getQueryString();
        if (query != null && !query.isBlank()) return query;

        byte[] buf = req.getContentAsByteArray();
        if (buf.length == 0) return "";
        return new String(buf, StandardCharsets.UTF_8);
    }

    private String extractResponseBody(ContentCachingResponseWrapper res) {
        String ct = res.getContentType();
        if (ct == null) return "";
        if (!ct.contains(MediaType.APPLICATION_JSON_VALUE) && !ct.contains("text")) return "";

        byte[] buf = res.getContentAsByteArray();
        if (buf.length == 0) return "";
        return new String(buf, StandardCharsets.UTF_8);
    }
}
