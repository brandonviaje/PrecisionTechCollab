$(document).ready(function() {
    const movieId = new URLSearchParams(window.location.search).get('id');

    if (movieId) {
        // Fetch movie details from the first API
        const apiUrl1 = `/api/movies/${movieId}`;
        $.getJSON(apiUrl1)
            .done(function(data) {
                const { title, poster_path, synopsis, release_date, genres } = data;

                // Set the movie details in HTML
                $('#movie-title').text(title);
                $('#movie-overview').text(synopsis);
                $('#movie-release-date').text(release_date);
                $('#movie-genres').text(genres);

                // Set the movie poster
                let posterSrc = "";
                const baseUrl = window.location.protocol + "//" + window.location.host;

                if (poster_path && poster_path.startsWith("/userimg/")) {
                    posterSrc = `${baseUrl}${poster_path}`;
                }
                else if (poster_path) {
                    posterSrc = `https://image.tmdb.org/t/p/w500/${poster_path}`;
                }

                $('#movie-poster').attr('src', posterSrc);
            })
            .fail(function(error) {
                console.error("Error fetching movie details from local API:", error);
            });
    }
});
