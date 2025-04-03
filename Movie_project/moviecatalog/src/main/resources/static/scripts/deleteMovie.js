$(document).ready(function() {
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

    // Function to display search results
    function displaySearchResults(movies) {
        $('#search-results').empty();

        movies.forEach(function(media) {
            const { title, name, poster_path, movie_id } = media;

            const imageSrc = poster_path && poster_path.startsWith("/userimg/")
                ? `http://localhost:8080${poster_path}`
                : `https://image.tmdb.org/t/p/original/${poster_path}`;

            const movieElement = `
                <div class="movie-result" data-id="${movie_id}" data-title="${title || name}">
                    <div class="movie-photo-container">
                        <img src="${imageSrc}" class="movie-poster" alt="${title || name}">
                    </div>
                    <div class="movie-title">${title || name}</div>
                    <button class="delete-button">Delete</button>
                </div>
            `;
            $('#search-results').append(movieElement);
        });
    }

    // Handle delete button click using .on()
    $('#search-results').on('click', '.delete-button', function() {
        const movieElement = $(this).closest('.movie-result');
        const movieId = movieElement.data('id');
        const movieTitle = movieElement.data('title');

        if (confirm(`Are you sure you want to delete "${movieTitle}"?`)) {
            deleteMovie(movieId, movieElement);
        }
    });

    // Function to delete a movie
    function deleteMovie(movieId, movieElement) {
        $.ajax({
            url: `/api/movies/delete/${movieId}`,
            type: 'DELETE',
            beforeSend: function() {
                movieElement.find('.delete-button').text('Deleting...');
            },
            success: function(response) {
                alert(response);
                movieElement.remove(); // Remove movie from UI
                if ($('#search-results').children().length === 0) {
                    $('#search-results').html('<p>No movies left.</p>');
                }
            },
            error: function(xhr) {
                alert("Failed to delete movie: " + xhr.responseText);
                movieElement.find('.delete-button').text('Delete');
            }
        });
    }
});
