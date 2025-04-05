// Extract movie ID from URL
const urlParams = new URLSearchParams(window.location.search);
const movieId = urlParams.get('id');  // Fetch the movie ID from the URL

// Fetch movie details using the API
const apiUrl2 = `https://api.themoviedb.org/3/movie/${movieId}?api_key=cf334fe88eeddcdc728d651ffed41008`;
$.getJSON(apiUrl2)
    .done(function(data) {
        const { title, poster_path, overview, release_date, genres } = data;

        // Join the genres into a single string
        const genreNames = genres.map(genre => genre.name).join(', ');

        // Update details in case the second API provides better data
        $('#movie-title').text(title);
        $('#movie-poster').attr('src', `https://image.tmdb.org/t/p/w500/${poster_path}`);
        $('#movie-overview').text(overview);
        $('#movie-release-date').text(release_date);
        $('#movie-genres').text(genreNames);
    })
    .fail(function(error) {
        console.error("Error fetching movie details from TMDB API:", error);
    });