package com.lafed.taf.ui.components;

import java.util.Objects;
import org.openqa.selenium.WebDriver;

/**
 * Marker base class for reusable UI fragments.
 */
public abstract class BaseComponent {

    protected final WebDriver driver;

    protected BaseComponent(WebDriver driver) {
        this.driver = Objects.requireNonNull(driver, "driver");
    }
}
