package com.lafed.taf.core.utils;

import com.lafed.taf.config.ExecutionConfig;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

/**
 * Tolerant mitigation layer for third-party overlays and ad-driven click interception.
 */
public class UiInterferenceGuard {
    private final WebDriver driver;

    public UiInterferenceGuard(WebDriver driver, ExecutionConfig config) {
        this.driver = driver;
    }

    public void afterNavigation() {
        if (clearGoogleVignette()) {
            // The page was reloaded without the vignette fragment; continue with the standard cleanup pass.
        }
        dismissVisibleCloseControls();
        hideKnownInterference();
    }

    public void beforeInteraction() {
        dismissVisibleCloseControls();
        hideKnownInterference();
    }

    public void recoverFromInterference() {
        beforeInteraction();
    }

    private boolean clearGoogleVignette() {
        String currentUrl = driver.getCurrentUrl();
        if (currentUrl == null || !currentUrl.contains("#google_vignette")) {
            return false;
        }

        driver.navigate().to(currentUrl.replace("#google_vignette", ""));
        return true;
    }

    private void dismissVisibleCloseControls() {
        try {
            ((JavascriptExecutor) driver).executeScript(
                    "const labels = new Set(['close', 'dismiss', 'skip ad', 'skip ads', 'fermer']);"
                            + "const isVisible = (element) => {"
                            + "  const style = window.getComputedStyle(element);"
                            + "  const rect = element.getBoundingClientRect();"
                            + "  return style.visibility !== 'hidden'"
                            + "    && style.display !== 'none'"
                            + "    && rect.width > 0"
                            + "    && rect.height > 0;"
                            + "};"
                            + "const candidates = Array.from(document.querySelectorAll('button, a, [role=\"button\"]'));"
                            + "for (const candidate of candidates) {"
                            + "  const label = ((candidate.innerText || '') + ' ' + (candidate.getAttribute('aria-label') || '') + ' ' + (candidate.getAttribute('title') || ''))"
                            + "    .trim()"
                            + "    .toLowerCase();"
                            + "  if (!label) {"
                            + "    continue;"
                            + "  }"
                            + "  const normalized = label.replace(/\\s+/g, ' ');"
                            + "  const container = candidate.closest('[role=\"dialog\"], .modal, [class*=\"overlay\"], [id*=\"overlay\"], [class*=\"popup\"], [id*=\"popup\"]');"
                            + "  const style = window.getComputedStyle(candidate);"
                            + "  const fixedLike = style.position === 'fixed' || (container && ['fixed', 'sticky', 'absolute'].includes(window.getComputedStyle(container).position));"
                            + "  if (labels.has(normalized) && isVisible(candidate) && (fixedLike || container)) {"
                            + "    candidate.click();"
                            + "  }"
                            + "}");
        } catch (RuntimeException ignored) {
            // The page can keep moving even when no dismissible control is available.
        }
    }

    private void hideKnownInterference() {
        try {
            ((JavascriptExecutor) driver).executeScript(
                    "const selectors = ["
                            + "  \"iframe[src*='googleads']\","
                            + "  \"iframe[id^='aswift_']\","
                            + "  \"iframe[name^='aswift_']\","
                            + "  \"[id*='google_ads_iframe']\","
                            + "  \"ins.adsbygoogle\","
                            + "  \".google-auto-placed\","
                            + "  \"[class*='adsbygoogle']\","
                            + "  \"[class*='google-auto-placed']\""
                            + "];"
                            + "const hide = (element) => {"
                            + "  if (!element || element === document.body || element === document.documentElement) {"
                            + "    return;"
                            + "  }"
                            + "  element.style.setProperty('display', 'none', 'important');"
                            + "  element.style.setProperty('visibility', 'hidden', 'important');"
                            + "  element.style.setProperty('pointer-events', 'none', 'important');"
                            + "  element.style.setProperty('opacity', '0', 'important');"
                            + "};"
                            + "const maybeHideContainer = (element) => {"
                            + "  let current = element;"
                            + "  for (let i = 0; i < 3 && current; i += 1) {"
                            + "    const style = window.getComputedStyle(current);"
                            + "    const rect = current.getBoundingClientRect();"
                            + "    const coversViewport = rect.width >= window.innerWidth * 0.4 && rect.height >= window.innerHeight * 0.25;"
                            + "    const highPriority = ['fixed', 'sticky', 'absolute'].includes(style.position) || Number(style.zIndex || 0) > 999;"
                            + "    const namedOverlay = /overlay|popup|interstitial|vignette|advert|ads/.test((current.id + ' ' + current.className).toLowerCase());"
                            + "    if (highPriority || coversViewport || namedOverlay) {"
                            + "      hide(current);"
                            + "    }"
                            + "    current = current.parentElement;"
                            + "  }"
                            + "};"
                            + "selectors.forEach((selector) => {"
                            + "  document.querySelectorAll(selector).forEach((element) => {"
                            + "    hide(element);"
                            + "    maybeHideContainer(element.parentElement);"
                            + "  });"
                            + "});"
                            + "Array.from(document.querySelectorAll('div, section, aside')).forEach((element) => {"
                            + "  const style = window.getComputedStyle(element);"
                            + "  const rect = element.getBoundingClientRect();"
                            + "  const namedOverlay = /overlay|popup|interstitial|vignette|advert|ads/.test((element.id + ' ' + element.className).toLowerCase());"
                            + "  const blocksViewport = ['fixed', 'sticky'].includes(style.position)"
                            + "    && Number(style.zIndex || 0) > 999"
                            + "    && rect.width >= window.innerWidth * 0.6"
                            + "    && rect.height >= window.innerHeight * 0.3;"
                            + "  if (namedOverlay || blocksViewport) {"
                            + "    hide(element);"
                            + "  }"
                            + "});");
        } catch (RuntimeException ignored) {
            // Best-effort cleanup should never fail the test flow on its own.
        }
    }
}
