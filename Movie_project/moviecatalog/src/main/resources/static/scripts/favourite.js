import { isUserSignedIn } from './headerfooter.js';

document.addEventListener("DOMContentLoaded", function () {
    const favoriteButton = document.getElementById("favourite-btn");
    const movieId = new URLSearchParams(window.location.search).get('id');

    // Check if favorite button and movie ID exist
    if (!favoriteButton || !movieId) return;

    // Check login status and add click event for non-logged-in users
    favoriteButton.addEventListener("click", function () {
        // Check if user is not signed in
        if (!isUserSignedIn()) {
            // Show alert
            alert("You need to sign in to add movies to your favorites.");
            return;
        }

        // Toggle favorite status for logged-in users
        const currentlyFavorited = favoriteButton.innerHTML.trim() === "♡";

        // Update button appearance
        favoriteButton.innerHTML = currentlyFavorited ? "♥" : "♡";

        // Store favorite status in localStorage
        localStorage.setItem(`favorite-${movieId}`, currentlyFavorited ? "true" : "false");
    });

    // If user is logged in, set initial favorite status
    if (isUserSignedIn()) {
        const isFavorited = localStorage.getItem(`favorite-${movieId}`) === "true";
        favoriteButton.innerHTML = isFavorited ? "♥" : "♡";
    }
});