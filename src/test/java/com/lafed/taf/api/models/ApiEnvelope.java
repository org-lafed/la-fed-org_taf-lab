package com.lafed.taf.api.models;

/**
 * Minimal generic payload wrapper for future API contracts.
 */
public record ApiEnvelope(String status, String message) {
}
