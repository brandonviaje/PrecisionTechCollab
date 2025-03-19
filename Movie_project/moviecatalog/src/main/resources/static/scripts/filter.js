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
    const imageSrc = poster_path.startsWith("/userimg/") ? `http://localhost:8080${poster_path}` : `https://image.tmdb.org/t/p/original/${poster_path}`;
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
            const genres = new Set();
            movies.forEach(function (movie) {
                if (Array.isArray(movie.genres)) {
                    movie.genres.forEach(genre => genres.add(genre.trim()));
                } else if (typeof movie.genres === "string") {
                    movie.genres.split(",").forEach(genre => genres.add(genre.trim()));
                }
            });

            const genreFilter = $("#genre-filter");
            genreFilter.empty();
            genreFilter.append('<option value="">All</option>');

            genres.forEach(genre => genreFilter.append(`<option value="${genre}">${genre}</option>`));

            console.log("Updated Genres in Dropdown:", [...genres]);  // Debugging log
        })
        .fail(error => console.error("Error Fetching genres:", error));
}


$(document).ready(function () {
    // Check if the movieUpdated flag is set in localStorage
    if (localStorage.getItem('movieUpdated') === 'true') {
        fetchMovies();  // Refresh the movies list
        localStorage.removeItem('movieUpdated');  // Remove the flag to prevent reloading unnecessarily
    } else {
        fetchMovies();
    }

    fetchGenres();

    $("#genre-filter").change(function () {
        $(this).css('position', 'relative');
        fetchMovies($(this).val());
    });

    window.addEventListener('movieAdded', function () {
        fetchMovies();
        fetchGenres();
    });
});
