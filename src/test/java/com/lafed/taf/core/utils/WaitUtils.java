package com.lafed.taf.core.utils;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;
import java.util.function.BooleanSupplier;

/**
 * Generic wait helpers with no page-specific stabilization logic.
 */
public final class WaitUtils {

    public void sleep(Duration duration) {
        try {
            Thread.sleep(duration.toMillis());
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Thread interrupted while waiting", exception);
        }
    }

    public boolean poll(BooleanSupplier condition, Duration timeout, Duration interval) {
        Objects.requireNonNull(condition, "condition");
        Instant deadline = Instant.now().plus(timeout);

        while (Instant.now().isBefore(deadline)) {
            if (condition.getAsBoolean()) {
                return true;
            }
            sleep(interval);
        }

        return condition.getAsBoolean();
    }
}
