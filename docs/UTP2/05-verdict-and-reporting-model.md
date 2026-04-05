# Verdict and Reporting Model

## Verdict Model

The repository currently maps test outcomes through TestNG and reporting listeners. In UTP2-oriented terms, verdict assignment is conservative and evidence-driven.

| Verdict | Current Mapping | Notes |
|---|---|---|
| `pass` | Assertions succeed and TestNG marks the test as passed | Implemented |
| `fail` | Assertion or runtime error causes TestNG failure | Implemented |
| `skipped` | TestNG skip, including explicit placeholder skips | Implemented |
| `inconclusive` | Reserved modeling value | Not emitted as a first-class runtime status today |

## Verdict Sources

| Source | Repository Element | Role |
|---|---|---|
| UI assertions | `UiAssertions` | Assign business-facing UI verdicts |
| API assertions | `ApiAssertions` | Assign protocol and payload verdicts |
| Runtime exceptions | Selenium, RestAssured, framework code | Escalate technical failures |
| TestNG lifecycle | Test execution engine | Final runtime status integration |

## Evidence Model

| Evidence Type | Produced By | Current Status |
|---|---|---|
| Screenshot attachment | `AllureTestListener` + `ScreenshotService` | Implemented for UI failure paths |
| API request attachment | `AutomationExerciseApiClient` | Implemented |
| API response attachment | `AutomationExerciseApiClient` | Implemented |
| Allure result files | Allure TestNG integration | Implemented |
| Surefire XML and text reports | Maven Surefire | Implemented |

## Evidence Flow

1. The suite starts with a resolved `TestContext`.
2. Test procedures execute against the SUT.
3. Assertions and runtime hooks assign pass, fail, or skipped outcomes.
4. Allure attachments preserve step-level evidence.
5. Surefire and Allure raw files persist the final execution trace.

## Reporting Notes

- The framework already emits portable `allure-results`.
- Explicit placeholder skips are preferable to flaky false negatives and should remain visible in reports.
- V1 can add richer metadata and trace links without changing the core result flow.
