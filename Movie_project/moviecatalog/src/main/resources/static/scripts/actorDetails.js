$(document).ready(function() {
    // Get the movie_id from the URL query parameters
    const movieId = new URLSearchParams(window.location.search).get('id');

    if (movieId) {
        // Request to your backend API to get movie details
        const movieDetailsUrl = `/api/movies/${movieId}`; // Assuming the backend has an endpoint to fetch movie by ID

        $.getJSON(movieDetailsUrl)
            .done(function(movieData) {
                // Destructure the movie data response
                const { title, releaseDate, synopsis, posterPath } = movieData;

                // Set the values to the HTML elements
                $('#movie-title').text(title);
                $('#movie-release-date').text(releaseDate);
                $('#movie-overview').text(synopsis);

                // If a poster path exists, set the image source
                const posterUrl = posterPath ? posterPath : 'https://via.placeholder.com/500'; // Placeholder if no poster
                $('#movie-poster').attr('src', posterUrl);
            })
            .fail(function(error) {
                console.error("Error fetching movie details:", error);
            });
    }
});
