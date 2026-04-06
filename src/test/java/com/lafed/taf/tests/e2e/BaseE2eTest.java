package com.lafed.taf.tests.e2e;

import com.lafed.taf.core.http.RequestSpecFactory;
import com.lafed.taf.tests.ui.BaseUiTest;

/**
 * Shared E2E test scaffold combining future UI and API capabilities.
 */
public abstract class BaseE2eTest extends BaseUiTest {

    protected RequestSpecFactory requestSpecFactory;

    protected void initializeE2eContext() {
        initializeUiContext();
        this.requestSpecFactory = new RequestSpecFactory();
    }
}
