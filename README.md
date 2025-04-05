# Precision Technology Inc. ™

![Image](https://github.com/user-attachments/assets/a1954cca-dbfe-4ded-81a4-0329b46125e4)

## Name: FilmFlix

<div style="text-align: justify;">
FilmFlix is a movie database cataloguing system designed to manage and display a vast collection of movies. It enables users to search, filter, and view detailed information about movies, including genres, release dates, and movie descriptions. Users can also add their favorite movies to a personal list for easy access.
</div>
<br>
<div style="text-align: justify;">
Admins have enhanced privileges, allowing them to manage the entire movie database. They can add new movies, edit existing movie details, and delete movies that are no longer relevant. The system makes navigation smooth between different user roles, with user-friendly interfaces for browsing and managing movie content.
</div>

# How to Run the Project

## Prerequisites
Before running the project, ensure you have the following installed:
- **IntelliJ IDEA**
- **Maven** (installed and configured)
- **Spring Boot**

## Step-by-Step Guide

### Option 1: Online Deployable

The project is deployed and can be accessed directly at: [Precision Tech Movie Catalog](https://precisiontechcollab.onrender.com/index.html)

You can interact with the movie catalog and manage your movies through our web interface.

**Note:**
  - **If the website hasn't been accessed for a while, it may take a few moments for the server to start. Kindly be patient while it wakes up.**
  - **When browsing the movie library, loading times may vary as all movies need to be fetched. Please allow a moment for the content to fully load.**
### Option 2: Run Locally

  ### 1. Open the Project in IntelliJ
  - Click on **code** and click on download zip
  - Unzip the folder and launch **IntelliJ IDEA**.
  - Load the project into the IDE.
  
  ### 2. Run Maven Lifecycle Commands
  - Locate the **Maven** tool window (usually on the right sidebar).
  - Search for **Lifecycle** and expand the section.
  - Click **Clean** and wait for the process to complete.
  
  ### 3. Start the Spring Boot Application
  - In the **Maven** tool window, navigate to:
    - **Plugins** → **Spring Boot**
    - Click **spring-boot:run** to start the application.
  
  ### 4. Access the Application
  - Allow the application to start and once the application is running, open a web browser.
  - Enter the following URL: http://localhost:8080/index.html

### User Documentation
For admin login details, please refer to the **user documentation** provided in the project. This includes information such as admin usernames and passwords.

### Recording
![Demo Recording](https://github.com/user-attachments/assets/130b8d3c-688e-4b61-bfe7-68263ec09d87)

# Sources 
This project uses the dataset by **TMDB Movies Dataset (2023)**. Details, including metadata, genres, release data, and other relevant information seen in the project, are from the dataset.

- **Name:** TMDB Movies Dataset (2023)
- **Dataset Source:** [Kaggle Dataset](https://www.kaggle.com/datasets/asaniczka/tmdb-movies-dataset-2023-930k-movies)
- **Provider:** Asaniczka and themoviedb.org
- **License:** ODC Attribution License
