import { isUserSignedIn } from './headerfooter.js';

$(document).ready(function () {
    const favoriteButton = $("#favourite-btn");
    const movieId = new URLSearchParams(window.location.search).get('id');

    if (!favoriteButton.length || !movieId) return;

    // Get the logged-in user's username from localStorage
    console.log("Stored userName:", localStorage.getItem('userName'));
    const userName = localStorage.getItem('userName')?.trim();
    if (!userName) {
        console.error("No valid username found");
        return;
    }

    // Fetch the movie details using the movieId
    $.ajax({
        url: `/api/movies/${movieId}`,
        method: 'GET',
        dataType: 'json'
    })
        .done(function (movieData) {
            console.log("API Response:", movieData); // Log the full API response

            // Log the specific properties to check their existence
            const { title, poster_path, synopsis, release_date, genres, runtime} = movieData;
            console.log("Title:", title, "Poster Path:", poster_path, "Synopsis:", synopsis, "Runtime:", runtime); // Check if properties exist

            // Populate the movie details on the page
            $("#movie-title").text(title);
            $("#movie-release-date").text(release_date);

            let posterSrc = poster_path;
            if (!posterSrc) {
                console.warn("No poster path found for the movie");
                posterSrc = "/path/to/default/poster.jpg"; // Default poster if missing
            } else if (posterSrc.startsWith('/userimg/')) {
                posterSrc = `http://localhost:8080${posterSrc}`; // Handle local server paths
            } else if (!posterSrc.startsWith('http')) {
                posterSrc = `https://image.tmdb.org/t/p/w500/${posterSrc}`; // Handle external links
            }
            $("#movie-poster").attr("src", posterSrc);
            const genreNames = Array.isArray(genres) ? genres.join(', ') : genres;
            $("#movie-genres").text(genreNames);
            $("#movie-overview").text(synopsis);
            $("#movie-runtime").text(runtime);
            checkFavoriteStatus(userName, movieId, favoriteButton);

        })
        .fail(function (xhr, status, error) {
            console.error("Error fetching movie data:", status, error);
            console.error("Response status:", xhr.status);
            console.error("Response text:", xhr.responseText);
            showNotification("Failed to load movie details", true);
        });

    // Handle the favorite button click
    favoriteButton.click(function () {
        if (!isUserSignedIn()) {
            alert("You need to sign in to add movies to your favorites.");
            return;
        }

        const currentlyFavorited = favoriteButton.html().trim() === "♥";
        const newStatus = !currentlyFavorited;
        favoriteButton.html(newStatus ? "♥" : "♡");

        const movie = {
            movie_id: movieId,
            title: $("#movie-title").text(),
            release_date: $("#movie-release-date").text(),
            poster_path: $("#movie-poster").attr("src"),
            genres: $("#movie-genres").text(),
            synopsis: $("#movie-overview").text(),
        };

        if (newStatus) {
            // Check if the movie is already in favorites to prevent duplicates
            $.ajax({
                url: `/api/favourites?username=${userName}`,
                method: "GET",
                dataType: "json",
                success: function (movies) {
                    const favoriteMovies = Array.isArray(movies) ? movies : movies.favorites || [];
                    if (favoriteMovies.some(m => m.movie_id === movieId)) {
                        console.log("Movie already exists in favorites, skipping addition.");
                        return;
                    }

                    // Only add if not already present
                    $.ajax({
                        url: `/api/favourites?username=${userName}`,
                        type: "POST",
                        contentType: "application/json",
                        data: JSON.stringify(movie),
                        success: function (response) {
                            console.log("Movie added to favorites:", response);
                            showNotification("Movie added to favorites!");
                        },
                        error: function (xhr, status, error) {
                            console.error("Error adding movie:", status, error);
                            favoriteButton.html("♡");
                            showNotification("Failed to add movie to favorites", true);
                        }
                    });
                }
            });
        } else {
            // Remove from favorites
            $.ajax({
                url: `/api/favourites/${movieId}?username=${userName}`,
                type: "DELETE",
                success: function (response) {
                    console.log("Movie removed from favorites:", response);
                    showNotification("Movie removed from favorites");
                },
                error: function (xhr, status, error) {
                    console.error("Error removing movie:", status, error);
                    favoriteButton.html("♥");
                    showNotification("Failed to remove movie from favorites", true);
                }
            });
        }
    });


    function checkFavoriteStatus(username, movieId, favButton) {
        console.log("Checking favorite status for:", username, movieId);

        $.ajax({
            url: `/api/favourites?username=${username}`,
            method: 'GET',
            dataType: 'json'
        })
            .done(function (movies) {
                console.log("Fetched favorite movies:", movies);

                // Ensure `movies` is treated as an array
                const favoriteMovies = Array.isArray(movies) ? movies : movies.favorites || [];

                const isFavorited = favoriteMovies.some(movie => movie.movie_id === movieId);
                favButton.html(isFavorited ? "♥" : "♡");
            })
            .fail(function (xhr, status, error) {
                console.error("Error fetching favorites:", status, error);
                favButton.html("♡");
            });
    }



    // Simple notification function
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
});
