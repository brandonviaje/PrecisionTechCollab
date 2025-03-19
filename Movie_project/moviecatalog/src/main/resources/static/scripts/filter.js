const apiUrl = '/api/movies';

function fetchMovies(genre = "", pgRating = "", languages = "") {
    let url = apiUrl;

    // Add filters to the URL if they are set
    const filters = [];
    if (genre) filters.push(`genre=${encodeURIComponent(genre)}`);
    if (pgRating) filters.push(`pg_rating=${encodeURIComponent(pgRating)}`);
    if (languages) filters.push(`spoken_languages=${encodeURIComponent(languages)}`);

    if (filters.length > 0) {
        url += `?${filters.join("&")}`;
    }

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

function fetchPgRatings() {
    $.getJSON(apiUrl)
        .done(function (movies) {
            const ratings = new Set();
            movies.forEach(function (movie) {
                if (movie.pgRating) {
                    //Normalize Casing
                    const formattedRating = movie.pgRating.toLowerCase().replace(/^\w/, (c) => c.toUpperCase());
                    ratings.add(formattedRating);
                }
            });
            const ratingFilter = $("#pg-rating-filter");
            ratingFilter.empty();
            ratingFilter.append('<option value="">All Ratings</option>');

            ratings.forEach(rating => ratingFilter.append(`<option value="${rating}">${rating}</option>`));
        })
        .fail(error => console.error("Error Fetching PG Ratings:", error));
}

function fetchLanguages() {
    $.getJSON(apiUrl)
        .done(function (movies) {
            const languages = new Set();
            movies.forEach(function (movie) {
                if (Array.isArray(movie.spokenLanguages)) {
                    movie.spokenLanguages.forEach(language => languages.add(language.trim()));
                    if (language && language.trim() !== "Null") {
                        languages.add(language.trim());
                    }
                } else if (typeof movie.spokenLanguages === "string") {
                    movie.spokenLanguages.split(",").forEach(language => languages.add(language.trim()));
                }
            });

            const languageFilter = $("#languages-filter");
            languageFilter.empty();
            languageFilter.append('<option value="">All Languages</option>');

            languages.forEach(language => {
                //Normalize Casing
                const formattedLanguage = language.toLowerCase().replace(/^\w/, (c) => c.toUpperCase());
                languageFilter.append(`<option value="${formattedLanguage}">${formattedLanguage}</option>`);
            });
        })
        .fail(error => console.error("Error Fetching Languages:", error));
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
            genreFilter.append('<option value="">All Genres</option>');

            genres.forEach(genre => {
                //Normalize Casing
                const formattedGenre = genre.toLowerCase().replace(/^\w/, (c) => c.toUpperCase());
                genreFilter.append(`<option value="${formattedGenre}">${formattedGenre}</option>`);
            });

            console.log("Updated Genres in Dropdown:", [...genres]);  // Debugging log
        })
        .fail(error => console.error("Error Fetching genres:", error));
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

$(document).ready(function () {
    fetchMovies();
    fetchGenres();
    fetchPgRatings();
    fetchLanguages();
    // Event listener for genre selection
    $("#genre-filter").change(function () {
        const genre = $(this).val();
        const pgRating = $("#pg-rating-filter").val();
        const languages = $("#languages-filter").val();
        fetchMovies(genre, pgRating, languages);
    });

    // Event listener for PG Rating selection
    $("#pg-rating-filter").change(function () {
        const genre = $("#genre-filter").val();
        const pgRating = $(this).val();
        const languages = $("#languages-filter").val();
        fetchMovies(genre, pgRating, languages);
    });

    // Event listener for languages selection
    $("#languages-filter").change(function () {
        const genre = $("#genre-filter").val();
        const pgRating = $("#pg-rating-filter").val();
        const languages = $(this).val();
        fetchMovies(genre, pgRating, languages);
    });

    window.addEventListener('movieAdded', function () {
        fetchMovies();
        fetchGenres();
        fetchPgRatings();
        fetchLanguages();
    });
});
