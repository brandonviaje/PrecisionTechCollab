const apiUrl = '/api/movies';

function fetchMovies(genre = "") {
    const url = genre ? `${apiUrl}?genre=${genre}` : apiUrl;

    $.getJSON(url)
        .done(function (movies) {

            if (movies.length === 0) {
                $(".movies").append("<p>No movies found.</p>");
                return;
            }

            movies.forEach(function (movie) {
                const movieCard = createMovieCard(movie);
                $(".movies").append(movieCard); // Append to movies div
            });
        })
        .fail(function (error) {
            console.error("Error Fetching data:", error);
        });
}

function createMovieCard(movie) {
    const { title, poster_path, movie_id } = movie;
    console.log("Poster Path:", poster_path); // Debugging line

    return `
    <div class="movie_item">
        <div class="movie-photo-container">
            <a href="../components/movieDetails.html?id=${movie_id}">
                <img src="https://image.tmdb.org/t/p/original/${poster_path}" class="movie_img_rounded" alt="${title}">
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
                movie.genres.split(",").forEach(function (genre) {
                    genres.add(genre.trim());
                });
            });

            const genreFilter = $("#genre-filter");
            genreFilter.empty();
            genreFilter.append('<option value="">All</option>');

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