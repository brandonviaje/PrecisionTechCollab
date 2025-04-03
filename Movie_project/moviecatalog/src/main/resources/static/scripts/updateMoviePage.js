$(document).ready(function() {
    // Fetch and display all movies on page load
    fetchMovies();

    // Search for movies when the user clicks the search button
    $('#search-button').click(function() {
        const searchQuery = $('#movie-search').val().trim();

        if (searchQuery !== '') {
            $.getJSON(`/api/movies/search?title=${searchQuery}`)
                .done(function(data) {
                    if (data.length > 0) {
                        displaySearchResults(data);
                    } else {
                        $('#search-results').html('<p>No movies found.</p>');
                    }
                })
                .fail(function(error) {
                    console.error("Error fetching movie data:", error);
                });
        } else {
            $('#search-results').empty();
            fetchMovies(); // Show all movies if the search is empty
        }
    });

    // Fetch all movies from the database
    function fetchMovies() {
        $.getJSON('/api/movies')
            .done(function(data) {
                if (data.length > 0) {
                    displaySearchResults(data); // Display all movies
                } else {
                    $('#search-results').html('<p>No movies found.</p>');
                }
            })
            .fail(function(error) {
                console.error("Error fetching all movies:", error);
            });
    }

    // Display the search results or all movies
    function displaySearchResults(movies) {
        $('#search-results').empty();

        movies.forEach(function(media) {
            const { title, name, poster_path, movie_id } = media;
            const imageSrc = poster_path && poster_path.startsWith("/userimg/")
                ? `http://localhost:8080${poster_path}`
                : `https://image.tmdb.org/t/p/original/${poster_path}`;

            const movieElement = $(`
                <div class="movie-result" data-id="${movie_id}">
                    <div class="movie-photo-container">
                        <img src="${imageSrc}" class="movie-poster" alt="${title || name}">
                    </div>
                    <div class="movie-title">${title || name}</div>
                </div>
            `);

            $('#search-results').append(movieElement);

            // Redirect to the update movie page when clicked
            movieElement.click(function() {
                window.location.href = `../components/updateMovie.html?id=${movie_id}`;
            });
        });
    }
});
