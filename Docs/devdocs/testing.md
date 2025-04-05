# Testing Documentation

This document outlines the testing strategy, tools, and test cases used in the project.

## Testing Strategy

We follow a **hybrid testing approach** for our backend, combining **unit tests** with **integration-style tests** to ensure robustness at every layer of the system. We also use **Playwright** for **end-to-end testing** to ensure the frontend and backend work together.

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

## Tools/Frameworks Used

| Layer         | Tool(s)                    |
|---------------|----------------------------|
| Unit Tests    | JUnit 5, Mockito           |
| Integration   | JUnit 5, MockWebServer     |
| System Tests  | Playwright                 |

## How to Run Tests

### Prerequisites
Before running the tests, make sure you have the following installed:
1. **Maven** (for unit and integration tests)
2. **Node.js** (for system tests with Playwright)

You must also have playwright set up for the system testing.

## Setting up PlayWright

To run system tests with Playwright, follow the steps below:

1. **Open your project in your IDE**
   
    - Launch your preferred IDE and open the project directory.
    
    - **Enter the following commands in your git bash terminal for this project:**

2. **Navigate to the test folder for system tests**:
    ```bash
    cd Movie_project/moviecatalog/e2eTesting
    ```

3. **Install the required dependencies:**:
    ```bash
    npm install
    ```
4. **Install Playwright:**:
    ```bash
    npx playwright install
    ```

### Unit & Integration Tests

1. **Open your project in your IDE**

    - Launch your preferred IDE and open the project directory.
    
    - Run the following command in your terminal to execute the unit and integration tests:

2. Change to the moviecatalog directory
    ```bash
    cd Movie_project/moviecatalog
    ```
3. Enter the following Maven command into your terminal, or use the corresponding option in IntelliJ IDEA to run the tests:
    ```bash
    mvn clean test
    ```
Your unit/integration style tests should start running!

### System Tests

1. **Ensure Spring Boot is Running**
     ```bash
     cd Movie_project/moviecatalog
     mvn clean install
     mvn spring-boot:run
     ```
2. Navigate to the e2eTesting folder.
    ```bash
    cd e2eTesting
    ```
3. **Run the desired test inside of the file**
   
    You can run your Playwright test using either the terminal or your IDE.

    - Using the Terminal: To run a specific test file, use the following command (replace movieTest.spec.js with the name of your test file):
      ```bash
      npx playwright test movieTest.spec.js
      ```
    - Using Your IDE: In your IDE, you can run a specific test by:
      - Right-clicking on the test file you want to execute.
      - Selecting Run to execute the test. For example, choose:
        - `movieTest.spec.ts`.

