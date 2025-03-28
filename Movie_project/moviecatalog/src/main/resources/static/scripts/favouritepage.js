import { isUserSignedIn } from './headerfooter.js';

$(document).ready(function() {
    // Check if user is signed in
    if (!isUserSignedIn()) {
        showNotificationAndRedirect("Please sign in to view your favorites.", "../html/login.html");
        return;
    }

    // Get the logged-in user's username
    const userName = localStorage.getItem('userName') || localStorage.getItem('username');

    if (!userName) {
        showNotificationAndRedirect("No user logged in.", "../html/login.html");
        return;
    }

    // Fetch favorite movies
    $.ajax({
        url: `/api/favourites?username=${userName}`,
        method: 'GET',
        dataType: 'json'
    })
        .done(function(response) {
            // Check if the response is a valid array of movies
            console.log("Full favorite movies response:", response);


            if (Array.isArray(response)) {
                console.log("Fetched favorite movies:", response);

                if (response.length === 0) {
                    displayNoFavoritesMessage();
                    return;
                }

                const movieListContainer = $('<div class="favorite-movies-grid"></div>');

                response.forEach(movie => {
                    console.log("Each movie object:", movie);
                    console.log("Movie ID:", movie.id, movie.movie_id, movie.movieId);
                    const movieCard = createMovieCard(movie);
                    movieListContainer.append(movieCard);
                });

                $('main').append(movieListContainer);
            } else {
                // Handle the case where the response is an error message
                console.error("Error fetching favorites:", response);
                showNotification("Failed to load favorite movies", true);
                displayNoFavoritesMessage();
            }
        })
        .fail(function(xhr, status, error) {
            console.error("Error fetching favorites:", status, error);
            console.error("Response status:", xhr.status);
            console.error("Response text:", xhr.responseText);

            showNotification("Failed to load favorite movies", true);
            displayNoFavoritesMessage();
        });


    // Function to create a movie card
    function createMovieCard(movie) {
        // Correct the property name to match what you added in the backend
        // Try multiple possible ID fields
        const movieId = movie.movie_id || movie.id || 'unknown';
        console.log("Movie card creation - Movie ID:", movieId, "Full movie object:", movie);

        // Robust poster path handling
        let posterSrc = movie.poster_path;
        if (!posterSrc) {
            posterSrc = "/path/to/default/poster.jpg"; // Add a default poster path
        } else if (posterSrc.startsWith('/userimg/')) {
            posterSrc = `http://localhost:8080${posterSrc}`;
        } else if (!posterSrc.startsWith('http')) {
            posterSrc = `https://image.tmdb.org/t/p/w500/${posterSrc}`;
        }

        const movieCard = $(`
        <div class="movie-card" data-movie-id="${movieId}">
            <img src="${posterSrc}" alt="${movie.title} Poster" class="movie-poster">
            <div class="movie-info">
                <h3 class="movie-title">${movie.title}</h3>
                <p class="movie-release-date">Released: ${movie.release_date}</p>
                <p class="movie-genres">${movie.genres}</p>
                <button class="remove-favorite-btn">Remove from Favorites</button>
            </div>
        </div>
    `);

        // Add remove from favorites functionality
        movieCard.find('.remove-favorite-btn').click(function() {
            console.log("Removing movie with ID:", movieId);
            removeFavoriteMovie(movieId);
        });

        // Add click event to navigate to movie details
        movieCard.find('.movie-poster, .movie-title').click(function() {
            window.location.href = `../html/moviedetails.html?id=${movieId}`;
        });

        return movieCard;
    }

    function removeFavoriteMovie(movieId) {
        $.ajax({
            url: `/api/favourites/${movieId}?username=${userName}`,
            type: "DELETE",
            success: function (response) {
                // Remove the movie card from the UI using the external ID
                $(`[data-movie-id="${movieId}"]`).remove();
                showNotification("Movie removed from favorites");
            },
            error: function (xhr, status, error) {
                console.error("Error removing movie:", {
                    status: xhr.status,
                    statusText: xhr.statusText,
                    responseText: xhr.responseText
                });
                showNotification("Failed to remove movie from favorites", true);
            }
        });
    }

    // Function to display message when no favorites
    function displayNoFavoritesMessage() {
        const noFavoritesMessage = $(`
            <div class="no-favorites-message">
                <p>You haven't added any favorite movies yet.</p>
                <a href="../html/browse.html" class="browse-movies-btn">Browse Movies</a>
            </div>
        `);
        $('main').html(noFavoritesMessage);
    }

    // Notification function
    function showNotification(message, isError = false) {
        // Create notification element if it doesn't exist
        if ($("#notification").length === 0) {
            $('body').append('<div id="notification" style="position: fixed; top: 20px; right: 20px; padding: 10px; z-index: 1000; display: none;"></div>');
        }

        const notificationEl = $("#notification");
        notificationEl.text(message);
        notificationEl.css({
            'background-color': isError ? '#ff6b6b' : '#4ecdc4',
            'color': 'white',
            'border-radius': '5px',
            'box-shadow': '0 2px 5px rgba(0,0,0,0.2)'
        });

        notificationEl.fadeIn().delay(3000).fadeOut();
    }

    // Notification and redirect function
    function showNotificationAndRedirect(message, redirectUrl) {
        showNotification(message, true);
        setTimeout(() => {
            window.location.href = redirectUrl;
        }, 3000);
    }
});