const apiUrl = '/api/movies';

function fetchMovies(genre = "") {
    const url = genre ? `${apiUrl}?genre=${encodeURIComponent(genre)}` : apiUrl;

    $.getJSON(url)
        .done(function (movies) {

            // Clear existing movies before appending new ones
            $(".movies").empty();

            if (movies.length === 0) {
                $(".movies").append("<p>No movies found.</p>");
                return;
            }

            movies.forEach(function (movie) {
                const movieCard = createMovieCard(movie);
                $(".movies").append(movieCard); // Append the new movie cards
            });
        })
        .fail(function (error) {
            console.error("Error Fetching data:", error);
        });
}

function createMovieCard(movie) {
    const { title, poster_path, movie_id } = movie;

    // Check if the poster path is a file name or a full URL (indicating it's from TMDb)
    const imageSrc = poster_path.startsWith("/userimg/") ? `..${poster_path}` : `https://image.tmdb.org/t/p/original/${poster_path}`;

    return `
    <div class="movie_item">
        <div class="movie-photo-container">
            <a href="../components/movieDetails.html?id=${movie_id}">
                <img src="${imageSrc}" class="movie_img_rounded" alt="${title}">
            </a>
        </div>
        <div class="title">${title}</div>
    </div>
    `;
}

function fetchGenres() {
    $.getJSON(apiUrl)
        .done(function (movies) {
            const genres = new Set();  // A set to ensure no duplicate genres

            // Loop through each movie and extract genres
            movies.forEach(function (movie) {
                console.log("Genres in movie:", movie.genres); // Log the genre field to inspect its structure

                // If genres is an array, you can directly loop through it
                if (Array.isArray(movie.genres)) {
                    movie.genres.forEach(function (genre) {
                        genres.add(genre.trim()); // Add each genre to the set
                    });
                } else if (typeof movie.genres === "string") {
                    movie.genres.split(",").forEach(function (genre) { // Split if it's a string
                        genres.add(genre.trim());
                    });
                }
            });

            const genreFilter = $("#genre-filter");
            genreFilter.empty(); // Clear the dropdown
            genreFilter.append('<option value="">All</option>'); // Default option

            // Add genres to dropdown
            genres.forEach(function (genre) {
                genreFilter.append(`<option value="${genre}">${genre}</option>`);
            });
        })
        .fail(function (error) {
            console.error("Error Fetching genres:", error);
        });
}

$(document).ready(function () {
    fetchMovies();
    fetchGenres();

    // Event listener for genre selection
    $("#genre-filter").change(function () {
        fetchMovies($(this).val());
    });
});