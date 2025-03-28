import { isUserSignedIn } from './headerfooter.js';

$(document).ready(function () {
    const favoriteButton = $("#favourite-btn");
    const movieId = new URLSearchParams(window.location.search).get('id');

    if (!favoriteButton.length || !movieId) return;

    // Get the logged-in user's username from localStorage
    console.log("Stored userName:", localStorage.getItem('userName'));
    console.log("Stored username:", localStorage.getItem('username'));
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

            $.ajax({
                url: `/api/movies/${movieId}`,
                method: 'GET',
                dataType: 'json'
            })
                .done(function (movieData) {
                    console.log("API Response:", movieData);

                    // Display movie details like title, poster, and genres
                    const { title, poster_path, synopsis, release_date, genres } = movieData;
                    $("#movie-title").text(title);
                    $("#movie-release-date").text(release_date);

                    // Handle poster image URL
                    let posterSrc = poster_path;
                    if (!posterSrc) {
                        posterSrc = "/path/to/default/poster.jpg"; // Default poster if none is found
                    } else if (posterSrc.startsWith('/userimg/')) {
                        posterSrc = `http://localhost:8080${posterSrc}`;
                    } else if (!posterSrc.startsWith('http')) {
                        posterSrc = `https://image.tmdb.org/t/p/w500/${posterSrc}`;
                    }
                    $("#movie-poster").attr("src", posterSrc);

                    // Display movie genres
                    const genreNames = Array.isArray(genres) ? genres.join(', ') : genres;
                    $("#movie-genres").text(genreNames);
                    $("#movie-overview").text(synopsis);

                    // Check if movie is in favorites
                    checkFavoriteStatus(userName, movieId, favoriteButton);
                })
                .fail(function (xhr, status, error) {
                    console.error("Error fetching movie data:", status, error);
                    showNotification("Failed to load movie details", true);
                });
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
            id: movieId, // Ensure this is the external movie ID
            movie_id: movieId, // Add this to explicitly set movie_id
            title: $("#movie-title").text(),
            release_date: $("#movie-release-date").text(),
            poster_path: $("#movie-poster").attr("src"),
            genres: $("#movie-genres").text(),
            synopsis: $("#movie-overview").text(),
            pg_rating: "Not Rated",
            runtime: 0,
            production_companies: "Unknown",
            spoken_languages: "English"
        };

        if (newStatus) {
            // Add movie to favorites
            $.ajax({
                url: `/api/favourites?username=${userName}`,
                type: "POST",
                contentType: "application/json",
                data: JSON.stringify(movie),
                success: function (response) {
                    showNotification("Movie added to favorites!");
                },
                error: function (xhr, status, error) {
                    console.error("Error adding movie:", xhr.status, xhr.statusText, xhr.responseText);
                    favoriteButton.html("♡");
                    showNotification("Failed to add movie to favorites", true);
                }
            });
        } else {
            // Remove movie from favorites
            $.ajax({
                url: `/api/favourites/${movieId}?username=${userName}`,
                type: "DELETE",
                success: function (response) {
                    showNotification("Movie removed from favorites");
                },
                error: function (xhr, status, error) {
                    console.error("Error removing movie:", xhr.status, xhr.statusText, xhr.responseText);
                    if (xhr.status === 404) {
                        showNotification("Movie not found in favorites", true);
                    } else {
                        showNotification("Failed to remove movie from favorites", true);
                    }
                    favoriteButton.html("♥");
                }
            });
        }
    });

    function checkFavoriteStatus(username, movieId, favButton) {
        $.ajax({
            url: `/api/favourites?username=${username}`,
            method: 'GET',
            dataType: 'json'
        })
            .done(function (movies) {
                const favoriteMovies = Array.isArray(movies) ? movies : (movies.favorites || []);
                const matchedMovie = favoriteMovies.find(movie =>
                    movie.title === $("#movie-title").text() &&
                    movie.release_date === $("#movie-release-date").text()
                );

                if (matchedMovie) {
                    favButton.data('favoriteId', matchedMovie.id);
                    favButton.html("♥");
                } else {
                    favButton.html("♡");
                }
            })
            .fail(function (xhr, status, error) {
                console.error("Error fetching favorites:", status, error);
                favButton.html("♡");
            });
    }

    // Simple notification function
    function showNotification(message, isError = false) {
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
