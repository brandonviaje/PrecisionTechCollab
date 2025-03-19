$(document).ready(function() {
    const movieId = new URLSearchParams(window.location.search).get('id');

    if (movieId) {
        const apiUrl = `https://api.themoviedb.org/3/movie/${movieId}?api_key=cf334fe88eeddcdc728d651ffed41008`;

        $.getJSON(apiUrl)
            .done(function(data) {
                const { title, poster_path, overview, release_date, genres } = data;

                // Join the genres into a single string
                const genreNames = genres.map(genre => genre.name).join(', ');

                $('#movie-title').text(title);
                $('#movie-poster').attr('src', `https://image.tmdb.org/t/p/w500/${poster_path}`);
                $('#movie-overview').text(overview);
                $('#movie-release-date').text(release_date);
                $('#movie-genres').text(genreNames); // Display the genres
            })
            .fail(function(error) {
                console.error("Error fetching movie details:", error);
            });
    }
});