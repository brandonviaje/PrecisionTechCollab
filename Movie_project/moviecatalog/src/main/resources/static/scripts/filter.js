const apiUrl = 'https://api.themoviedb.org/3/trending/movie/week?api_key=cf334fe88eeddcdc728d651ffed41008&page=';
const genreApiUrl = 'https://api.themoviedb.org/3/genre/movie/list?api_key=cf334fe88eeddcdc728d651ffed41008';

// Store all movies and genres
let allMovies = [];
let allGenres = [];
let currentPage = 1; // Start from the first page

// Fetch the list of genres
function fetchGenres() {
    $.getJSON(genreApiUrl)
        .done(function (data) {
            allGenres = data.genres;
            populateGenreFilter(allGenres);
        })
        .fail(function (error) {
            console.error("Error Fetching genres:", error);
        });
}

// Populate the genre filter dropdown with genres from the API
function populateGenreFilter(genres) {
    const genreSelect = $('#genre-filter');
    genres.forEach(function (genre) {
        const option = `<option value="${genre.id}">${genre.name}</option>`;
        genreSelect.append(option);
    });
}

// Fetch movies and display them
function fetchMovies(page = 1) {
    $.getJSON(apiUrl + page)
        .done(function (data) {
            if (page === 1) {
                allMovies = data.results; // On the first page, overwrite the list
            } else {
                allMovies = allMovies.concat(data.results); // On subsequent pages, append the results
            }
            displayMovies(allMovies);
        })
        .fail(function (error) {
            console.error("Error Fetching movies:", error);
        });
}

// Display movies in the container
function displayMovies(movies) {
    $(".movies").empty(); // Clear the movie display before appending
    movies.forEach(function (media) {
        const movieCard = createMovieCard(media);
        $(".movies").append(movieCard);
    });
}

// Create a movie card to display movie info
function createMovieCard(media) {
    const { title, name, poster_path, id } = media;
    return `
    <div class="movie_item">
        <div class="movie-photo-container">
            <a href="../components/movieDetails.html?id=${id}">
                <img src="https://image.tmdb.org/t/p/w300/${poster_path}" class="movie_img_rounded" alt="${title || name}">
            </a>
        </div>
        <div class="title">${title || name}</div>
    </div>
    `;
}

// Filter movies by genre
function filterMoviesByGenre(selectedGenreId) {
    let filteredMovies = allMovies;

    if (selectedGenreId) {
        filteredMovies = allMovies.filter(function (media) {
            return media.genre_ids.includes(parseInt(selectedGenreId));
        });
    }

    displayMovies(filteredMovies);
}

// Load more movies (e.g., when the user scrolls or clicks a button)
function loadMoreMovies() {
    currentPage++; // Increment the current page number
    fetchMovies(currentPage); // Fetch the next page of movies
}

// Event listener for genre filter change
$(document).ready(function () {
    fetchGenres();
    fetchMovies(currentPage); // Fetch the first page of movies

    $('#genre-filter').change(function () {
        const selectedGenreId = $(this).val();
        filterMoviesByGenre(selectedGenreId);
    });

    // Load more movies when a button is clicked or when scrolling (example button click)
    $('#load-more-button').click(function () {
        loadMoreMovies();
    });
});
