import { isUserSignedIn } from './headerfooter.js';

$(document).ready(function() {
    if (!isUserSignedIn()) {
        showNotificationAndRedirect("Please sign in to view your favorites.", "../html/login.html");
        return;
    }

    const userName = localStorage.getItem('userName') || localStorage.getItem('username');

    if (!userName) {
        showNotificationAndRedirect("No user logged in.", "../html/login.html");
        return;
    }

    $.ajax({
        url: `/api/favourites?username=${userName}`,
        method: 'GET',
        dataType: 'json'
    })
        .done(function(response) {
            const favoriteMovies = Array.isArray(response) ? response : response.favorites || [];
            console.log("Fetched favorite movies:", favoriteMovies);

            if (favoriteMovies.length === 0) {
                displayNoFavoritesMessage();
                return;
            }

            const movieListContainer = $('<div class="favorite-movies-grid"></div>');

            favoriteMovies.forEach(movie => {
                const movieCard = createMovieCard(movie);
                movieListContainer.append(movieCard);
            });

            $('main').append(movieListContainer);
        })
        .fail(function(xhr, status, error) {
            console.error("Error fetching favorites:", status, error, xhr.responseText);
            showNotification("Failed to load favorite movies", true);
            displayNoFavoritesMessage();
        });

    function createMovieCard(movie) {
        let posterSrc = movie.poster_path;
        if (!posterSrc) {
            posterSrc = "../assets/default-poster.jpg";
        } else if (posterSrc.startsWith('/userimg/')) {
            posterSrc = `http://localhost:8080${posterSrc}`;
        } else if (!posterSrc.startsWith('http')) {
            posterSrc = `https://image.tmdb.org/t/p/w500/${posterSrc}`;
        }

        const movieCard = $(`
            <div class="movie-card" data-movie-id="${movie.movie_id}">
                <img src="${posterSrc}" alt="${movie.title} Poster" class="movie-poster">
                <div class="movie-info">
                    <h3 class="movie-title">${movie.title}</h3>
                    <button class="remove-favorite-btn">Remove from Favorites</button>
                </div>
            </div>
        `);

        movieCard.find('.remove-favorite-btn').click(function() {
            removeFavoriteMovie(movie.movie_id);
        });

        movieCard.find('.movie-poster, .movie-title').click(function() {
            window.location.href = `../components/movieDetails.html?id=${movie.movie_id}`;
        });

        return movieCard;
    }

    function removeFavoriteMovie(movieId) {
        $.ajax({
            url: `/api/favourites/${movieId}?username=${userName}`,
            type: "DELETE",
            success: function () {
                $(`[data-movie-id="${movieId}"]`).remove();
                showNotification("Movie removed from favorites");

                if ($('.favorite-movies-grid .movie-card').length === 0) {
                    displayNoFavoritesMessage();
                }
            },
            error: function (xhr, status, error) {
                console.error("Error removing movie:", status, error, xhr.responseText);
                showNotification("Failed to remove movie from favorites", true);
            }
        });
    }

    function displayNoFavoritesMessage() {
        const noFavoritesMessage = `
            <div class="no-favorites-message">
                <p>You haven't added any favorite movies yet.</p>
                <a href="../components/library.html" class="browse-movies-btn">Browse Movies</a>
            </div>
        `;
        $('main').html(noFavoritesMessage);
    }

    function showNotification(message, isError = false) {
        if ($("#notification").length === 0) {
            $('body').append('<div id="notification"></div>');
        }

        const notificationEl = $("#notification");
        notificationEl.text(message);
        notificationEl.css({
            'position': 'fixed',
            'top': '20px',
            'right': '20px',
            'padding': '10px',
            'background-color': isError ? '#ff6b6b' : '#4ecdc4',
            'color': 'white',
            'border-radius': '5px',
            'box-shadow': '0 2px 5px rgba(0,0,0,0.2)',
            'max-width': '300px',
            'text-align': 'center',
            'z-index': '1000',
            'display': 'none'
        });

        notificationEl.fadeIn().delay(3000).fadeOut();
    }

    function showNotificationAndRedirect(message, redirectUrl) {
        if (!window.redirecting) {
            window.redirecting = true;
            showNotification(message, true);
            setTimeout(() => { window.location.href = redirectUrl; }, 3000);
        }
    }
});
