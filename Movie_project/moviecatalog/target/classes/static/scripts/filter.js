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

            const genreMap = new Map(); // stores lowercase as key then the correct cased genre as value

            movies.forEach(function (movie) {
                if (Array.isArray(movie.genres)) {
                    movie.genres.forEach(genre => processGenre(genre, genreMap));
                } else if (typeof movie.genres === "string") {
                    movie.genres.split(",").forEach(genre => processGenre(genre, genreMap));
                }
            });

            const genreFilter = $("#genre-filter");
            genreFilter.empty();
            genreFilter.append('<option value="">All</option>');

            // add genres with correct casing
            genreMap.forEach(originalGenre => {
                genreFilter.append(`<option value="${originalGenre}">${originalGenre}</option>`);
            });
        })
        .fail(error => console.error("Error Fetching genres:", error));
}

// helper func process genres and apply correct formatting
function processGenre(genre, genreMap) {
    const trimmedGenre = genre.trim();
    const normalizedGenre = trimmedGenre.toLowerCase();
    if (!genreMap.has(normalizedGenre)) {
        genreMap.set(normalizedGenre, formatGenre(trimmedGenre));
    }
}

// format new genres (e.g., DOCUMENTARY → Documentary)
function formatGenre(genre) {
    return genre
        .toLowerCase()
        .replace(/\b\w/g, char => char.toUpperCase()); // Capitalizes each word
}



$(document).ready(function () {
    fetchMovies();
    fetchGenres();

    // Event listener for genre selection
    $("#genre-filter").change(function () {
        $(this).css('position', 'relative');
        fetchMovies($(this).val());
    });

    window.addEventListener('movieAdded',function (){
        fetchMovies();
        fetchGenres();
    });
});