const apiUrl = '/api/movies';

function fetchMovies(genre = "") {
    const url = genre ? `${apiUrl}?genre=${genre}` : apiUrl;

    $.getJSON(url)
        .done(function (movies) {
            $(".movies").empty(); // Clear existing movies

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
    const { title, runtime, genres, synopsis, id } = movie;
    return `
    <div class="movie-card">
      <h2>${title}</h2>
      <p><strong>Runtime:</strong> ${runtime}</p>
      <p><strong>Genre:</strong> ${genres}</p>
      <p><strong>Synopsis:</strong> ${synopsis}</p>
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
    fetchMovies(); // Load all movies on page load
    fetchGenres(); // Populate genre dropdown

    // Event listener for genre selection
    $("#genre-filter").change(function () {
        fetchMovies($(this).val());
    });
});
