$(document).ready(function () {
    const urlParams = new URLSearchParams(window.location.search);
    const query = urlParams.get("query");

    if (!query || query.trim() === "") {
        $(".movies").html("<p>No movies found.</p>");
        return;
    }

    searchMovies(query);
});

function searchMovies(query) {
    const url = `/api/movies/search?title=${encodeURIComponent(query)}`;

    $.getJSON(url)
        .done(function (movies) {
            $(".movies").empty();

            if (movies.length === 0) {
                $(".movies").append("<p>No movies found.</p>");
                return;
            }

            movies.forEach(function (movie) {
                const movieCard = createMovieCard(movie);
                $(".movies").append(movieCard);
            });
        })
        .fail(function (error) {
            console.error("Error Fetching movies:", error);
        });
}


function createMovieCard(movie) {
    const { title, poster_path, movie_id } = movie;

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
