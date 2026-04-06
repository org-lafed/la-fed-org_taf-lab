package com.lafed.taf.api.services;

import com.lafed.taf.api.clients.BaseApiClient;
import java.util.Objects;

/**
 * Shared parent for future service-oriented API abstractions.
 */
public abstract class ApiServiceSupport {

    protected final BaseApiClient apiClient;

    protected ApiServiceSupport(BaseApiClient apiClient) {
        this.apiClient = Objects.requireNonNull(apiClient, "apiClient");
    }
}
