# Testing Documentation

This document outlines the testing strategy, tools, and test cases used in the project.


# Testing Strategy

We follow a **hybrid testing approach** for our backend, combining **unit tests** with **integration-style tests** to ensure robustness at every layer of the system.

## Unit Tests (UT)
- Focus on testing individual service classes and logic in isolation, ensuring that each component performs as expected under normal and edge-case scenarios.
- We use **JUnit** for test execution and **Mockito** for mocking dependencies.

## Integration Tests (IT)
- Instead of traditional integration tests that involve a real backend or database, we employ **MockWebServer** to simulate real server responses and test how our components interact with the API. This allows us to:
  - Test interactions between the service layer and external services.
  - Simulate different server responses, such as success and failure cases, without needing to set up a real server.
  - Mock network failures and test error handling scenarios in a controlled environment.

By integrating **MockWebServer**, we can perform high-level tests for the communication between different layers of the system (e.g., API calls, HTTP status codes, etc.), making sure the components work together as intended.

## System Tests (ST)
- End-to-end tests using **Playwright** simulate user behavior by interacting with the actual UI and ensuring the system performs as expected from the user's perspective. This layer is essential for validating overall functionality and user interactions.


This hybrid approach ensures that we thoroughly test individual components (unit tests), the integration of those components (MockWebServer-based tests), and the full system (system tests with Playwright) to guarantee high-quality, reliable software.


## Tools Used

| Layer         | Tool(s)                    |
|---------------|----------------------------|
| Unit Tests    | JUnit 5, Mockito           |
| Integration   | JUnit 5, MockWebServer     |
| System Tests  | Playwright (TyprScript)    |


## How to Run Tests

### Unit & Integration Tests
```bash
mvn clean test
```

### System Tests

