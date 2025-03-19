$(document).ready(function() {

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
        }
    });

    // Display the search results
    function displaySearchResults(movies) {
        $('#search-results').empty();

        movies.forEach(function(media) {
            const { title, name, poster_path, movie_id } = media;

            const imageSrc = poster_path && poster_path.startsWith("/userimg/")
                ? `http://localhost:8080${poster_path}`
                : `https://image.tmdb.org/t/p/original/${poster_path}`;

            const movieElement = $(`
                <div class="movie-result" data-id="${movie_id}" data-title="${title || name}">
                    <div class="movie-photo-container">
                        <img src="${imageSrc}" class="movie-poster" alt="${title || name}">
                    </div>
                    <div class="movie-title">${title || name}</div>
                </div>
            `);

            $('#search-results').append(movieElement);

            // Show confirmation popup on click
            movieElement.click(function() {
                const movieId = $(this).data("id");
                const movieTitle = $(this).data("title");
                console.log("Deleting movie:", movieTitle, "with ID:", movieId);

                if (confirm(`Are you sure you want to delete "${movieTitle}"?`)) {
                    deleteMovie(movieId, $(this));
                }
            });
        });
    }

    // Function to delete a movie
    function deleteMovie(movieId, movieElement) {
        $.ajax({
            url: `/api/movies/delete/${movieId}`,
            type: 'DELETE',
            success: function(response) {
                alert(response); // Show success message
                movieElement.remove(); // Remove the movie from the UI
            },
            error: function(xhr) {
                alert("Failed to delete movie: " + xhr.responseText);
            }
        });
    }

});
