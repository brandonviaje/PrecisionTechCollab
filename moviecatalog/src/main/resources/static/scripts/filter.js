const apiUrl = '/api/movies';

function fetchMovies(genre = "", pgRating = "", languages = "") {
    return new Promise((resolve, reject) => {
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
                    resolve();  // Resolve even if no movies are found
                    return;
                }

                movies.forEach(function (movie) {
                    const movieCard = createMovieCard(movie);
                    $(".movies").append(movieCard); // Append the new movie cards
                });

                resolve(); // Resolve when done
            })
            .fail(function (error) {
                console.error("Error Fetching data:", error);
                reject(error);
            });
    });
}

function fetchPgRatings() {
    return new Promise((resolve, reject) => {
        $.getJSON(apiUrl)
            .done(function (movies) {
                const ratings = new Set();
                movies.forEach(function (movie) {
                    if (movie.pgRating) {
                        // Normalize Casing
                        const formattedRating = movie.pgRating.toLowerCase().replace(/^\w/, (c) => c.toUpperCase());
                        ratings.add(formattedRating);
                    }
                });

                const sortedRatings = [...ratings].sort((a, b) => a.localeCompare(b));

                const ratingFilter = $("#pg-rating-filter");
                ratingFilter.empty();
                ratingFilter.append('<option value="">All Ratings</option>');

                sortedRatings.forEach(rating => {
                    if (rating !== "18+") {
                        ratingFilter.append(`<option value="${rating}">${rating}</option>`);
                    }
                });

                resolve(); // Resolve when done
            })
            .fail(error => {
                console.error("Error Fetching PG Ratings:", error);
                reject(error);
            });
    });
}

function fetchLanguages() {
    return new Promise((resolve, reject) => {
        $.getJSON(apiUrl)
            .done(function (movies) {
                const languages = new Set();
                movies.forEach(function (movie) {
                    if (Array.isArray(movie.spokenLanguages)) {
                        movie.spokenLanguages.forEach(language => languages.add(language.trim()));
                    } else if (typeof movie.spokenLanguages === "string") {
                        movie.spokenLanguages.split(",").forEach(language => languages.add(language.trim()));
                    }
                });

                const sortedLanguages = [...languages].sort((a, b) => a.localeCompare(b));
                const languageFilter = $("#languages-filter");
                languageFilter.empty();
                languageFilter.append('<option value="">All Languages</option>');

                sortedLanguages.forEach(language => {
                    const formattedLanguage = language.toLowerCase().replace(/^\w/, (c) => c.toUpperCase());
                    languageFilter.append(`<option value="${formattedLanguage}">${formattedLanguage}</option>`);
                });

                resolve();
            })
            .fail(error => {
                console.error("Error Fetching Languages:", error);
                reject(error);
            });
    });
}


function fetchGenres() {
    return new Promise((resolve, reject) => {
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

                // Convert Set to an array and sort alphabetically
                const sortedGenres = [...genres].sort((a, b) => a.localeCompare(b));

                const genreFilter = $("#genre-filter");
                genreFilter.empty();
                genreFilter.append('<option value="">All Genres</option>');

                sortedGenres.forEach(genre => {
                    // Normalize Casing
                    const formattedGenre = genre.toLowerCase().replace(/^\w/, (c) => c.toUpperCase());
                    genreFilter.append(`<option value="${formattedGenre}">${formattedGenre}</option>`);
                });

                resolve();
            })
            .fail(error => {
                console.error("Error Fetching genres:", error);
                reject(error);
            });
    });
}



function createMovieCard(movie) {
    const { title, poster_path, movie_id } = movie;

    // Dynamically get the base URL (for local or production)
    const baseUrl = window.location.protocol + "//" + window.location.host;
    const imageSrc = poster_path.startsWith("/userimg/") ? `${baseUrl}${poster_path}` : `https://image.tmdb.org/t/p/original/${poster_path}`;

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


$(document).ready(async function () {
    try {
        await Promise.all([
            fetchMovies(),
            fetchGenres(),
            fetchPgRatings(),
            fetchLanguages()
        ]);
        console.log("All data loaded.");
    } catch (error) {
        console.error("Error fetching data:", error);
    }

    // Event listener for genre selection
    $("#genre-filter").change(async function () {
        const genre = $(this).val();
        const pgRating = $("#pg-rating-filter").val();
        const languages = $("#languages-filter").val();
        try {
            await fetchMovies(genre, pgRating, languages);
            console.log("Movies loaded successfully.");
        } catch (error) {
            console.error("Error fetching movies:", error);
        }
    });

    // Event listener for PG Rating selection
    $("#pg-rating-filter").change(async function () {
        const genre = $("#genre-filter").val();
        const pgRating = $(this).val();
        const languages = $("#languages-filter").val();
        try {
            await fetchMovies(genre, pgRating, languages);
            console.log("Movies loaded successfully.");
        } catch (error) {
            console.error("Error fetching movies:", error);
        }
    });

    // Event listener for languages selection
    $("#languages-filter").change(async function () {
        const genre = $("#genre-filter").val();
        const pgRating = $("#pg-rating-filter").val();
        const languages = $(this).val();
        try {
            await fetchMovies(genre, pgRating, languages);
            console.log("Movies loaded successfully.");
        } catch (error) {
            console.error("Error fetching movies:", error);
        }
    });

    // Event listener for when a movie is added
    window.addEventListener('movieAdded', async function () {
        const genre = $("#genre-filter").val();
        const pgRating = $("#pg-rating-filter").val();
        const languages = $("#languages-filter").val();
        try {
            await fetchMovies(genre, pgRating, languages);
            console.log("Movies reloaded successfully.");
        } catch (error) {
            console.error("Error re-fetching movies:", error);
        }
        await fetchGenres();
        await fetchPgRatings();
        await fetchLanguages();
    });
});
