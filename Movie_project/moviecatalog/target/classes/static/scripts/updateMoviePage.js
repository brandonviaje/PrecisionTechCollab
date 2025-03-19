$(document).ready(function() {

    // Search for movies when the user clicks the search button
    $('#search-button').click(function() {
        const searchQuery = $('#movie-search').val().trim();

        if (searchQuery !== '') {
            $.getJSON(`/api/movies/search?title=${searchQuery}`)
                .done(function(data) {
                    if (data.length > 0) {
                        displaySearchResults(data);  // Display search results
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
    // Display the search results
    function displaySearchResults(movies) {
        $('#search-results').empty();

        movies.forEach(function(media) {
            // Destructure the necessary data from the media object
            const { title, name, poster_path, movie_id } = media;

            // Determine the image source based on the poster path
            const imageSrc = poster_path && poster_path.startsWith("/userimg/") ? `http://localhost:8080${poster_path}` :  `https://image.tmdb.org/t/p/original/${poster_path}`;

            // Create the movie element with the title and poster
            const movieElement = $(`
            <div class="movie-result" data-id="${movie_id}">
                <div class="movie-photo-container">
                    <img src="${imageSrc}" class="movie-poster" alt="${title || name}">
                </div>
                <div class="movie-title">${title || name}</div>
            </div>
        `);

            // Append movie element to the search results container
            $('#search-results').append(movieElement);

            // When a movie is clicked, show the form and prefill data
            movieElement.click(function() {
                window.location.href = `../components/updateMovie.html?id=${movie_id}`;
            });
        });
    }


});
