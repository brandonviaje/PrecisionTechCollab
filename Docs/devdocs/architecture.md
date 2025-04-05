# System Architecture

## Overview
- **Frontend:** HTML, CSS, JavaScript
- **Backend:** Spring Boot (Java)
- **DataBase:** Supabase

## How it works:

Our project is built using the MVC (Model-View-Controller) architecture, which separates the application into three interconnected components: the Model, the View, and the Controller.

1. **View (Frontend - HTML/CSS/JS)**:
   - The **View** is the **UI** that users interact with. It displays data and listens for user actions (clicks, form submits, etc.).

2. **Controller (Spring Boot)**:
   - The **Controller** listens to requests from the **View**, executes business logic, interacts with the **Model** (database), and returns the results to the **View**.

3. **Model (Supabase)**:
   - The **Model** represents the data. It contains the business logic and database interactions. In your case, Supabase acts as the database layer, storing and retrieving data.

## Data Flow:

1. The **User** interacts with the **View** (frontend).
2. The **View** sends requests to the **Controller** (Spring Boot).
3. The **Controller** communicates with the **Model** (Supabase) to fetch/manipulate data.
4. The **Controller** sends a response (data, JSON, or status) back to the **View**.
5. The **View** updates the UI accordingly.

## Diagram
![Architecture Diagram](https://github.com/user-attachments/assets/391ba4d5-34a4-49c1-9ead-1e5a09ffdccc)

## Tech Stack
| Component   | Tech                | Role                        |
|-------------|---------------------|-----------------------------|
| Frontend    | HTML, CSS, JavaScript  | User Interface              |
| Backend     | Spring Boot (Java)  | API & Backend logic        |
| Database    | Supabase            | Stores all Data             |
| Testing    | Playwright, JUnit, MockWebServer, Mockito | 	Testing and Mocking services |
