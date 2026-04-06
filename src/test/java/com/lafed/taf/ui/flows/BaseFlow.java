package com.lafed.taf.ui.flows;

import com.lafed.taf.core.state.StateStore;
import java.util.Objects;

/**
 * Shared parent for future UI flows.
 */
public abstract class BaseFlow {

    protected final StateStore stateStore;

    protected BaseFlow(StateStore stateStore) {
        this.stateStore = Objects.requireNonNull(stateStore, "stateStore");
    }
}
