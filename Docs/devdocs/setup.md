# Developer Setup Guide

## Overview
This guide provides the steps to set up the development environment for the PrecisionTechCollab project. It covers prerequisites and setting up the backend and frontend. Our testing.md goes into depth on how to configure playwright for e2e testing, and how to run our unit tests/integration tests. The project uses Java 23 with Spring Boot for the backend, and HTML, CSS, and JavaScript for the frontend. Playwright is optional and used for end-to-end testing.

## Prerequisites
- Java 23 (Oracle OpenJDK)
- Maven
- Git Bash
- Node.js (required for Playwright)
- Playwright (for testing, optional)

## Getting Started

### 1. Clone the Repository

Enter the following into your Git Bash terminal:

```bash
git clone https://github.com/brandonviaje/PrecisionTechCollab.git
cd PrecisionTechCollab
```

### 2. Change the directory

```bash
cd moviecatalog
```

### 3. Build the JAR File

```bash
mvn clean install
```

### 4. Call Spring Boot Run

```bash
mvn spring-boot:run
```

### 5. Access the Application in Your Browser

Open your browser and enter the following URL:
http://localhost:8080/index.html

### 6. Shutdown the Application

To shut down the Spring Boot application, go back to your Git Bash terminal and press Ctrl+C.

NOTE: Please view `testing.md` for further instructions on how to test our program.

## Editing Source Code

After cloning the repository, open the project in your IDE of choice (preferably IntelliJ).

The source code for this project is located in:
`Movie_project/moviecatalog/src/main`

From here, the project is organized into two folders:

- `java`: Contains the backend code/logic, including Java classes for the Spring Boot application.
- `resources`: Contains the frontend assets (HTML, CSS, JavaScript, Images).

You can make changes to the source code in these directories. Once you've made updates, the changes will be reflected when you run the application locally.
