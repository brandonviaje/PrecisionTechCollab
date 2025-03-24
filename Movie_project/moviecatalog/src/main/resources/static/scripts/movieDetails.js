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

                // Set the movie poster (assuming local server serves images)
                let posterSrc = "";

                if (poster_path && poster_path.startsWith("/userimg/")) {
                    posterSrc = `http://localhost:8080${poster_path}`; // Assuming the server serves the image at localhost:8080
                }
                else if (poster_path) {
                    posterSrc = `https://image.tmdb.org/t/p/w500/${poster_path}`;
                }

                $('#movie-poster').attr('src', posterSrc);
            })
            .fail(function(error) {
                console.error("Error fetching movie details from local API:", error);
            });

        // Fetch movie details from the second API
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
    }
});
