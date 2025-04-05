# Movie API Documentation

## Overview
This API is designed to interact with a movie catalog. It follows RESTful principles, where each endpoint corresponds to a specific resource (movie or favourite movie) and uses HTTP methods to perform CRUD operations (Create, Read, Update, Delete).

The Movie API supports the following operations:
- **Create** a new movie and add/remove favourite movies.
- **Read** movie details, favourite movies, and perform searches.
- **Update** existing movie details.
- **Delete** movies or remove them from favourites.

## Base URL
`/api`

---

### 1. **Add Favourite Movie**
- **Endpoint:** `POST /favourites`
- **Description:** Adds a movie to the user's favourites.
- **Request Parameters:**
  - `username` (query parameter): The username of the user.
  - `movie` (body): The movie object to be added to favourites.
- **Request Body Example:**
  ```json
  {
    "title": "Inception",
    "releaseDate": "2010-07-16",
    "posterPath": "/path/to/poster.jpg",
    "genres": "Sci-Fi, Thriller",
    "synopsis": "A thief who steals corporate secrets through the use of dream-sharing technology is given the inverse task...",
    "pgRating": "PG-13",
    "productionCompanies": "Warner Bros.",
    "runtime": 148,
    "spokenLanguages": "English"
  }
  ```
- **Response:**
    - **200 OK:** "Favourite movie added successfully"
---

### 2. **Remove Favourite Movie**
- **Endpoint:** `DELETE /favourites/{movieId}`
- **Description:** Removes a movie from the user's list of favourite movies.
- **Path Variables:**
  - `movieId` (path parameter, required): The ID of the movie to remove from favourites.
- **Request Parameters:**
  - `username`: The username of the user who wants to remove the movie.
- **Response:**
  - **200 OK:** "Movie removed from favorites successfully"
  - **404 Not Found:** "Movie not found in favorites for user: {username}"

---

### 3. **Get Favourite Movies**
- **Endpoint:** `GET /favourites`
- **Description:** Retrieves all favourite movies for a user.
- **Request Parameters:**
  - `username` (query parameter, required): The username of the user whose favourite movies are being retrieved.
- **Response:**
  - **200 OK:** List of favourite movies, or a message indicating no favourites.

---

### 4. **Add Movie**
- **Endpoint:** `POST /movies`
- **Description:** Adds a new movie to the catalog.
- **Request Parameters:**
  - `title`: The title of the movie.
  - `releaseDate`: The release date of the movie.
  - `poster`: The movie poster image file.
  - `genres`: Comma-separated list of genres.
  - `synopsis`: The synopsis of the movie.
  - `pgRating`: The PG rating of the movie.
  - `productionCompanies`: The production companies involved.
  - `runtime`: The runtime of the movie in minutes.
  - `spokenLanguages`: Comma-separated list of spoken languages.
- **Response:**
  - **200 OK:** "Movie added successfully with poster at {posterPath}"

---

### 5. **Get Movies (Filter)**
- **Endpoint:** `GET /movies`
- **Description:** Filters movies by genre, PG rating, and spoken languages.
- **Request Parameters:**
  - `genre`: The genre of the movie.
  - `pg_rating`: The PG rating of the movie.
  - `spoken_languages`: The spoken languages of the movie.
- **Response:**
  - **200 OK:** List of filtered movies.

---

### 6. **Search Movies**
- **Endpoint:** `GET /movies/search`
- **Description:** Searches movies by title.
- **Request Parameters:**
  - `title`: The title of the movie to search.
- **Response:**
  - **200 OK:** List of movies matching the search.

---

### 7. **Get Movie Details**
- **Endpoint:** `GET /movies/{movieId}`
- **Description:** Retrieves detailed information for a specific movie by their movie ID.
- **Path Variables:**
  - `movieId`: The ID of the movie to retrieve details for.
- **Response:**
  - **200 OK:** Movie details.

---

### 8. **Delete Movie**
- **Endpoint:** `DELETE /movies/delete/{movieId}`
- **Description:** Deletes a movie from the catalog.
- **Path Variables:**
  - `movieId`: The ID of the movie to delete.
- **Response:**
  - **200 OK:** "The movie has been deleted."
  - **404 Not Found:** "The movie was not found."

---

### 9. **Update Movie**
- **Endpoint:** `PATCH /movies/{movieId}/update`
- **Description:** Updates details of an existing movie.
- **Path Variables:**
  - `movieId`: The ID of the movie to update.
- **Request Parameters:**
  - `title`, `releaseDate`, `genres`, `synopsis`, `pgRating`, `productionCompanies`, `runtime`, `spokenLanguages`, `poster`: The fields to update.
- **Response:**
  - **200 OK:** "Movie updated successfully!"
  - **404 Not Found:** "Movie not found for the given ID."

---


